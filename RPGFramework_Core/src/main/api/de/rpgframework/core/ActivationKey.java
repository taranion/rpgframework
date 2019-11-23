package de.rpgframework.core;

import de.rpgframework.core.RoleplayingSystem;

public interface ActivationKey {
	
	enum Duration {
		UNLIMITED,
		YEAR,
	}

	//--------------------------------------------------------------------
	/**
	 * @return the system
	 */
	public RoleplayingSystem getSystem();

	//--------------------------------------------------------------------
	/**
	 * @return the value
	 */
	public String getValue();

	//--------------------------------------------------------------------
	/**
	 * @return the until
	 */
	public Duration getDuration();

	//--------------------------------------------------------------------
	public String getCompany();

	//--------------------------------------------------------------------
	public String getLanguage();

}