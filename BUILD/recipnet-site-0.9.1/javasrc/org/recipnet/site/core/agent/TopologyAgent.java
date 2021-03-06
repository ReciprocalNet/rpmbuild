/*
 * Reciprocal Net project
 * 
 * TopologyAgent.java
 *
 * 18-Mar-2003: midurbin wrote first draft 
 * 28-Mar-2003: ekoperda fixed bug #833 in getSitesToPullFrom()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.core.util package
 *              to org.recipnet.site.core.agent; also changed package
 *              references to match source tree reorganization
 * 14-Dec-2005: ekoperda added security rules logic in a few new functions and
 *              modified class to use modern coding conventions
 * 26-May-2006: jobollin reformatted the source
 * 06-Jan-2008: ekoperda added tracking for offline sites
 * 26-Nov-2008: ekoperda clarified comments on notifySiteDeactivation()
 */

package org.recipnet.site.core.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * <p>
 * An agent of Site Manager that determines the topology of the Site Network.
 * It tracks the Site Network's status as sites are added and removed, and as
 * inter-site messages are pushed and pulled. It embodies the various topology
 * and security rules regarding the transmission of inter-site messages.
 * {@code SiteManager} and related classes are expected to delegate all
 * decisions regarding which ISM's should go where to this class. This class is
 * thread-safe.
 * </p><p>
 * The current implementation of this class is deliberately trivial compared to
 * the complexity of its interface.  The present Site Network topology
 * implemented by this class is a fully-connected mesh.  Thus, every public ISM
 * that is generated by one site is pushed to every other site.  Also, every
 * site periodically initiates pull requests to each of the other sites.
 * </p><p>
 * On the push side, a "shunned" sites mechanism tracks remote sites that are
 * offline.  Any failed push attempt to a remote site is sufficient to land
 * that remote site on the shunned sites list.  ISM's generated by the local
 * site are not pushed to shunned sites.  That remote site will remain on the
 * shunned sites list for <code>shunnedDuration</code> milliseconds or until
 * another thread reports a successful pull or push against the site.  This
 * mechanism increases the performance and decreases the user-perceived latency
 * of the ISM-pushing engine when there are many remote sites offline.  The 
 * shunned sites list does not play a role in pull operations because pull
 * operations are high-latency and resource-intensive by nature; there would be
 * little advantage to shunning sites there.
 * </p><p>
 * More complex and efficient topologies are envisioned in the future as the
 * Site Network grows.
 * </p>
 */
public class TopologyAgent {
    /**
     * Set at construction time.
     */
    private int localSiteId;
    private long shunnedDuration;
    private SiteManager siteManager;

    /**
     * A collection of {@code SiteInfo}'s representing all the sites presently
     * active in the site network. The collection does contain the local site.
     * This is populated by the constructor and is modified by
     * {@code notifySiteActivation()} and {@code notifySiteDeactivation()}.
     */
    private Collection<SiteInfo> activeSites;


    /**
     * The shunned set of sites.  Stored as a collection of 
     * <code>Integer</code>'s, where each is a site id.  We suggest that ISM's
     * not be pushed to shunned sites for performance reasons.  Shunned sites
     * will drop out of the set after a while, configurable at construction
     * time.
     */
    private AgingSet<Integer> shunnedSiteIds;

    /**
     * The only constructor.
     * 
     * @param sites an array of zero or more {@code SiteInfo} objects that
     *        describes the set of sites presently known to be part of the Site
     *        Network. This object's record of the same information may be
     *        manipulated later via calls to {@code notifySiteActivation()} and
     *        {@code notifySiteDeactivation()}.
     * @param localSiteId is the siteId of the local site
     * @param shunnedDuration the number of milliseconds for which a remote
     *        site should be shunned following a failed push attempt to it.
     */
    public TopologyAgent(SiteInfo sites[], int localSiteId, 
	    long shunnedDuration, SiteManager siteManager) {
        this.activeSites = new HashSet<SiteInfo>(Arrays.asList(sites));
        this.localSiteId = localSiteId;
	this.shunnedSiteIds = new AgingSet<Integer>(shunnedDuration);
	this.shunnedDuration = shunnedDuration;
	this.siteManager = siteManager;
    }

