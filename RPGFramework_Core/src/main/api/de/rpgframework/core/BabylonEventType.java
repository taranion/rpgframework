package de.rpgframework.core;

public enum BabylonEventType {

	/*
	 * The internal social networks connection state changed
	 * 0=SocialNetwork 1=ConnectionState
	 */
	SOCIAL_NETWORK_STATE_CHANGED,

	/**
	 * A message to display to the user
	 * 0=Integer:MessageType (0=INFO, 1=WARN, 2=ERROR) 1=String:Message
	 */
	UI_MESSAGE,


	/*
	 * 0=CharacterHandle
	 */
	CHAR_ADDED,

	/*
	 * 0=CharacterHandle, 1=Attachment_Added, 2=Attachment_Modified, 3=Attachment_Removed
	 */
	CHAR_MODIFIED,

	/*
	 * 0=CharacterHandle
	 */
	CHAR_REMOVED,

	/*
	 * 0=CharacterHandle
	 */
	CHAR_RENAMED,

	/*
	 * 0=ConfigOption
	 */
	CONFIG_OPTION_CHANGED,

	/*
	 * No parameter. The layers in the session screen have changed
	 */
	SESSION_SCREEN_LAYER_CHANGED,

	/*
	 * No parameter. The configuration of the session screen changed
	 */
	SESSION_SCREEN_CHANGED,

	/*
	 * 0=Grid Width, 1=Offset X, 2=Offset Y
	 */
	SESSION_SCREEN_REGULAR_GRID_CHANGED,

	/*
	 * NO parameter. Explore alpha map changed
	 */
	SESSION_SCREEN_UNCOVERED,

	/*
	 * 0=CharacterHandle
	 */
	PRINT_REQUESTED,

}
