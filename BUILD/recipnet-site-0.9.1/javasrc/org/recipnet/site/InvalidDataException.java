/*
 * Reciprocal Net Project
 * 
 * InvalidDataException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 * 21-Feb-2003: ekoperda added reason codes MISMATCHED_LAB_AND_PROVIDER,
 *              BLANK_STRINGS_FORBIDDEN, NEED_SAMPLE, WRONG_BASEDIRECTORY,
 *              WRONG_LABDIRECTORY, MISSING_ZIPENTRY_SITEGRANTISM,
 *              MISSING_ZIPENTRY_INTERSITEMESSAGE
 * 24-Mar-2003: ekoperda added reason code ILLEGAL_LOCALLABID
 * 02-Apr-2003: nsanghvi added reason code ILLEGAL_EXTENSIONPATH
 * 29-May-2003: ekoperda removed reason codes NEED_SAMPLE, WRONG_BASEDIRECTORY,
 *              WRONG_LABDIRECTORY, and WRONG_LOCALLABID (obsolete)
 * 17-Jul-2003: midurbin added reason code ILLEGAL_FILENAME
 * 11-May-2004: cwestnea removed BLANK_STRINGS_FORBIDDEN and added codes 
 *              0x2000 - 0x8000000
 * 06-Jan-2005: jobollin expanded the definition of the ILLEGAL_SPGP reason
 *              code
 * 10-Jun-2005: midurbin added INVALID_ACCESS_FOR_USER_TYPE reason code
 * 21-Oct-2005: midurbin added ILLEGAL_FILE_DESCRIPTION reason code
 * 01-Nov-2005: jobollin added MALFORMED_FORMULA reason code
 * 07-Dec-2005: ekoperda removed reason code MISSING_ZIPENTRY_INTERSITEMESSAGE
 *              and renumbered the others
 */

package org.recipnet.site;

/**
 * A <code>RecipnetException</code> subclass thrown when a request contains
 * invalid, inconsistent, or insufficient data
 */
public class InvalidDataException extends RecipnetException {
    
    /**
     * A reason code for use when the data are invalid because they specify
     * both a lab and a provider but should only specify one or the other
     */
    public static final int HAS_LAB_AND_PROVIDER = 1 << 0;
    
    /**
     * A reason code for use when the data are invalid because they don't
     * specify an associated lab
     */
    public static final int NEED_LAB = 1 << 1;
    
    /**
     * A reason code for use when the data are invalid because they don't
     * specify an associated provider
     */
    public static final int NEED_PROVIDER = 1 << 2;
    
    /**
     * A reason code for use when the data are invalid because they don't
     * specify either an associated lab or an associated provider
     */
    public static final int NEED_LAB_OR_PROVIDER = 1 << 3;
    
    /**
     * A reason code for use when the reason for the data invalidity is a
     * missing inactive date
     */
    public static final int NEED_INACTIVE_DATE = 1 << 4;

    /**
     * A reason code for use when the reason for the data invalidity is a
     * provider id that doesn't belong to a particular lab id.
     */
    public static final int MISMATCHED_LAB_AND_PROVIDER = 1 << 5;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * a .zip entry (with some particular name) expected to be found in a
     * msgpak file was not found.
     */
    public static final int MISSING_MSGPAK_ZIPENTRY = 1 << 6;