    /**
     * <p>
     * Whenever a caller decides to execute a periodic pull of inter-site
     * messages from other sites in the Site Network, it consults this
     * function.  This function tells the caller which remote sites it should
     * execute pulls against, and which third sites' inter-site messages it
     * should request from each. The returned information may vary over time
     * depending for example on the set of active sites, the Site Network's
     * current topology, and the time elapsed since the last pull. The caller
     * is expected to invoke {@code notifyPull} subsequently, for each
     * suggested site, after the pull was executed or attempted.
     * </p><p>
     * This function returns suggestions only, and nothing prevents a caller
     * from pulling from (or otherwise receiving ISM's from) other sites not
     * suggested by this function.
     * </p><p>
     * In the current implementation, this function instructs the caller to
     * pull ISM's from each remote site, requesting a replay of ISM's sent by
     * all sites.
     * </p>
     * 
     * @return a map of site id's to collections of site id's. Each entry in
     *         the map corresponds with one remote site that the caller should
     *         attempt to pull from. The site id's in the collection associated
     *         with that map entry describe which originating sites' ISM's
     *         should be requested during that pull. This map never contains an
     *         entry that represents the local site and never contains an entry
     *         for any site whose {@code SiteInfo.baseUrl} is null.
     */
    public synchronized Map<Integer, Collection<Integer>>
            suggestSitesToPullFrom() {
        Map<Integer, Collection<Integer>> pullMap
                = new HashMap<Integer, Collection<Integer>>();
        for (SiteInfo targetSite : this.activeSites) {
            if ((targetSite.id != localSiteId) 
                    && (targetSite.baseUrl != null)) {
                // Instruct the caller to contact targetSite and attempt to
                // pull ISM's.
                Collection<Integer> sitesToAskAbout = new ArrayList<Integer>();
                for (SiteInfo currentSite : this.activeSites) {
                    if (currentSite.id != localSiteId) {
                        // Instruct the caller that when he contacts targetSite
                        // he should ask for ISM's originally sent by
                        // currentSite.
                        sitesToAskAbout.add(currentSite.id);
                    }
                }
                pullMap.put(targetSite.id, sitesToAskAbout);
            }
        }
        return pullMap;
    }

    /**
     * For an inter-site message that presumably was received from a remote
     * site, determines whether the message file should be retained on disk for
     * topology reasons. The caller is free to retain the message file on disk
     * for other reasons, even if this function returns false.
     * 
     * @param ism an InterSiteMessage to be considered.
     * @return The current implementation always returns {@code false}.
     */
    public boolean shouldRetainIsm(@SuppressWarnings("unused")
    InterSiteMessage ism) {
        // currently unimplemented
        return false;
    }

    /**
     * <p>
     * For an ISM that was newly-generated by the local site and supplied to
     * this function by the caller, this function suggests a set of sites to
     * which the ISM should be pushed. The caller is expected to invoke
     * {@code notifyPush()} subsequently, for each suggested site, after the
     * ISM has been pushed or a push was attempted.
     * </p><p>
     * The sites returned by this function merely are suggestions, and nothing
     * prevents a caller from pushing ISM's to (or otherwise transmitting ISM's
     * to) other sites not suggested by this function, provided the caller
     * obeys other ISM security rules. See
     * {@code isIsmEligibleForTransmissionTo()} for the embodiment of those
     * security rules. Every suggested site returned by this function is
     * guaranteed to be an eligible recipient for {@code ism}; thus, callers
     * need not invoke {@code isIsmEligibleForTransmissionTo()} also, provided
     * they adhere to this function's suggestions.
     * </p><p>
     * In the current implementation, this function instructs the caller to
     * push the ISM to each remote site that is eligible to receive it,
     * according to security rules, except that sites presently on the shunned
     * list are excluded.
     * </p>
     * 
     * @return a collection of zero or more {@code SiteInfo}'s, where each
     *         identifies a remote site to which {@code ism} should be pushed.
     *         This collection never includes the local site nor any site whose
     *         {@code SiteInfo.baseUrl} is null.
     * @param ism the inter-site message that was newly-generated by the local
     *        site.
     */
    public synchronized Collection<SiteInfo> suggestSitesToPushTo(
            InterSiteMessage ism) {
        Collection<SiteInfo> pushToCollection = new ArrayList<SiteInfo>();
        for (SiteInfo targetSite : this.activeSites) {
            if (this.isIsmEligibleForTransmissionTo(ism, targetSite.id)
                    && targetSite.id != this.localSiteId
                    && targetSite.baseUrl != null
		    && !this.shunnedSiteIds.contains(targetSite.id)) {
                // targetSite is a suggested destination.
                pushToCollection.add(targetSite);
            }
        }
        return pushToCollection;
    }

