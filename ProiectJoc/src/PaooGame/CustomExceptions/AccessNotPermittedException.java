package PaooGame.CustomExceptions;

/**
 * @class AccessNotPermittedException
 * @brief Custom exception to indicate that an attempted operation is not allowed.
 *
 * This exception is thrown when a part of the game logic attempts to perform
 * an action or access a resource for which it does not have the necessary
 * permissions or when an operation is restricted under certain conditions.
 * It extends the base `Exception` class.
 */
public class AccessNotPermittedException extends Exception {
    /**
     * @brief Constructs an AccessNotPermittedException with a message indicating the restricted target.
     * @param target A string describing the resource or operation to which access was denied.
     */
    public AccessNotPermittedException(String target) {
        super("Sorry, you do not have access to: " + target);
    }
}