    /**
     * A reason code for use when the reason for the data invalidity is that 
     * the specified localLabId value (of a sample) contains illegal
     * characters.
     */
    public static final int ILLEGAL_LOCALLABID = 1 << 7;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the sample's specified labId refers to a lab record that is inactive.
     */
    public static final int INACTIVE_ASSOCIATED_LAB = 1 << 8;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the sample's specified providerId refers to a provider record that is
     * inactive.
     */
    public static final int INACTIVE_ASSOCIATED_PROVIDER = 1 << 9;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the repositoryDirectory object is not the child of the lab base
     * directory of if it contains illegal characters.
     */
    public static final int ILLEGAL_EXTENSIONPATH = 1 << 10;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified filename contains illegal characters.
     */
    public static final int ILLEGAL_FILENAME = 1 << 11;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified name (of a lab, provider, user, or site) contains illegal
     * characters.
     */
    public static final int ILLEGAL_NAME = 1 << 12;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified short name (of a lab or site) contains illegal characters.
     */
    public static final int ILLEGAL_SHORT_NAME = 1 << 13;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified fullname (of a user) contains illegal characters.
     */
    public static final int ILLEGAL_FULLNAME = 1 << 14;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified directory name (of a lab) contains illegal characters.
     */
    public static final int ILLEGAL_DIRECTORY_NAME = 1 << 15;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified url (of a lab) contains illegal characters.
     */
    public static final int ILLEGAL_URL = 1 << 16;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified copyright notice (of a lab) contains illegal characters.
     */
    public static final int ILLEGAL_COPYRIGHT_NOTICE = 1 << 17;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified contact (of a provider) contains illegal characters.
     */
    public static final int ILLEGAL_CONTACT = 1 << 18;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified base url (of a site) contains illegal characters.
     */
    public static final int ILLEGAL_BASE_URL = 1 << 19;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified repository url (of a site) contains illegal characters.
     */
    public static final int ILLEGAL_REPOSITORY_URL = 1 << 20;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified space group symbol contains illegal characters, does not
     * represent a valid spacegroup, or is otherwise incorrect or malformed.
     */
    public static final int ILLEGAL_SPGP = 1 << 21;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified color (of a sample) contains illegal characters.
     */
    public static final int ILLEGAL_COLOR = 1 << 22;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified summary (of a sample) contains illegal characters.
     */
    public static final int ILLEGAL_SUMMARY = 1 << 23;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified attribute (of a sample) contains illegal characters.
     */
    public static final int ILLEGAL_ATTRIBUTE = 1 << 24;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified annotation (of a sample) contains illegal characters.
     */
    public static final int ILLEGAL_ANNOTATION = 1 << 25;

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the specified comments (of a sample or provider) contain illegal 
     * characters.
     */
    public static final int ILLEGAL_COMMENTS = 1 << 26;

    /**
     * A reson code for use when the reason for the data invalidity is that the
     * specified globalAccessLevel is one that is non-sensical for the
     * specified user type (ie, one specific to a provider user applied to a
     * lab user).
     */
    public static final int INVALID_ACCESS_FOR_USER_TYPE = 1 << 27; 

    /**
     * A reason code for use when the reason for the data invalidity is that
     * the file description contains illegal characters or is set to an illegal
     * value.
     */
    public static final int ILLEGAL_FILE_DESCRIPTION = 1 << 28;
    
    /**
     * A reason code for use when the reason for the data invalidity is that
     * a chemical formula is unparseable or is not not correctly formatted for
     * its formula type.
     */
    public static final int MALFORMED_FORMULA = 1 << 29;

    /**
     * A catchall reason code, normally used only subclasses
     */
    public static final int OTHER = 1 << 30;
    
    /**
     * The object in which invalid data were detected
     */
    private Object invalidDataObject;
    
    /**
     * The reason code describing the data invalidity; a combination of the
     * specific reason code bitmasks defined in this class
     */
    private int reason;
    
    /**
     * Creates a new <code>InvalidDataException</code>
     */
    public InvalidDataException() {
        this(null, null, 0);
    }
    
    /**
     * Creates a new <code>InvalidDataException</code> with the specified
     * invalid data object
     *
     * @param obj the <code>Object</code> in which invalid data were detected
     * @param reason the reason code for the invalidity of the data
     */
    public InvalidDataException(Object obj, int reason) {
        this(null, obj, reason);
    }
    
    /**
     * Creates a new <code>InvalidDataException</code> with the specified
     * detail message and invalid data object
     *
     * @param message the detail message for this exception
     * @param obj the <code>Object</code> in which invalid data were detected
     * @param reason the reason code for the invalidity of the data
     */
    public InvalidDataException(String message, Object obj, int reason) {
        super(message);
        invalidDataObject = obj;
        this.reason = reason;
    }
    
    /**
     * Returns the object in which invalid data were detected
     *
     * @return the object in which invalid data were detected; may be
     * <code>null</code> if none was specified at construction time
     */
    public Object getInvalidDataObject() {
        return invalidDataObject;
    }
    
    /**
     * Returns the reason code describing the data invalidity
     *
     * @return the reason code describing the data invalidity; a combination of
     * the specific reason code bitmasks defined in this class
     */
    public int getReason() {
        return reason;
    }
    
}
