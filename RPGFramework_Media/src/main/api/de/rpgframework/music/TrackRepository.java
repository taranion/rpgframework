package de.rpgframework.music;

public interface TrackRepository {

	public void initiateScan();
	
	public Track getSong(UniqueTrackID mbTrackID);
	
}
