/*
 * Reciprocal Net Project
 * @(#)IncorrectResultException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 */
package org.recipnet.site;

/**
 * An <code>OperationFailedException</code> subclass that may
 * be thrown when the operation in question completed without exception but
 * produced incorrect results or returned an error code
 */
public class IncorrectResultException extends OperationFailedException {
    
    /**
     * Creates a new <code>IncorrectResultException</code>
     */
    public  IncorrectResultException() {
        this(null);
    }
    
    /**
     * Creates a new <code>IncorrectResultException</code> with the specified
     * detail message
     *
     * @param message the detail message for this exception
     */
    public  IncorrectResultException(String message) {
        super(message);
    }
    
}
