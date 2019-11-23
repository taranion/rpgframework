/**
 * 
 */
package de.rpgframework.adventure;

import de.rpgframework.core.RoleplayingSystem;

/**
 * Every data structure bound to a specific roleplaying
 * system that can be serialized into an enhanced adventure
 * module needs to implement this interface.
 * E.g. Creature stats
 * 
 * @author Stefan
 *
 */
public interface RuleSpecificAdventureData {

	public RoleplayingSystem getSystem();
	
}
