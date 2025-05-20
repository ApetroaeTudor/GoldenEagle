package PaooGame.DatabaseManaging;

import PaooGame.CustomExceptions.AccessNotPermittedException;
import PaooGame.CustomExceptions.DataBufferNotReadyException;
import PaooGame.CustomExceptions.ValueStoreException;

/**
 * @interface DataManager
 * @brief Defines the contract for managing game data, including loading, storing, and resetting data.
 *
 * This interface outlines the essential operations for any class that aims to handle
 * game state saving. It supports operations on individual key-value pairs as well
 * as bulk operations on a data buffer (e.g., loading from or storing to a database).
 * It also includes methods for managing game scores.
 *
 * Implementations of this interface are expected to handle potential issues like
 * access restrictions, uninitialized data buffers, or storage errors by throwing
 * custom exceptions.
 */
public interface DataManager {
    /**
     * @brief Loads an integer value associated with a given key.
     * @param key The unique identifier for the data to be loaded.
     * @param access A boolean flag, potentially for future access control logic (its specific use is implementation-dependent).
     * @return The integer value associated with the key.
     * @throws AccessNotPermittedException If the operation is not permitted.
     * @throws ValueStoreException If there's an error retrieving the value.
     * @throws DataBufferNotReadyException If the underlying data source or buffer is not ready.
     */
    public int load(String key, boolean access) throws AccessNotPermittedException, ValueStoreException, DataBufferNotReadyException;

    /**
     * @brief Stores an integer value associated with a given key.
     * @param key The unique identifier for the data to be stored.
     * @param value The integer value to store.
     * @param access A boolean flag, potentially for future access control logic.
     * @throws AccessNotPermittedException If the operation is not permitted.
     * @throws ValueStoreException If there's an error storing the value.
     */
    public void store(String key, int value, boolean access ) throws AccessNotPermittedException, ValueStoreException;

    /**
     * @brief Resets the internal data buffer to a default or initial state.
     * @param access A boolean flag, potentially for future access control logic.
     * @throws AccessNotPermittedException If the operation is not permitted.
     */
    public void resetBuffer(boolean access)throws AccessNotPermittedException;

    /**
     * @brief Loads data from a persistent source into the internal data buffer.
     * @param access A boolean flag, potentially for future access control logic.
     * @throws AccessNotPermittedException If the operation is not permitted.
     */
    public void loadBuffer(boolean access) throws AccessNotPermittedException;

    /**
     * @brief Stores the current state of the internal data buffer to a persistent source.
     * @param access A boolean flag, potentially for future access control logic.
     * @throws AccessNotPermittedException If the operation is not permitted.
     * @throws ValueStoreException If there's an error during the storage process.
     */
    public void storeBuffer(boolean access) throws AccessNotPermittedException, ValueStoreException;

    /**
     * @brief Loads the top game scores.
     * @param access A boolean flag, potentially for future access control logic.
     * @return An array of integers representing the loaded scores (e.g., top 3).
     * @throws AccessNotPermittedException If the operation is not permitted.
     */
    public int[] loadScore(boolean access) throws AccessNotPermittedException;

    /**
     * @brief Stores game scores.
     * @param access A boolean flag, potentially for future access control logic.
     * @param score1 The first score to store.
     * @param score2 The second score to store.
     * @param score3 The third score to store.
     * @throws AccessNotPermittedException If the operation is not permitted.
     * @throws ValueStoreException If there's an error during the storage process.
     */
    public void storeScore(boolean access, int score1, int score2, int score3) throws AccessNotPermittedException, ValueStoreException;
}