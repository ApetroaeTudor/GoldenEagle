package PaooGame.CustomExceptions;

/**
 * @class DataBufferNotReadyException
 * @brief Custom exception to indicate that a required data buffer is not yet initialized or populated.
 *
 * This exception is thrown when an operation attempts to access or use a data buffer
 * (for example within a DataManager) that has not been properly prepared, typically meaning
 * data has not been loaded into it from a source like a database.
 * It extends the base `Exception` class.
 */
public class DataBufferNotReadyException extends Exception {
    /**
     * @brief Constructs a DataBufferNotReadyException with a default message.
     * The message advises that the data buffer is in an invalid state and suggests
     * that data should be stored (loaded) into it first.
     */
    public DataBufferNotReadyException() {
        super("The concrete DataManager buffer is in an invalid state.\nPlease first store some data from the database..");
    }
}