    /**
     * <p>
     * For an ISM generated by the local site or another site at some point,
     * determines whether the ISM is eligible for transmission to a specified
     * remote site. This function's logic considers both the Site Network's
     * topology and its security rules. In contrast to
     * {@code suggestSitesToPushTo()}, this function imposes conditions upon
     * the caller: a caller seeking to transmit an ISM to a remote site must
     * consult this function beforehand and must not transmit the ISM to the
     * specified remote site if this function returns false.
     * </p><p>
     * This function might return true even in cases where transmitting the ISM
     * would be impossible, such as when {@code remoteSiteId} equals the local
     * site's id or when the specified remote site has a
     * {@code SiteInfo.baseUrl} field equal to null. Callers are responsible
     * for determining whether a particular ISM transmission authorized by this
     * function actually would be possible.
     * </p><p>
     * The current implementation contains logic that enforces ISM security
     * rules. No topology rules are considered in this particular
     * implementation.
     * </p>
     * 
     * @return true if {@code ism} is eligible for delivery to the specified
     *         remote site, or false if it is not. The caller must not transmit
     *         {@code ism} to the specified remote site if this function
     *         returns false.
     * @param ism the inter-site message that would be transmitted.
     * @param remoteSiteId identifies the remote site to which the ISM would be
     *        transmitted.
     */
    public boolean isIsmEligibleForTransmissionTo(InterSiteMessage ism,
            int remoteSiteId) {
        return this.isIsmEligibleForTransmissionTo(ism, remoteSiteId,
                InterSiteMessage.INVALID_SEQ_NUM,
                InterSiteMessage.INVALID_SEQ_NUM);
    }

