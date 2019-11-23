package de.rpgframework;


public interface ConfigNode {

	//--------------------------------------------------------------------
	public String getLocalId();

	//--------------------------------------------------------------------
	/**
	 * Returns the full path identifier
	 */
	public String getPathID();

	//--------------------------------------------------------------------
	/**
	 * Returns a internationalized name of the option
	 */
	public String getName();

	//--------------------------------------------------------------------
	public void setName(String name);

}