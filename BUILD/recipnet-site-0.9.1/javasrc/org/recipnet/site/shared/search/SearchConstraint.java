/*
 * Reciprocal Net project
 * 
 * SearchConstraint.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added getMatches()
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.recipnet.site.shared.db.SampleInfo;

/**
 * <p>
 * A {@code SearchConstraint} limits the results of a sample search in some way.
 * The mechanisms by which it accomplishes this is by generating a fragment of
 * SQL that when included in the SQL WHERE clause indicates a condition that
 * must be met for inclusion.
 * </p><p>
 * To generate complex searches, many {@code SearchConstraint} implementors may
 * be combined to form a tree structure. {@code SearchConstraint} objects that
 * have children must define the relationship of those children and properly
 * construct the appropriate WHERE clause fragment to reflect that relationship.
 * </p><p>
 * {@code SearchConstraint} objects and their subclasses can be expected to be
 * immutable from the time they are initialized.
 * </p><p>
 * This abstract base class may be extended to implement various types of
 * {@code SearchConstraints}. Minimally {@code getWhereClauseFragment()} will
 * need to be implemented.
 * </p>
 */
public abstract class SearchConstraint implements Serializable {

    /**
     * Generates a a {@code String} representing a condition that may be used in
     * the WHERE clause of an SQL query. The supplied {@code DbTableManager} may
     * update its internal state in response to invocations made on it by the
     * implementation of this method. The 'parameters' {@code Collection}
     * supplied to this method will have elements appended that correspond to
     * the {@code String} returned. This method MUST be overridden by
     * subclasses. Subclasses that have children should incorperate their
     * childrens' WHERE clause fragment into their own.
     * 
     * @param tableTracker the table tracker that will generate and distribute
     *        table aliases for various constraints to use in their SQL fragment
     * @param parameters a {@code Collection} of parameters that will be
     *        provided to a {@code PreparedStatement} that will be created using
     *        the SQL generated by this method. This collection may only contain
     *        object types that are supported by
     *        {@code PreparedStatement.setObject()}. Subclasses must add
     *        parameters to this collection in the order they wish them to be
     *        substituted into their SQL fragment.
     * @param scei a {@code SearchConstraintExtraInfo} that may contain useful
     *        information needed by subclasses to generate SQL
     * @return a {@code String} that is an SQL fragment representing a condition
     *         that may be used as (or added to) the WHERE clause. This method
     *         should never return null, but may retrun an empty {@code String}
     *         to signify that this particular {@code SearchConstraint} has no
     *         effect on the search.
     * @throws UnsupportedOperationException if subclasses failed to override
     *         this method. Subclasses may not throw this exception.
     */
    public abstract String getWhereClauseFragment(
            SearchTableTracker tableTracker, List<Object> parameters, 
            SearchConstraintExtraInfo scei);

    /**
     * To allow full traversal and management of a tree of
     * {@code SearchConstraint} objects it must be possible to get references to
     * child nodes, so this method returns an unmodifiable {@code Collection} of
     * them. This method may return null if a particular node has no children.
     * This implementation for this abstract class always returns null. If
     * subclasses have children, they must override this method.
     * 
     * @return an unmodifiable {@code Collection} containing all child
     *         {@code SearchConstraint} objects; may be empty
     */
    public Collection<? extends SearchConstraint> getChildren() {
        return Collections.<SearchConstraint>emptySet();
    }

    /**
     * <p>
     * Accumulates information about whether and how the sample matches this
     * {@code SearchConstraint}.
     * </p><p>
     * A SearchConstraint it considered to 'match' the sample if the criteria
     * encapsualted in it or its children are found to be present in the sample.
     * A SearchConstraint is considered to 'mismatch' if the criteria are NOT
     * (collectively) found to be present in the sample.
     * </p><p>
     * The 'matches' parameter should be an initialized collection that will be
     * populated with {@code FieldMatchInfo} objects expressing some (or all) of
     * the specific fields on the sample that matched search criteria if, in
     * fact, the sample matched. If it is known by the caller that the sample
     * did NOT match the search criteria (due to its absense in the search
     * results returned by core), null may be provided as this parameter.
     * Providing null may allow this method to return a value (incomplete though
     * still possibly useful) even when child {@code SearchConstraint} objects
     * do not override this method.
     * </p><p>
     * The 'mismatches' parameter should be an initialized collection that will
     * be populated with the {@code FieldMatchInfo} objects expressing some (or
     * all) fields that specifically prevented the sample from matching the
     * search criteria if, in fact, the sample did not match. This collection
     * will only be populated in cases where specific field values are explicity
     * forbidden by the {@code SearchConstraint}. If it is known by the caller
     * that the sample DID match the search criteria (due to its presense in the
     * search results returned by core), null may be provided as this parameter.
     * Providing null may allow this method to return a value (incomplete though
     * still possibly useful) even when child {@code SearchConstraint} objects
     * do not implement this method.
     * </p><p>
     * Only one of either 'matches' or 'mismatches' will be populated by any
     * particular implementation of this method. If the sample matches this
     * {@code SearchConstraint} (as determine either by a full duplication of
     * core's searching logic or by a hint given by the caller), 'matches' might
     * be populated by this method. Otherwise, 'mismatches' might be populated.
     * </p><p>
     * While it's true that this method could be invoked for every sample on a a
     * {@code SearchConstraint} to mimic the search behavior of core, it is not
     * advised to perform searches in this way. This method generates useful
     * information about search matches and is ideally suited for use in
     * display-side logic for a small number of samples already known to match
     * the search criteria.
     * </p><p>
     * The base class implementation returns true if the parameters indicate
     * that the sample matches this constraint and false if the parameters
     * indicate that the sample does not match this constraint. Otherwise a
     * {@code UnsupportedOperationException} is thrown. This minimal
     * implementation will allow {@code SearchConstraint} trees to return valid
     * values even when very few contained {@code SearchConstraint} objects
     * fully support this method.
     * </p>
     * 
     * @param sample a {@code SampleInfo} to be considered
     * @param matches a collection that will be populated to contain specific
     *        information about field matches (if it is determined that the
     *        sample matches the search criteria) or null to indicate that it is
     *        known that the sample does not match this {@code SearchConstraint}
     *        (imporoves performance)
     * @param mismatches a collection that will be populated to contain specific
     *        information about fields that prevent the sample from matching the
     *        search criteria or null to indicate that it is known that the
     *        sample matches this {@code SearchConstraint} (imporoves
     *        performance)
     * @return a boolean indicating whether the sample met the search criteria
     *         described by this {@code SearchConstraint}.
     */
    public boolean getMatches(
            @SuppressWarnings("unused") SampleInfo sample,
            Collection<FieldMatchInfo> matches,
            Collection<FieldMatchInfo> mismatches) {
        if ((matches != null) && (mismatches == null)) {
            // it was a given that the sample matches this constraint
            return true;
        } else if ((matches == null) && (mismatches != null)) {
            // it was a given that the sample does not match this constraint
            return false;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
