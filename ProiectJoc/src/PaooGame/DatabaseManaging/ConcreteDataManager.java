package PaooGame.DatabaseManaging;

import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import java.sql.*;
import org.sqlite.JDBC;

/**
 * @class ConcreteDataManager
 * @brief Implements the DataManager interface to manage game data using an in-memory buffer and an SQLite database.
 *
 * This class provides a concrete way to save and load game state. It uses a HashMap as an
 * in-memory buffer for quick access to game variables and interacts with an SQLite database
 * for persistent storage of game saves and high scores.
 * It follows a Singleton pattern for instantiation.
 */
public class ConcreteDataManager implements DataManager{

    private Map<String,Integer> buffer; ///< In-memory buffer to hold current game state variables.
    private RefLinks reflink; ///< Reference to shared game resources and utilities.

    Connection c = null; ///< JDBC Connection object for database interaction.
    Statement stmt = null; ///< JDBC Statement object for executing SQL.
    PreparedStatement pstmt = null; ///< JDBC PreparedStatement object for parameterized SQL.

    private static ConcreteDataManager instance = null; ///< Singleton instance of this class.

    /**
     * @brief Gets the singleton instance of ConcreteDataManager.
     *
     * If an instance does not exist, it creates one. This ensures that only one
     * ConcreteDataManager object is used throughout the application.
     *
     * @param reflink A reference to shared game resources, needed for initialization.
     * @return The singleton instance of ConcreteDataManager.
     */
    public static ConcreteDataManager getInstance(RefLinks reflink){
        if(ConcreteDataManager.instance == null){
            ConcreteDataManager.instance = new ConcreteDataManager(reflink);
        }
        return instance;
    }


    /**
     * @brief Private constructor to enforce the Singleton pattern.
     * Initializes the in-memory buffer with default values for all game state variables.
     * @param reflink A reference to shared game resources.
     */
    private ConcreteDataManager(RefLinks reflink){
        this.reflink = reflink;
        this.buffer = new HashMap<>();
        // Initialize buffer with keys from Constants and default values (-1 or specific starting values).
        this.buffer.put(Constants.CURRENT_STATE,-1);

        this.buffer.put(Constants.HERO_HEALTH,-1);
        this.buffer.put(Constants.HERO_X,-1);
        this.buffer.put(Constants.HERO_Y,-1);
        this.buffer.put(Constants.HERO_HAS_WHIP,-1);
        this.buffer.put(Constants.HERO_NR_OF_FLEES,-1);
        this.buffer.put(Constants.HERO_NR_OF_COLLECTED_SAVES,-1);
        this.buffer.put(Constants.HERO_NR_OF_FINISHED_LEVELS,-1);
        this.buffer.put(Constants.HERO_GOLD,-1);

        this.buffer.put(Constants.TIGER0_HEALTH,-1);
        this.buffer.put(Constants.TIGER1_HEALTH,-1);

        this.buffer.put(Constants.BASIC_SKELETON0_HEALTH,-1);
        this.buffer.put(Constants.BASIC_SKELETON1_HEALTH,-1);
        this.buffer.put(Constants.STRONG_SKELETON0_HEALTH,-1);

        this.buffer.put(Constants.BOSS_HEALTH,-1);
        this.buffer.put(Constants.MINOTAUR0_HEALTH,-1);
        this.buffer.put(Constants.MINOTAUR1_HEALTH,-1);
        this.buffer.put(Constants.GHOST0_HEALTH,-1);
        this.buffer.put(Constants.GHOST1_HEALTH,-1);

        this.buffer.put(Constants.TIMESTAMP,-1);


    }

    /**
     * @brief Loads a value from the in-memory buffer.
     * @param key The key identifying the value to load (defined in Constants).
     * @param access A boolean flag (currently unused in this implementation, potentially for future permission checks).
     * @return The integer value associated with the key from the buffer.
     */
    @Override
    public int load(String key, boolean access) {
        return this.buffer.get(key);
    }

    /**
     * @brief Stores a value into the in-memory buffer.
     * @param key The key identifying the value to store (defined in Constants).
     * @param value The integer value to store.
     * @param access A boolean flag (currently unused in this implementation, potentially for future permission checks).
     */
    @Override
    public void store(String key, int value, boolean access) {
        this.buffer.put(key,value);
    }

