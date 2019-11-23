/**
 * 
 */
package de.rpgframework.session;

/**
 * Definies the layer level on the session screen.
 * 
 * @author prelle
 *
 */
public enum SessionScreenLevel implements Comparable<SessionScreenLevel> {

	/**
	 * Background images or battlemap images
	 */
	BACKGROUND,
	/**
	 * Blacks out all area not already explored
	 */
	EXPLORED,
	/**
	 * Overlays on battlemaps - either squares or hexes. 
	 */
	GRID,
	/**
	 * PC and NPC/Monster tokens
	 */
	TOKENS,
	/**
	 * Darken those areas not visible to the player
	 */
	FIELD_OF_VIEW,
	/**
	 * Player health view, Initiative view
	 */
	COMBAT_DETAILS,
	/**
	 * Handouts to display to the players
	 */
	HANDOUTS,
	/**
	 * Short messages - e.g. to indicate whos turn it is
	 */
	MESSAGES
	;

}
