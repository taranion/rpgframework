/**
 * 
 */
package de.rpgframework.music;

import java.util.Collection;

import de.rpgframework.core.Genre;

/**
 * @author Stefan
 *
 */
public interface TrackClassification {

	public UniqueTrackID getTrack();
	
	public Collection<Genre> getGenres();
}
