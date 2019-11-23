/**
 * 
 */
package de.rpgframework.music;

import java.util.Collection;

/**
 * Stores informations which songs (preferably identified by their 
 * MusicBrainz-ID) are suitable for which genres, moods ore scenes.
 * 
 * @author Stefan
 *
 */
public interface ClassificationDatabase {

	//--------------------------------------------------------------------
	/**
	 * Determine if the given track has an entry in the database
	 */
	public boolean isKnownTrack(Track track);
	
	public TrackClassification getTrackInfo(UniqueTrackID id);
	
	public Collection<Track> findTracks(Filter... filter);
	
}
