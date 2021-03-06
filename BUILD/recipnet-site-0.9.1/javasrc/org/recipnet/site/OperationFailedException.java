/*
 * Reciprocal Net Project
 * 
 * OperationFailedException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 * 24-Mar-2003: jobollin modified the constructors as part of task #808
 * 07-Jul-2006: jobollin reformatted the code
 */

package org.recipnet.site;

/**
 * A generic {@code RecipnetException} subclass used to indicate that some
 * operation failed unexpectedly; often used to wrap lower-level exceptions.
 * Where there is no external exception to wrap, one of this class' subclasses
 * should generally be used
 */
public class OperationFailedException extends RecipnetException {

    /**
     * Creates a new {@code OperationFailedException}
     */
    public OperationFailedException() {
        super();
    }

    /**
     * Creates a new {@code OperationFailedException} with the specified detail
     * message
     * 
     * @param message the detail message for this exception
     */
    public OperationFailedException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code OperationFailedException} with the specified cause
     * 
     * @param cause the {@code Throwable} cause of this exception
     */
    public OperationFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new {@code OperationFailedException} with the specified detail
     * message and cause
     * 
     * @param message the detail message for this exception
     * @param cause the {@code Throwable} cause of this exception
     */
    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