    /**
     * Similar to the other version of {@code isIsmEligibleForTransmissionTo}
     * with an additional feature that may be convenient for some callers. The
     * function returns false in every case where the other version of
     * {@code isIsmEligibleForTransmissionTo} would return false, and
     * additionally returns false when the sequence number on {@code ism} does
     * not exceed caller-specified minimums. Thus it can help to facilitate
     * incremental transmissions of batches of ISM's.
     * 
     * @return true if {@code ism} is eligible for delivery to the specified
     *         remote site and its sequence number exceeds the caller-specified
     *         minimums, or false otherwise. The caller must not transmit
     *         {@code ism} to the specified remote site if this function
     *         returns false.
     * @param ism the inter-site message that would be transmitted.
     * @param remoteSiteId identifies the remote site to which the ISM would be
     *        transmitted.
     * @param seqNumLimitForPublic an optional sequence number limit. If this
     *        argument is specified, and {@code ism} is a public ISM (i.e. one
     *        addressed to {@code ALL_SITES}, then this function will not
     *        return true unless {@code ism.sourceSeqNum} is greater than this
     *        argument. This argument may be set to the special value
     *        {@code InterSiteMessage.INVALID_SEQ_NUM} if sequence number
     *        checking for public ISM's is not desired.
     * @param seqNumLimitForPrivate an optional sequence number limit. If this
     *        argument is specified, and {@code ism} is a private ISM (i.e. one
     *        addressed to one identified site), then this function will not
     *        return true unless {@code ism.sourceSeqNum} is greater than this
     *        argument. This argument may be set to the special value
     *        {@code InterSiteMessage.INVALID_SEQ_NUM} if sequence number
     *        checking for private ISM's is not desired.
     */
    public boolean isIsmEligibleForTransmissionTo(InterSiteMessage ism,
            int remoteSiteId, long seqNumLimitForPublic,
            long seqNumLimitForPrivate) {
        /*
         * Implementation note: this function could have been coded just as
	 * well as one 10-line inline conditional. Instead, the same logic is
         * expressed as a sequence of if's for clarity.
         */
        if ((ism.destSiteId != InterSiteMessage.ALL_SITES)
                && (ism.destSiteId != remoteSiteId)) {
            /*
             * Message unsuitable for delivery because it was not addressed to
             * remoteSiteId.
             */
            return false;
        }
        if ((ism.destSiteId == InterSiteMessage.ALL_SITES)
                && (seqNumLimitForPublic != InterSiteMessage.INVALID_SEQ_NUM)
                && (ism.sourceSeqNum <= seqNumLimitForPublic)) {
            /*
             * Message unsuitable for delivery because it is public but it does
             * not exceed the caller's minimum desired sequence number for
             * public messages.
             */
            return false;
        }
        if ((ism.destSiteId != InterSiteMessage.ALL_SITES)
                && (seqNumLimitForPrivate != InterSiteMessage.INVALID_SEQ_NUM)
                && (ism.sourceSeqNum <= seqNumLimitForPrivate)) {
            /*
             * Message unsuitable for delivery because it is private but it 
	     * does not exceed the caller's minimum desired sequence number for
             * private messages.
             */
            return false;
        }

        // The message is suitable for delivery.
        return true;
    }

    /**
     * <p>
     * Generates a SQL fragment that expresses logic equivalent to the four-
     * argument version of {@code isIsmEligibleForTransmissionTo()} above. This
     * might be useful when bulk lookups of many ISM's from a database must be
     * performed.
     * </p><p>
     * The particular database structure assumed is as follows. A column named
     * {@code destSiteId} contains integer values equivalent to those that
     * would be found in an {@code InterSiteMessage.destSiteId} field, with the
     * exception that a {@code NULL} value in the column corresponds to
     * destination of {@code ALL_SITES}. A column named {@code sourceSeqNum}
     * contains integer values equivalent to those that would be found in an
     * {@code InterSiteMessage.sourceSeqNum} field, with {@code NULL} values
     * being forbidden.
     * </p>
     * 
     * @return a string containing a SQL fragment suitable for inclusion in a
     *         {@code WHERE} clause of a {@code SELECT} statement. The fragment
     *         is enclosed within a pair of parentheses.
     * @param remoteSiteId identifies the remote site to which the ISM would be
     *        transmitted.
     * @param seqNumLimitForPublic an optional sequence number limit. If this
     *        argument is specified, and {@code ism} is a public ISM (i.e. one
     *        addressed to {@code ALL_SITES}, then this function will not
     *        return true unless {@code ism.sourceSeqNum} is greater than this
     *        argument. This argument may be set to the special value
     *        {@code InterSiteMessage.INVALID_SEQ_NUM} if sequence number
     *        checking for public ISM's is not desired.
     * @param seqNumLimitForPrivate an optional sequence number limit. If this
     *        argument is specified, an {@code ism} is a private ISM (i.e. one
     *        addressed to one identified site), then this function will not
     *        return true unless {@code ism.sourceSeqNum} is greater than this
     *        argument. This argument may be set to the special value
     *        {@code InterSiteMessage.INVALID_SEQ_NUM} if sequence number
     *        checking for private ISM's is not desired.
     */
    public String sqlForIsIsmEligibleForTransmissionTo(int remoteSiteId,
            long seqNumLimitForPublic, long seqNumLimitForPrivate) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("((destSiteId IS NULL");
        if (seqNumLimitForPublic != InterSiteMessage.INVALID_SEQ_NUM) {
            sql.append(" AND sourceSeqNum > ");
            sql.append(seqNumLimitForPublic);
        }
        sql.append(") OR (destSiteId=");
        sql.append(remoteSiteId);
        if (seqNumLimitForPrivate != InterSiteMessage.INVALID_SEQ_NUM) {
            sql.append(" AND sourceSeqNum > ");
            sql.append(seqNumLimitForPrivate);
        }
        sql.append("))");
        
