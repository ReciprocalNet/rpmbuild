/*
 * Reciprocal Net Project
 * 
 * ResourcesExhaustedException.java
 *
 * 25-Feb-2003: jobollin autogenerated skeleton source from the UML model as
 *              part of task #756
 * 26-Feb-2003: jobollin completed the source as part of task #756
 * 24-Mar-2003: jobollin modified the constructors as part of task #808
 * 27-Sep-2005: midurbin added SAMPLE_AUTO_LOCAL_LAB_IDS_EXPIRED
 * 07-Jul-2006: jobollin reformatted the source
 */

package org.recipnet.site.core;

/**
 * A {@code ResourceException} subclass for use when not enough of a required
 * resource is available to complete the requested operation. Such a condition
 * may be temporary, in that the system is likely to recover without
 * intervention, or permanent, in that administrative action is required to
 * correct the situation. Resources that may suitable for monitoring subject to
 * this exception include, but are not limited to, hard disk space, unused
 * sample ids, and simultaneous user slots.
 */
public class ResourcesExhaustedException extends ResourceException {

    /**
     * A reason code indicating that insufficient disk space is available to
     * perform the requested operation
     */
    public static final int DISK_SPACE_EXHAUSTED = 100;

    /**
     * A reason code indicating that there are no available sample IDs
     */
    public static final int SAMPLE_IDS_EXHAUSTED = 200;

    /**
     * A reason code indicating that the maximum number of allowed users has
     * been reached
     */
    public static final int USER_LIMIT_REACHED = 300;

    /**
     * A reason code indicating that every automatically generated id for a
     * particular lab has been used
     */
    public static final int SAMPLE_AUTO_LOCAL_LAB_IDS_EXHAUSTED = 400;

    /**
     * The reason code indicating what resources are exhausted; the value should
     * be one of the reason codes defined for this exception
     */
    private final int reason;

    /**
     * Creates a new {@code ResourcesExhaustedException}
     */
    public ResourcesExhaustedException() {
        this(0);
    }

    /**
     * Creates a new {@code ResourcesExhaustedException} with the specified
     * reason code
     * 
     * @param reason the reason code for this exception
     */
    public ResourcesExhaustedException(int reason) {
        super();
        this.reason = reason;
    }

    /**
     * Creates a new {@code ResourcesExhaustedException} with the specified
     * detail message, reason code, and cause
     * 
     * @param message a {@code String} containing the detail message for this
     *        exception (may be {@code null})
     * @param reason the reason code for this exception
     * @param cause the {@code Throwable} cause for this exception (may be
     *        {@code null})
     */
    public ResourcesExhaustedException(String message, int reason,
            Throwable cause) {
        super(message);
        initCause(cause);
        this.reason = reason;
    }

    /**
     * Returns the reason code for this exception
     * 
     * @return the reason code indicating what resources are exhausted; the
     *         value should be one of the reason codes defined for this
     *         exception
     */
    public int getReason() {
        return reason;
    }

}
