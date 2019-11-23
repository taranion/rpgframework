package de.rpgframework.core;

import de.rpgframework.core.RoleplayingSystem;

public interface License {

	//--------------------------------------------------------------------
	/**
	 * @return the name
	 */
	public abstract String getName();

	//--------------------------------------------------------------------
	/**
	 * @return the mail
	 */
	public abstract String getMail();

	//--------------------------------------------------------------------
	/**
	 * @return the system
	 */
	public abstract RoleplayingSystem getSystem();

	//--------------------------------------------------------------------
	/**
	 * @return the system
	 */
	public abstract String getLanguage();

	//--------------------------------------------------------------------
	/**
	 * @return the value
	 */
	public abstract String getValue();

	//--------------------------------------------------------------------
	/**
	 * @return the company
	 */
	public abstract String getCompany();

	//--------------------------------------------------------------------
	/**
	 * @return the from
	 */
	public abstract long getFrom();

	//--------------------------------------------------------------------
	/**
	 * @return the until
	 */
	public abstract long getUntil();

}