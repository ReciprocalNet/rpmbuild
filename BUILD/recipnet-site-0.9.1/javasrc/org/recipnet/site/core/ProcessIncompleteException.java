/*
 * Reciprocal Net Project
 * @(#)ProcessIncompleteException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 * 24-Mar-2003: jobollin modified the constructors as part of task #808
 * 03-Jul-2003: ekoperda made class serializable and added writeObject() to fix
 *              bug #963
 */
package org.recipnet.site.core;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.recipnet.site.OperationFailedException;

/**
 * An <code>OperationFailedException</code> subclass that may be thrown
 * when a thread catches an <code>InterruptedException</code> while waiting for
 * an external process to complete; contains a reference to the relevant
 * <code>Process</code> object. This exception is serializable, but the
 * <code>process</code> field is not persisted across serialization because 
 * <code>Process</code> objects are not serializable.
 */
public class ProcessIncompleteException extends OperationFailedException 
        implements Serializable {
    
    /**
     * If not <code>null</code>, the <code>Process</code> object representing
     * the process that had not completed.  This value is always null if this
     * object has been deserialized.
     */
    private Process process;
    
    /**
     * Creates a new <code>ProcessIncompleteException</code> with the specified
     * process and cause
     *
     * @param proc the </code>Process<code> that had not completed
     * @param cause the <code>Throwable</code> cause for this exception;
     * normally an <code>InterruptedException</code>
     */
    public  ProcessIncompleteException(Process proc, Throwable cause) {
        this(null, proc, cause);
    }
    
    /**
     * Creates a new <code>ProcessIncompleteException</code> with the specified
     * detail message, process, and cause
     *
     * @param message the detail message for this exception
     * @param proc the </code>Process<code> that had not completed
     * @param cause the <code>Throwable</code> cause for this exception;
     * normally an <code>InterruptedException</code>
     */
    public  ProcessIncompleteException(String message, Process proc,
            Throwable cause) {
	super(message);
	    // TODO: this isn't really right; a null cause is still a cause:
        if (cause != null) {
            initCause(cause);
        }
	process = proc;
    }
    
    /**
     * @return the <code>Process</code> representing the process that had not
     *     completed.  This value is always null if this object has been
     *     deserialized.
     */
    public Process getProcess() {
        return process;
    }
 
    /** Custom serialization handler */
    private void writeObject(ObjectOutputStream out) throws IOException {
	// Process objects are not serializable.  Hide the process value
	// temporarily to keep the serialization engine from choking.
	Process tempProcess = this.process;
	this.process = null;

	// Invoke the default serialization handler.
	out.defaultWriteObject();

	// Reset the process value.
	this.process = tempProcess;
    }
}
