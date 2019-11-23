package org.prelle.rpgframework;

import java.util.MissingResourceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ConfigNode;

/**
 * @author prelle
 *
 */
public abstract class ConfigNodeImpl implements ConfigNode {

	protected final static Logger logger = LogManager.getLogger("babylon.config");

	protected String id;
	protected ConfigContainerImpl parent;
	protected String name;

	//--------------------------------------------------------------------
	protected ConfigNodeImpl(String id) {
		if (id==null)
			throw new NullPointerException();
		this.id = id;
	}

	//--------------------------------------------------------------------
	public String toString() {
		return id+" with i18nKey "+getI18NKey()+" and resource bundle "+parent.getResourceBundle().getBaseBundleName()+" and pref "+parent.getPreferences();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigNode#getLocalId()
	 */
	@Override
	public String getLocalId() {
		return id;
	}

	//--------------------------------------------------------------------
	public String getI18NKey() {
		return parent.getI18NKey()+"."+id;
	}

	//--------------------------------------------------------------------
	@Override
	public String getPathID() {
		if (parent!=null)
			return parent.getPathID()+"."+id;
		return id;
	}

	//--------------------------------------------------------------------
	void setParent(ConfigContainerImpl parent) {
		this.parent = parent;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigNode#getName()
	 */
	@Override
	public String getName() {
		if (name!=null)
			return name;

		try {
			return parent.getResourceBundle().getString(getI18NKey());
		} catch (MissingResourceException e) {
			logger.error("Cannot find key '"+e.getKey()+"' in "+parent.getResourceBundle()+"/"+parent.getResourceBundle().getBaseBundleName());
			return e.getKey();
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigNode#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
}
