/*
 * Reciprocal Net Project
 * @(#)DuplicateDataException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 * 21-Feb-2003: ekoperda added field code LAB_AND_LOCALLABID
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 30-Jun-2005: midurbin added getDuplicateField()
 */
package org.recipnet.site.core;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.db.ContainerObject;

/**
 * An <code>InvalidDataException</code> subclass for use when an attempt is
 * made to violate a data uniqueness constraint
 */
public class DuplicateDataException extends InvalidDataException {
    
    /**
     * A field code for a username; currently the only container object with a
     * username is a UserInfo
     */
    public static final int USERNAME = 0x1;
    
    /**
     * A field code for a labId/localLabId combination; currently the only
     * container object with a labId and localLabId is a SampleInfo
     */
    public static final int LAB_AND_LOCALLABID = 0x2;

    /**
     * A code for the field containing the duplicate data
     */
    private int duplicateField;
    
    /**
     * Creates a new <code>DuplicateDataException</code>
     */
    public  DuplicateDataException() {
        this(null, null, 0);
    }
    
    /**
     * Creates a new <code>DuplicateDataException</code> with the specified
     * object containing the duplicate data and field code specifying which
     * datum is a duplicate
     *
     * @param obj the <code>ContainerObject</code> having duplicate data
     * @param field the code for the field containing a duplicate datum
     */
    public  DuplicateDataException(ContainerObject obj, int field) {
        this(null, obj, field);
    }
    
    /**
     * Creates a new <code>DuplicateDataException</code> having the specified
     * detail message and invalid object
     *
     * @param message the detail message for this exception
     * @param obj the <code>ContainerObject</code> holding the duplicate data
     * @param field the code for the field containing a duplicate datum
     */
    public  DuplicateDataException(String message, ContainerObject obj,
            int field) {
	super(message, obj, InvalidDataException.OTHER);
	duplicateField = field;
    }

    /**
     * Gets the duplicate field identifier for this exception.
     * @return one or more of the field codes logically ORed together
     *     representing the fields whose values were duplicates of existing
     *     values and therefore are invalid
     */
    public int getDuplicateField() {
        return this.duplicateField;
    }
    
}
