/**
 *
 */
package org.prelle.rpgframework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.PropertyResourceBundle;
import java.util.prefs.Preferences;

import de.rpgframework.ConfigChangeListener;
import de.rpgframework.ConfigContainer;
import de.rpgframework.ConfigNode;
import de.rpgframework.ConfigOption;
import de.rpgframework.ConfigOption.Type;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;

/**
 * @author prelle
 *
 */
public class ConfigContainerImpl extends ConfigNodeImpl implements ConfigContainer {

	private PropertyResourceBundle RES = BabylonConstants.RES;
	protected Preferences PREF;

	private List<ConfigNodeImpl> children;
	private Collection<ConfigChangeListener> listener;
	private List<ConfigOption<?>> recentlyChanged;

	//--------------------------------------------------------------------
	public ConfigContainerImpl(Preferences pref, String id) {
		super(id);
		if (pref==null)
			throw new NullPointerException("Preferences may not  be null");
		PREF = pref;
		children = new ArrayList<ConfigNodeImpl>();
		listener = new ArrayList<ConfigChangeListener>();
		recentlyChanged = new ArrayList<ConfigOption<?>>();
	}

	//--------------------------------------------------------------------
	public String getI18NKey() {
		if (RES==null)
			System.err.println("No resource bundle attached to ConfigNode "+getPathID());

		if (parent==null)
			return "config."+id;

		return parent.getI18NKey()+"."+id;
	}

	//--------------------------------------------------------------------
	@Override
	public String getPathID() {
		if (parent!=null)
			return parent.getPathID()+"."+getLocalId();
		return getLocalId();
	}

	//--------------------------------------------------------------------
	public String toString() {
		return id+" with i18nKey "+getI18NKey()+" and resource bundle "+RES.getBaseBundleName()+" and pref "+PREF;
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
			return RES.getString(getI18NKey());
		} catch (MissingResourceException e) {
			logger.error("Cannot find key '"+e.getKey()+"' in "+RES.getBaseBundleName());
			return e.getKey();
		}
	}


	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ConfigNode> iterator() {
		return (new ArrayList<ConfigNode>(children)).iterator();
	}

	//--------------------------------------------------------------------
	public void addChild(ConfigNodeImpl child) {
		logger.debug("Add child "+child.getLocalId()+" to "+getPathID());
		child.setParent(this);
		if (!children.contains(child))
			children.add(child);
	}

	//--------------------------------------------------------------------
	@Override
	void setParent(ConfigContainerImpl parent) {
		super.setParent(parent);
//		if (PREF!=null)
//			PREF = parent.getPreferences().node(getLocalId());
	}

	//-------------------------------------------------------------------
	public Collection<ConfigNode> getChildren() {
		return new ArrayList<ConfigNode>(children);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#getOption(java.lang.String)
	 */
	@Override
	public ConfigOption<?> getOption(String id) {
		for (ConfigNode child : children) {
			if (((ConfigNodeImpl)child).getLocalId().equals(id)) {
				if (child instanceof ConfigOption)
					return (ConfigOption<?>) child;
				throw new NoSuchElementException("Element exists but is not an ConfigOption");
			}
		}
		throw new NoSuchElementException("Did not find '"+id+"' in "+children+" of "+this);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#getOption(java.lang.String)
	 */
	@Override
	public ConfigNode getChild(String id) {
		for (ConfigNode child : children) {
			if (((ConfigNodeImpl)child).getLocalId().equals(id)) {
				return child;
			}
		}
		throw new NoSuchElementException();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#addListener(de.rpgframework.ConfigChangeListener)
	 */
	@Override
	public void addListener(ConfigChangeListener callback) {
		if (!listener.contains(callback))
			listener.add(callback);

	}

	//--------------------------------------------------------------------
	void markRecentlyChanged(ConfigOption<?> option) {
		synchronized (recentlyChanged) {
			if (!recentlyChanged.contains(option))
				recentlyChanged.add(option);
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#fireConfigChange()
	 */
	@Override
	public void fireConfigChange() {
		synchronized (recentlyChanged) {
			if (recentlyChanged.isEmpty())
				return;
			for (ConfigChangeListener callback : listener) {
				try {
					callback.configChanged(this, recentlyChanged);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (ConfigOption<?> opt : recentlyChanged) {
				try {
					BabylonEventBus.fireEvent(BabylonEventType.CONFIG_OPTION_CHANGED, opt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			recentlyChanged.clear();
		}
	}

	//--------------------------------------------------------------------
	PropertyResourceBundle getResourceBundle() {
		return RES;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#setResourceBundle(java.util.PropertyResourceBundle)
	 */
	@Override
	public void setResourceBundle(PropertyResourceBundle res) {
		if (res==null)
			throw new NullPointerException("ResourceBundle is NULL");

//		if (res.getBaseBundleName()==null)
//			throw new NullPointerException("ResourceBundle has no name");
		this.RES = res;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#changePreferences(java.util.prefs.Preferences)
	 */
	@Override
	public void changePreferences(Preferences pref) {
		PREF = pref;
		for (ConfigNodeImpl child : children) {
			if (child instanceof ConfigContainer)
				((ConfigContainer)child).changePreferences(pref);
		}
	}

	//-------------------------------------------------------------------
	public Preferences getPreferences() {
		return PREF;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#createOption(java.lang.String, de.rpgframework.ConfigOption.Type, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> ConfigOption<T> createOption(String id, Type type, T defValue) {
		ConfigOptionImpl<T> ret = new ConfigOptionImpl(id, type, defValue);
		ret.setParent(this);
		children.add(ret);
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#createContainer(java.lang.String)
	 */
	@Override
	public ConfigContainer createContainer(String id) {
		Preferences childPref = PREF.node(id);
		ConfigContainerImpl ret = new ConfigContainerImpl(childPref, id);
		ret.setParent(this);
		ret.setResourceBundle(RES);
		children.add(ret);
		return ret;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigContainer#removeChild(java.lang.String)
	 */
	@Override
	public void removeChild(String id) {
		for (ConfigNode child : children) {
			if (((ConfigNodeImpl)child).getLocalId().equals(id)) {
				children.remove(child);
				return;
			}
		}
	}

}