    /**
     * @brief Resets the in-memory buffer to default starting game state values.
     * This is typically used when starting a new game.
     * @param access A boolean flag (currently unused, potentially for future permission checks).
     */
    @Override
    public void resetBuffer(boolean access){
        // Set buffer values to initial game start conditions.
        this.buffer.put(Constants.CURRENT_STATE,1); //lvl1

        this.buffer.put(Constants.HERO_HEALTH,100); //full health
        this.buffer.put(Constants.HERO_X,Constants.HERO_LEVEL1_STARTING_X);
        this.buffer.put(Constants.HERO_Y,Constants.HERO_LEVEL1_STARTING_Y);
        this.buffer.put(Constants.HERO_HAS_WHIP,0); // 0 for false
        this.buffer.put(Constants.HERO_NR_OF_FLEES,2);
        this.buffer.put(Constants.HERO_NR_OF_COLLECTED_SAVES,0);
        this.buffer.put(Constants.HERO_NR_OF_FINISHED_LEVELS,0);
        this.buffer.put(Constants.HERO_GOLD,0);

        this.buffer.put(Constants.TIGER0_HEALTH,100);
        this.buffer.put(Constants.TIGER1_HEALTH,100);

        this.buffer.put(Constants.BASIC_SKELETON0_HEALTH,100);
        this.buffer.put(Constants.BASIC_SKELETON1_HEALTH,100);
        this.buffer.put(Constants.STRONG_SKELETON0_HEALTH,100);

        this.buffer.put(Constants.BOSS_HEALTH,100);
        this.buffer.put(Constants.MINOTAUR0_HEALTH,100);
        this.buffer.put(Constants.MINOTAUR1_HEALTH,100);
        this.buffer.put(Constants.GHOST0_HEALTH,100);
        this.buffer.put(Constants.GHOST1_HEALTH,100);

        this.buffer.put(Constants.TIMESTAMP, (int)Instant.now().getEpochSecond());

        this.reflink.setDataRefreshSignal(true); // Signal that buffer data has been refreshed.
    }

    /**
     * @brief Loads the latest game save from the SQLite database into the in-memory buffer.
     * If the database or table doesn't exist, it attempts to create them.
     * @param access A boolean flag (currently unused, potentially for future permission checks).
     */
    @Override
    public void loadBuffer(boolean access) {
        ResultSet rs = null; // Declare ResultSet outside try to access in finally
        try {
            DriverManager.registerDriver(new JDBC()); // Ensure SQLite driver is registered.
            this.c = DriverManager.getConnection(Constants.DB_URL); // Establish connection.
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD); // Ensure table exists.

            this.pstmt = c.prepareStatement(Constants.QUERY_SELECT_LATEST_SAVE_CMD);
            rs = pstmt.executeQuery(); // Get the latest save.

            if(rs.next()){ // If a save exists
                for(String token : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
                    store(token, rs.getInt(token),access); // Populate buffer from ResultSet.
                }
            }
            // If no save exists, buffer retains its current (possibly default or reset) values.
        }catch (SQLIntegrityConstraintViolationException e) {
            System.err.println();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            // Clean up database resources.
            try {
                if (rs != null ) rs.close(); // Close ResultSet if opened
                if (pstmt != null ) pstmt.close(); // Close PreparedStatement if opened
                if (stmt != null ) stmt.close(); // Close Statement if opened
                if (c != null ) c.close(); // Close Connection if opened
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }
    }

    /**
     * @brief Stores the current state of the in-memory buffer into the SQLite database as a new game save.
     * Manages the maximum number of save entries, deleting the oldest if the limit is reached.
     * @param access A boolean flag (currently unused, potentially for future permission checks).
     */
    @Override
    public void storeBuffer(boolean access) {
        ResultSet rs = null; // Declare ResultSet outside try
        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD); // Ensure table exists.

            rs = stmt.executeQuery(Constants.QUERY_NR_OF_ENTRIES_CMD);
            int nrOfEntries = 0;
            if(rs.next()){
                nrOfEntries = rs.getInt(1);
            }

            if (rs != null && !rs.isClosed()) rs.close();


            if(nrOfEntries>=Constants.DB_MAX_ENTRIES){
                stmt.executeUpdate(Constants.DELETE_OLDEST_ENTRY_CMD); // Delete oldest if max entries reached.
            }

            pstmt = c.prepareStatement(Constants.INSERT_CMD);
            // Populate PreparedStatement with values from the buffer.
            // Loop should go up to ALL_DATA_MANAGEMENT_CONSTANTS.length
            for(int i = 0; i < Constants.ALL_DATA_MANAGEMENT_CONSTANTS.length; ++i){
                pstmt.setInt(i+1,load(Constants.ALL_DATA_MANAGEMENT_CONSTANTS[i],access));
            }


