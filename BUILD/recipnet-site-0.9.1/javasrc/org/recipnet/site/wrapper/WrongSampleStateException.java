/*
 * Reciprocal Net Project
 * @(#)WrongSampleStateException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 08-Aug-2004: cwestnea modified constructor to use SampleWorkflowBL
 */

package org.recipnet.site.wrapper;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;

/**
 * An <code>OperationNotPermittedException</code> subclass for the case in
 * which a workflow action was requested on a sample that was not in a
 * suitable state
 */
public class WrongSampleStateException extends OperationNotPermittedException {
    
    /**
     * The sample state code at the time of the attempted action
     */
    private int sampleState;
    
    /**
     * The action code of the attempted action
     */
    private int requestedAction;
    
    /**
     * Creates a new <code>WrongSampleStateException</code>
     */
    public  WrongSampleStateException() {
        this(null, SampleWorkflowBL.INVALID_STATUS, SampleWorkflowBL.INVALID_ACTION);
    }
    
    /**
     * Creates a new <code>WrongSampleStateException</code>, specifying the
     * sample's state at the time of the request and the requested action
     *
     * @param state the sample's state code at the time of the attempted action
     * @param action the code for the attempted action
     */
    public  WrongSampleStateException(int state, int action) {
        this(null, state, action);
    }
    
    /**
     * Creates a new <code>WrongSampleStateException</code> with the specified
     * detail message, sample state code, and action code
     *
     * @param message the detail message for this exception
     * @param state the sample state code for the sample at the time of the
     * attempted action
     * @param action the action code for the attempted action
     */
    public  WrongSampleStateException(String message, int state, int action) {
        super(message);
        sampleState = state;
        requestedAction = action;
    }
    
    /**
     * Returns the sample state code at the time of the attempted action that
     * triggered this exception
     *
     * @return the sample state code
     */
    public int getSampleState() {
        return sampleState;
    }
    
    /**
     * Returns the action code of the attempted action that triggered this
     * exception
     *
     * @return the action code
     */
    public int getRequestedAction() {
        return requestedAction;
    }
    
}
