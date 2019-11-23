/**
 *
 */
package de.rpgframework.boot;

import java.util.List;

import de.rpgframework.ConfigOption;
import de.rpgframework.RPGFrameworkInitCallback;

/**
 * @author Stefan
 *
 */
public interface BootStep {

	public String getID();

	/**
	 * Relative amount of time necessary to execute this step
	 * 10 = Light
	 * 20 = Medium
	 * 30 = Heavy
	 */
	public int getWeight();

	public boolean shallBeDisplayedToUser();

	public List<ConfigOption<?>> getConfiguration();

	/**
	 * @return TRUE, if execution was successful
	 */
	public boolean execute(RPGFrameworkInitCallback callback);

}
