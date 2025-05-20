package PaooGame.DatabaseManaging;

import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import java.sql.*;
import org.sqlite.JDBC;


public class ConcreteDataManager implements DataManager{

    private Map<String,Integer> buffer;
    private RefLinks reflink;

    Connection c = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    private static ConcreteDataManager instance = null;
    public static ConcreteDataManager getInstance(RefLinks reflink){
        if(ConcreteDataManager.instance == null){
            ConcreteDataManager.instance = new ConcreteDataManager(reflink);
        }
        return instance;
    }


    private ConcreteDataManager(RefLinks reflink){
        this.reflink = reflink;
        this.buffer = new HashMap<>();
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

    @Override
    public int load(String key, boolean access) {
        return this.buffer.get(key);
    }

    @Override
    public void store(String key, int value, boolean access) {
        this.buffer.put(key,value);
    }

    @Override
    public void resetBuffer(boolean access){
        this.buffer.put(Constants.CURRENT_STATE,1); //lvl1

        this.buffer.put(Constants.HERO_HEALTH,100); //full health
        this.buffer.put(Constants.HERO_X,Constants.HERO_LEVEL1_STARTING_X);
        this.buffer.put(Constants.HERO_Y,Constants.HERO_LEVEL1_STARTING_Y);
        this.buffer.put(Constants.HERO_HAS_WHIP,0);
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

        this.reflink.setDataRefreshSignal(true);



    }

    @Override
    public void loadBuffer(boolean access) {
        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD);

            this.pstmt = c.prepareStatement(Constants.QUERY_SELECT_LATEST_SAVE_CMD);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                for(String token : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
                    store(token, rs.getInt(token),access);
                }

            }

        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }


    }

    @Override
    public void storeBuffer(boolean access) {
        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD);

            ResultSet rs = stmt.executeQuery(Constants.QUERY_NR_OF_ENTRIES_CMD);
            int nrOfEntries = 0;
            if(rs.next()){
                nrOfEntries = rs.getInt(1);
            }

            if(nrOfEntries>=Constants.DB_MAX_ENTRIES){
                stmt.executeUpdate(Constants.DELETE_OLDEST_ENTRY_CMD);
            }


            pstmt = c.prepareStatement(Constants.INSERT_CMD);

            for(int i = 0;i<Constants.NR_OF_DB_CONSTANTS;++i){
                pstmt.setInt(i+1,load(Constants.ALL_DATA_MANAGEMENT_CONSTANTS[i],access));
            }

            pstmt.executeUpdate();

            c.commit();




        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " +e.getMessage());
            try {
                if(c!=null){
                    c.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Failed rolling back!.\n");
            }

        }finally {
            try {
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }



    }

    @Override
    public int[] loadScore(boolean access) {
        int nr=1000000000;
        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD);

            this.pstmt = c.prepareStatement(Constants.GET_SCORE_LINE_CMD);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                nr = rs.getInt(Constants.CURRENT_STATE);
            }

        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }
        int score1 = nr%1000;
        int score2 = (nr%1000000)/(1000);
        int score3 = (nr%1000000000)/(1000000) ;
        int[] scores = new int[3]; scores[0] = score1; scores[1] = score2; scores[2] = score3;
        return scores;
    }

    @Override
    public void storeScore(boolean access, int score1, int score2, int score3) {
        int nr=1000000000;
        if((new File(Constants.DB_PATH)).exists()){
            nr = 1000000000 + score1 + score2*1000 + score3 * 1000000;

        }
        int oldTimestamp = this.load(Constants.TIMESTAMP,access);
        int oldState = this.load(Constants.CURRENT_STATE,access);
        this.store(Constants.TIMESTAMP,-2,access);
        this.store(Constants.CURRENT_STATE,nr,access);

        try {
            DriverManager.registerDriver(new JDBC());
            this.c = DriverManager.getConnection(Constants.DB_URL);
            c.setAutoCommit(false);
            this.stmt = c.createStatement();
            stmt.executeUpdate(Constants.CREATE_TABLE_CMD);

            this.pstmt = c.prepareStatement(Constants.GET_SCORE_LINE_CMD);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){ //exista deja
                try{
                    this.pstmt = c.prepareStatement(Constants.UPDATE_SCORE_LINE_CMD);
                    this.pstmt.setInt(1, nr);  // value to set
                    this.pstmt.executeUpdate();
                    c.commit();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                pstmt = c.prepareStatement(Constants.INSERT_CMD);

                for(int i = 0;i<Constants.NR_OF_DB_CONSTANTS;++i){
                    pstmt.setInt(i+1,load(Constants.ALL_DATA_MANAGEMENT_CONSTANTS[i],access));
                }
                pstmt.setInt(1,-2);
                pstmt.setInt(2,nr);

                pstmt.executeUpdate();
                c.commit();
            }

        }catch (SQLIntegrityConstraintViolationException e) {
            System.out.println();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException ex) {
                System.err.println("Resource cleanup failed: " + ex.getMessage());
            }
        }

        this.store(Constants.TIMESTAMP,oldTimestamp,access);
        this.store(Constants.CURRENT_STATE,oldState,access);

    }
}
