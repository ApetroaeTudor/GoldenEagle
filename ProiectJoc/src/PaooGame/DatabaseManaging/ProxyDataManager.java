package PaooGame.DatabaseManaging;

import PaooGame.Config.Constants;
import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;
import PaooGame.RefLinks;

/**
 * @class ProxyDataManager
 * @brief Implements the DataManager interface using a Proxy pattern to control access and add validation to a ConcreteDataManager.
 *
 * This class acts as an intermediary (proxy) for a {@link ConcreteDataManager} instance.
 * It enforces access control (via the `access` boolean parameter), validates input keys and values
 * before delegating operations to the concrete manager, and ensures the concrete manager is instantiated.
 * This separation allows for additional logic (like security checks or validation) without modifying
 * the core data management implementation.
 */
public class ProxyDataManager implements DataManager {

    private ConcreteDataManager concreteDataManager; ///< The actual data manager instance that handles the core data operations.
    private RefLinks reflink; ///< Reference to shared game resources and utilities.

    /**
     * @brief Constructs a ProxyDataManager.
     * @param reflink A reference to shared game resources, used for instantiating the ConcreteDataManager if needed.
     */
    public ProxyDataManager(RefLinks reflink){
        this.reflink = reflink;
        // The concreteDataManager is lazily initialized when first needed.
    }

