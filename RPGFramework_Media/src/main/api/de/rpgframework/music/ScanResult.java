/**
 * 
 */
package de.rpgframework.music;

/**
 * @author prelle
 *
 */
public interface ScanResult {

	public enum State {
		/**
		 * No ID3 tag was found in the original file
		 */
		MISSING_TAG,
		/**
		 * The track contained an ID3 tag, but was missing a MusicBrainz ID.
		 */
		MISSING_MUSICBRAINZ_ID,
		/**
		 * The file was correctly tagged but there don't exist any 
		 * classification data for the song.
		 */
		MISSING_CLASSIFICATION,
		/**
		 * The file was correctly tagged and is already known in the
		 * classification database.
		 */
		GOOD
	}
	
	public State getState();
	
	public void setState(State newState);
	
	public Track getTrack();
	
}
