/**
 * 
 */
package de.rpgframework.core;

/**
 * @author prelle
 *
 */
public interface CommandBusListener {
	
	//----------------------------------------------------------------
	/**
	 * Return a name that can be presented to a human
	 */
	public String getReadableName();
	
	//----------------------------------------------------------------
	/**
	 * Check if the plugin is able to support the given command.
	 * 
	 * @param src Class that asks
	 * @param type Command type to be executed
	 * @param values Array of parameters to the command. Which parameters these are
	 *      is dependant on the CommandType (see there)
	 * @return TRUE if listener can perform command
	 */
	public boolean willProcessCommand(Object src, CommandType type, Object... values);
	
	//----------------------------------------------------------------
	/**
	 * Execute the given command
	 * 
	 * @param src Class that asks
	 * @param type Command type to be executed
	 * @param values Array of parameters to the command. Which parameters these are
	 *      is dependant on the CommandType (see there)
	 * @return Result of the executed command - no matter if successful or not.
	 */
	public CommandResult handleCommand(Object src, CommandType type, Object... values);
	
}
