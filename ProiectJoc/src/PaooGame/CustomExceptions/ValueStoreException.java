package PaooGame.CustomExceptions;

/**
 * @class ValueStoreException
 * @brief Custom exception to indicate an error during a value storage or retrieval operation.
 *
 * This exception is thrown when there's a problem processing an operation related to
 * storing or accessing a value, often in the context of a data buffer or data management system.
 * It extends the base `Exception` class.
 */
public class ValueStoreException extends Exception {
    /**
     * @brief Constructs a ValueStoreException with a message indicating the cause of the error.
     * @param cause A string describing the specific reason or context of the storage/retrieval failure.
     *              The message will be prepended to a generic "Couldn't process buffer operation.." message.
     */
    public ValueStoreException(String cause) {
        super(cause + "Couldn't process buffer operation..\n");
    }
}