/*
 * Reciprocal Net project
 * 
 * MinZSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.db.SampleDataInfo;

/**
 * A {@code SearchConstraint} to limit search results to those samples whose 'z'
 * value is greater than or equal to the given value.
 */
public class MinZSC extends NumericSampleDataSC {

    /**
     * A constructor to fully initialize a {@code MaxZSC}.
     */
    public MinZSC(int minZValue) {
        super(SampleDataInfo.Z_FIELD, minZValue, GREATER_THAN_OR_EQUAL_TO);
    }
}