    /**
     * @brief Loads an integer value associated with a given key, with access control and validation.
     * @param key The unique identifier for the data to be loaded.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @return The integer value associated with the key.
     * @throws AccessNotPermittedException If `access` is false.
     * @throws ValueStoreException If the provided `key` is not a valid known constant.
     * @throws DataBufferNotReadyException If the loaded value from the concrete manager is -1 (indicating data not ready).
     */
    @Override
    public int load(String key, boolean access) throws AccessNotPermittedException, ValueStoreException, DataBufferNotReadyException {
        if(!access){
            throw new AccessNotPermittedException("Concrete Data Manager");
        }

        boolean keyFound = false;
        for(String token : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
            if(key.compareTo(token)==0){ // Check if the key is a known constant.
                keyFound = true;
                break; // Found the key, no need to continue loop.
            }
        }
        if(!keyFound){
            throw new ValueStoreException("Invalid key given.\n");
        }

        if(this.concreteDataManager == null){ // Lazy initialization of the concrete manager.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }

        int returnVal = this.concreteDataManager.load(key,true); // Delegate to concrete manager.
        if( returnVal ==  -1){ // Check if the loaded value indicates an uninitialized state.
            throw new DataBufferNotReadyException();
        }
        return returnVal;
    }

    /**
     * @brief Stores an integer value associated with a given key, with access control and input validation.
     * @param key The unique identifier for the data to be stored.
     * @param value The integer value to store.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @throws AccessNotPermittedException If `access` is false.
     * @throws ValueStoreException If the `key` is invalid or the `value` is out of expected range for the given key.
     */
    @Override
    public void store(String key, int value, boolean access) throws AccessNotPermittedException, ValueStoreException {
        if(!access){
            throw new AccessNotPermittedException("Concrete Data Maganer"); // Typo: "Maganer" should be "Manager"
        }
        // Perform validation based on the key.
        switch (key){
            case Constants.CURRENT_STATE:
                if( !(value==1 || value==2 || value==3) ){ // Valid level IDs.
                    throw new ValueStoreException("Invalid level ID given.\n");
                }
                break;
            // Health values validation.
            case Constants.HERO_HEALTH:
            case Constants.TIGER0_HEALTH:
            case Constants.TIGER1_HEALTH:
            case Constants.BASIC_SKELETON0_HEALTH:
            case Constants.BASIC_SKELETON1_HEALTH:
            case Constants.STRONG_SKELETON0_HEALTH:
            case Constants.BOSS_HEALTH:
            case Constants.MINOTAUR0_HEALTH:
            case Constants.MINOTAUR1_HEALTH:
            case Constants.GHOST0_HEALTH:
            case Constants.GHOST1_HEALTH:
                if( value<0 || value > 100){ // Health between 0 and 100.
                    throw new ValueStoreException("Invalid argument given.\nHealth has to be between 0 and 100.\n");
                }
                break;
            case Constants.HERO_HAS_WHIP:
                if( !(value == 0 || value ==1)){ // Boolean represented as 0 or 1.
                    throw new ValueStoreException("Invalid argument given.\nThis data is boolean so please insert only 0/1.\n");
                }
                break;
            case Constants.HERO_NR_OF_FLEES:
                if( value < 0 || value >this.reflink.getHero().getMaxNrOfEscapes()){
                    throw new ValueStoreException("Invalid argument given.\nThe value has to be between 0 and " + this.reflink.getHero().getMaxNrOfEscapes()+".\n");
                }
                break;
            case Constants.HERO_NR_OF_COLLECTED_SAVES:
                if( value < 0 || value >6){ // Max 6 collected saves.
                    throw new ValueStoreException("Invalid argument given.\nThe value has to be between 0 and  6.\n");
                }
                break;
            case Constants.HERO_NR_OF_FINISHED_LEVELS:
                if(!(value == 0 || value == 1 || value ==2 || value == 3)){ // Max 3 finished levels.
                    throw new ValueStoreException("Invalid argument given.\nThe value has to be between 0 and  3.\n");
                }
                break;
            case Constants.HERO_GOLD:
                if(value<0){ // Gold must be non-negative.
                    throw new ValueStoreException("Invalid amount of gold!\nIt has to be positive!\n");
                }
                break;
            // Coordinates and timestamp validation.
            case Constants.HERO_X:
            case Constants.HERO_Y:
            case Constants.TIMESTAMP:
                if(value<0 && key.equals(Constants.TIMESTAMP) && value != -2){ // Allow -2 for score row timestamp
                    throw new ValueStoreException("Invalid argument given.\nThe timestamp epoch time value should always be positive (or -2 for scores).\n");
                } else if (value < 0 && !key.equals(Constants.TIMESTAMP)) {
                    throw new ValueStoreException("Invalid argument given.\nCoordinates should be positive.\n");
                }
                break;
            default:
                boolean isKnownKey = false;
                for(String token : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
                    if(key.compareTo(token)==0){
                        isKnownKey = true;
                        break;
                    }
                }
                if(!isKnownKey) {
                    throw new ValueStoreException("Invalid key given.\n");
                }
                // If key is known but not handled by specific cases above, it passes (e.g. enemy positions if they were stored)
                break;
        }

        if (this.concreteDataManager == null){ // Lazy initialization.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        this.concreteDataManager.store(key,value,true); // Delegate to concrete manager.
    }

    /**
     * @brief Resets the internal data buffer to a default state, with access control.
     * Also restores the state of game levels.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @throws AccessNotPermittedException If `access` is false.
     */
    @Override
    public void resetBuffer(boolean access) throws AccessNotPermittedException{
        if(!access){
            throw new AccessNotPermittedException("Concrete Data Manager");
        }
        if (this.concreteDataManager == null){ // Lazy initialization.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        // Restore states of individual levels before resetting the buffer.
        this.reflink.getGame().getLevel1State().restoreState();
        this.reflink.getGame().getLevel2State().restoreState();
        this.reflink.getGame().getLevel3State().restoreState();
        this.concreteDataManager.resetBuffer(true); // Delegate to concrete manager.
    }

    /**
     * @brief Loads data from a persistent source into the buffer, with access control.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @throws AccessNotPermittedException If `access` is false.
     */
    @Override
    public void loadBuffer(boolean access) throws AccessNotPermittedException {
        if(!access){
            throw new AccessNotPermittedException("Concrete Data Manager");
        }

        if(this.concreteDataManager == null){ // Lazy initialization.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        this.concreteDataManager.loadBuffer(access); // Delegate to concrete manager.
    }

    /**
     * @brief Stores the current buffer state to a persistent source, with access control and data validation.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @throws AccessNotPermittedException If `access` is false.
     * @throws ValueStoreException If any data in the buffer is found to be invalid (-1) before storing.
     */
    @Override
    public void storeBuffer(boolean access) throws AccessNotPermittedException, ValueStoreException {
        if(!access){
            throw new AccessNotPermittedException("Concrete Data Manager");
        }
        if(this.concreteDataManager == null){ // Lazy initialization.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }

        boolean invalidData=false;
        // Check if any essential data in the buffer is uninitialized (-1).
        for(String key : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
            if(this.concreteDataManager.load(key,access)==-1){ // Assuming true access for internal check.
                invalidData = true;
                break; // Found invalid data, no need to check further.
            }
        }

        if(invalidData){
            throw new ValueStoreException("Can't currently store data in the database.\nSome values are invalid.\n");
        }
        this.concreteDataManager.storeBuffer(access); // Delegate to concrete manager.
    }

    /**
     * @brief Loads game scores, with access control.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @return An array of integers representing the loaded scores.
     * @throws AccessNotPermittedException If `access` is false.
     */
    @Override
    public int[] loadScore(boolean access) throws AccessNotPermittedException {
        if(!access){
            throw new AccessNotPermittedException("Load from the Database");
        }

        if(this.concreteDataManager == null){ // Lazy initialization.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        return this.concreteDataManager.loadScore(access); // Delegate to concrete manager.
    }

    /**
     * @brief Stores game scores, with access control and score validation.
     * @param access A boolean flag; if false, an AccessNotPermittedException is thrown.
     * @param score1 The first score to store.
     * @param score2 The second score to store.
     * @param score3 The third score to store.
     * @throws AccessNotPermittedException If `access` is false.
     * @throws ValueStoreException If any score is outside the valid range (0-999).
     */
    @Override
    public void storeScore(boolean access, int score1, int score2, int score3) throws AccessNotPermittedException, ValueStoreException {
        if(!access){
            throw new AccessNotPermittedException("Store to the Database");
        }

        // Validate score ranges.
        if(score1<0 || score1>999 || score2<0 || score2>999 || score3<0 || score3>999){
            throw new ValueStoreException("Please insert scores between 0 and 999..\n");
        }

        if(this.concreteDataManager == null){ // Lazy initialization.
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        this.concreteDataManager.storeScore(access,score1,score2,score3); // Delegate to concrete manager.
    }
}