        return sql.toString();
    }

    /**
     * Must be called by Site Manager's worker thread whenever it receives a
     * {@code SiteActivationISM}.
     * 
     * @param site the SiteInfo object describing the new site.
     */
    public synchronized void notifySiteActivation(SiteInfo site) {
        this.activeSites.add(site);
    }

    /**
     * Must be called by Site Manager's worker thread whenever it receives a
     * {@code SiteUpdateISM}.
     * 
     * @param siteId the siteId for the site that is deactivated.
     */
    public synchronized void notifySiteUpdate(SiteInfo site) {
        this.activeSites.remove(new SiteInfo(site.id));
	this.activeSites.add(site);

	// Maybe we've received new contact information for this newly-updated
	// site.  Remove it from the shunned set.
	this.shunnedSiteIds.remove(site.id);
    }

    /**
     * Must be called by Site Manager's worker thread whenever a remote site's
     * record is fully deactivated.  This may be immediately in response to
     * receipt of a {@code SiteDeactivationISM}, or some time later after all
     * the site's ISM's have been processed.
     *
     * This method has no effect if the siteId was not already known.
     */
    public synchronized void notifySiteDeactivation(int siteId) {
        this.activeSites.remove(new SiteInfo(siteId));
	this.shunnedSiteIds.remove(siteId);
    }

    /**
     * This function should be called whenever a pull is attempted from another
     * site.
     * 
     * @param siteId corresponds to the site being pulled from.
     * @param succeeded true if pull was successful, otherwise false.
     * @param ismCount number of ISM's present in the pull reply; ignored
     *        unless succeeded is true.
     */
    public void notifyPull(int siteId, boolean succeeded,
            @SuppressWarnings("unused") int ismCount) {
	if (succeeded) {
	    // Since we've successfully pulled ISM's from the remote site, it's
	    // probably still online.  Remove it from the shunned set.
	    this.shunnedSiteIds.remove(siteId);
	}
    }

    /**
     * This function should be called whenever a push is attempted to another
     * site.
     * 
     * @param siteId corresponds to the site being pushed to.
     * @param succeeded true if push was successful, otherwise false.
     */
    public void notifyPush(int siteId, boolean succeeded) {
	if (succeeded) {
	    // Since we successfully pushed ISM's to the remote site, it's
	    // probably still online.  Remove it from the shunned set.
	    this.shunnedSiteIds.remove(siteId);
	} else {
	    // Our push failed; the site is probably offline.  Add it to the
	    // shunned set.
	    this.shunnedSiteIds.add(siteId);

	    // Log a message.
            this.siteManager.recordLogRecord(
                    LogRecordGenerator.topologySiteShunned(siteId,  
                    this.shunnedDuration));
	}
    }

    /**
     * A quick utility class inspired by the Set interface. Elements age out of
     * the set, with a maximum lifetime (in milliseconds) specified at
     * construction time.  Elements automatically disappear from the set once
     * they reach the maxAge age limit.
     */
    private static class AgingSet<T> {
	private Map<T, Long> map;
	private long maxAge;

	public AgingSet(long maxAge) {
	    this.map = new TreeMap<T, Long>();
	    this.maxAge = maxAge;
	}

	public void add(T value) {
	    // Do the put.
	    this.map.put(value, System.currentTimeMillis());
	}

	public boolean contains(T value) {
	    Long timeCreated = this.map.get(value);
	    if (timeCreated != null) {
		if (System.currentTimeMillis() - timeCreated < this.maxAge) {
		    return true;
		}
		this.map.remove(value);
	    }
	    return false;   
	}

	public boolean remove(T value) {
	    boolean rc = this.contains(value);
	    if (rc) {
		this.map.remove(value);
	    }
	    return rc;
	}
    }
}