            pstmt.executeUpdate();
            c.commit(); // Commit transaction.

        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " +e.getMessage());
            try {
                if(c!=null){
                    c.rollback(); // Rollback on error.
                }
            } catch (SQLException ex) {
                System.err.println("Failed rolling back!.\n");
            }
        }finally {
            // Clean up database resources.
            try {
                // rs might have been closed already, or not opened in case of early exception
                if (rs != null && !rs.isClosed()) rs.close();
                if (pstmt != null && !pstmt.isClosed()) pstmt.close();
                if (stmt != null && !stmt.isClosed()) stmt.close();
                if (c != null && !c.isClosed()) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }
    }

    /**
     * @brief Loads high scores from the database.
     * Scores are packed into a single integer in a special database row.
     * @param access A boolean flag (currently unused).
     * @return An array of 3 integers representing the top three scores. Returns default values if no scores found.
     */
    @Override
    public int[] loadScore(boolean access) {
        int nr=1000000000; // Default packed value
        ResultSet rs = null; // Declare ResultSet outside try
        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD); // Ensure table exists.

            this.pstmt = c.prepareStatement(Constants.GET_SCORE_LINE_CMD);
            rs = pstmt.executeQuery();

            if(rs.next()){
                nr = rs.getInt(Constants.CURRENT_STATE); // Scores are stored in CURRENT_STATE column for the special score row.
            }
        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(); // Original error handling
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) rs.close();
                if (pstmt != null && !pstmt.isClosed()) pstmt.close();
                if (stmt != null && !stmt.isClosed()) stmt.close();
                if (c != null && !c.isClosed()) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }
        // Unpack scores.
        int score1 = nr%1000;
        int score2 = (nr%1000000)/(1000);
        int score3 = (nr%1000000000)/(1000000) ;
        int[] scores = new int[3]; scores[0] = score1; scores[1] = score2; scores[2] = score3;
        return scores;
    }

    /**
     * @brief Stores high scores into the database.
     * Scores are packed into a single integer and stored in a special row (identified by TIMESTAMP = -2).
     * If the score row doesn't exist, it's inserted; otherwise, it's updated.
     * @param access A boolean flag (currently unused).
     * @param score1 The first high score.
     * @param score2 The second high score.
     * @param score3 The third high score.
     */
    @Override
    public void storeScore(boolean access, int score1, int score2, int score3) {
        int nr=1000000000; // Base for packing
        if((new File(Constants.DB_PATH)).exists()){ // Check if DB file exists
            nr = 1000000000 + score1 + score2*1000 + score3 * 1000000;
        }
        // Temporarily modify buffer for storing the score row.
        int oldTimestamp = this.load(Constants.TIMESTAMP,access);
        int oldState = this.load(Constants.CURRENT_STATE,access);
        this.store(Constants.TIMESTAMP,-2,access); // Special TIMESTAMP for score row.
        this.store(Constants.CURRENT_STATE,nr,access); // Store packed scores in CURRENT_STATE field.

        ResultSet rs = null; // Declare ResultSet outside try
        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD); // Ensure table exists.

            this.pstmt = c.prepareStatement(Constants.GET_SCORE_LINE_CMD);
            rs = pstmt.executeQuery();

            if(rs.next()){ //exista deja - If score row already exists, update it.
                // Close the current ResultSet before creating a new PreparedStatement for update
                if(rs != null && !rs.isClosed()) rs.close();
                try{
                    this.pstmt = c.prepareStatement(Constants.UPDATE_SCORE_LINE_CMD);
                    this.pstmt.setInt(1, nr);  // value to set for CURRENT_STATE (packed scores)
                    this.pstmt.executeUpdate();
                    c.commit();
                } catch (SQLException e) {
                    e.printStackTrace(); // Original error handling
                }
            }
            else{ // If score row does not exist, insert it.
                // Close the current ResultSet before creating a new PreparedStatement for insert
                if(rs != null && !rs.isClosed()) rs.close();
                pstmt = c.prepareStatement(Constants.INSERT_CMD);
                // Populate most fields with current buffer values (some will be irrelevant for score row).
                // Loop should go up to ALL_DATA_MANAGEMENT_CONSTANTS.length
                for(int i = 0;i < Constants.ALL_DATA_MANAGEMENT_CONSTANTS.length; ++i){
                    pstmt.setInt(i+1,load(Constants.ALL_DATA_MANAGEMENT_CONSTANTS[i],access));
                }
                // Specifically set TIMESTAMP and CURRENT_STATE for the score row.
                pstmt.setInt(1,-2); // TIMESTAMP
                pstmt.setInt(2,nr);  // CURRENT_STATE (packed scores)

                pstmt.executeUpdate();
                c.commit();
            }

        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(); // Original error handling
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) rs.close();
                if (pstmt != null && !pstmt.isClosed()) pstmt.close();
                if (stmt != null && !stmt.isClosed()) stmt.close();
                if (c != null && !c.isClosed()) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }

        // Restore original buffer values for TIMESTAMP and CURRENT_STATE.
        this.store(Constants.TIMESTAMP,oldTimestamp,access);
        this.store(Constants.CURRENT_STATE,oldState,access);
    }
}