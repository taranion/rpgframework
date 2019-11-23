/**
 * 
 */
package de.rpgframework.character;


/**
 * Data taken from a hardcopy rulebook, to store references to 
 * pages (depending on the language)
 * 
 * @author Stefan
 *
 */
public interface HardcopyPluginData extends PluginData {
	
	//--------------------------------------------------------------------
	/**
	 * If this data reflects a product, this method returns the 
	 * unabbreviated product name.
	 * @return full product name or NULL
	 */
	public String getProductName();
	
	//--------------------------------------------------------------------
	/**
	 * If this plugin reflects a product, this method returns the 
	 * abbreviated product name.
	 * @return Short product name or NULL
	 */
	public String getProductNameShort();

	//--------------------------------------------------------------------
	public int getPage();
	
	//--------------------------------------------------------------------
	/**
	 * Given that the license if granted, this method returns a longer
	 * help text with an excerpt from the rules.
	 * 
	 * @return Help text or NULL
	 */
	public String getHelpText();

	//--------------------------------------------------------------------
	public String getName();
	
}
