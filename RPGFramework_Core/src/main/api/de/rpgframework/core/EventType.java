/**
 *
 */
package de.rpgframework.core;

/**
 * @author prelle
 *
 */
public enum EventType {

	// From SessionService
	// 0 = Player
	PLAYER_ADDED,
	PLAYER_REMOVED,
	PLAYER_CHANGED,
	ADVENTURE_ADDED,
	ADVENTURE_REMOVED,
	ADVENTURE_CHANGED,
	ADVENTURE_STARTED,
	ADVENTURE_STOPPED,
	// 0 = Group
	GROUP_ADDED,
	GROUP_REMOVED,
	GROUP_CHANGED,
	GROUP_SELECTED,

	/**
	 * 0 = Genre[]
	 */
	GENRE_CHANGED,
	RULESYSTEM_CHANGED,
	/* 0=SessionContext */
	SESSION_STARTED,
	SESSION_STOPPED,

	// From DeviceService
	SESSIONSCREEN_APPEARED,
	SESSIONSCREEN_DISAPPEARED,

	// From SocialNetwork
	/* 0=Friend */
	SOCIAL_FRIEND_FOUND,
	/* 0=Friend */
	SOCIAL_FRIEND_UPDATED,

	// From CharacterProvider
	CHARACTER_SERVICE_CONNECT,
	/* 0=Name  1=Local Path */
	CHARACTER_MODIFY_LOCAL,
	CHARACTER_MODIFY_REMOTE,
	CHARACTER_NEW_LOCAL,
	CHARACTER_NEW_REMOTE,
	/* 0=Number of changed files. Only thrown when there were changes */
	CHARACTER_SYNC_COMPLETE,

	/*
	 * Device management
	 */
	// 0 = Device, 1 = RPGToolDeviceType
	DEVICE_APPEARED,
	// 0 = Device, 1 = RPGToolDeviceType
	DEVICE_DISAPPEARED,

	/*
	 * Music management
	 */
	// 0 = FileSystem, 1 = Percent (Double)
	MUSIC_SCAN_PROGRESS,	// 0 = FileSystem, 1 = Percent (Double)
	// 0 = FileSystem, 1 = List<ScanResult>
	MUSIC_SCAN_COMPLETE,

	/*
	 * Media management
	 */
	// 0=Library, 1=Media
	MEDIA_ADDED,
	// 0=Library, 1=Media
	MEDIA_UPDATED,
	// 0=Library, 1=Media
	MEDIA_REMOVED,

	// 0 = WebServer
	WEBSERVER_STARTED,
	
	/*
	 * Backgroundtask management
	 */
	BACKGROUNDTASK_STARTED,
	BACKGROUNDTASK_STOPPED,
	
	/*
	 * Enhanced Adventure
	 */
	// No parameter
	ADVENTUREDISPLAY_LOADED,
	// A different section from the adventure has been selected, 0=Structure element
	ADVENTUREDISPLAY_STRUCTURE_ELEMENT_CHANGED,
}
