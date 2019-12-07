/**
 * 
 */
package org.prelle.rpgframework;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import de.rpgframework.ConfigOption;
import de.rpgframework.StringConverter;

/**
 * @author prelle
 *
 */
public class ConfigOptionImpl<T> extends ConfigNodeImpl implements ConfigOption<T> {

	private Type type;
	private T[] choiceOptions;
	private T defaultValue;
	private int lowerLimit;
	private int upperLimit;
	private StringConverter<T> converter;
	
	//--------------------------------------------------------------------
	public ConfigOptionImpl(String id, Type type, T defVal) {
		super(id);
		this.type = type;
		defaultValue = defVal;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getChoiceOptions()
	 */
	@Override
	public T[] getChoiceOptions() {
		return choiceOptions;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#setOptions(java.lang.Object[])
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setOptions(T... choices) {
		choiceOptions = choices;
	}
	
	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getOptionName(java.lang.Object)
	 */
	public String getOptionName(T obj) {
		String i18nKey = getI18NKey()+".choice."+obj;
		try {
			return parent.getResourceBundle().getString(i18nKey);
		} catch (MissingResourceException e) {
			logger.error("Cannot find key '"+e.getKey()+"' in "+parent.getResourceBundle().getBaseBundleName());
			return e.getKey();
		}
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getDefaultValue()
	 */
	@Override
	public T getDefaultValue() {
		return defaultValue;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getLowerLimit()
	 */
	@Override
	public int getLowerLimit() {
		return lowerLimit;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getType()
	 */
	@Override
	public Type getType() {
		return type;
	}

	//--------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getUpperLimit()
	 */
	@Override
	public int getUpperLimit() {
		return upperLimit;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getValue()
	 */
	@Override
	public T getValue() {
		Preferences PREF = parent.getPreferences();
		switch (type) {
		case BOOLEAN:
			Boolean val = PREF.getBoolean(getLocalId(), (Boolean) defaultValue);
			return (T) val;
		case TEXT:
		case PASSWORD:
			T valChoice = (T) PREF.get(getLocalId(), null);
			if (valChoice==null)
				valChoice = defaultValue;
			return valChoice;
		case CHOICE:
			valChoice = null;
			String strChoice = PREF.get(getLocalId(), null);
			if (strChoice!=null && converter!=null)
				valChoice = converter.fromString(strChoice);
			if (valChoice==null && converter==null) {
				logger.warn("No converter for CHOICE string of "+getPathID()+" found");
				System.err.println("No converter for CHOICE string of "+getPathID()+" found");
			}
			if (valChoice==null)
				valChoice = defaultValue;
			return valChoice;
		case NUMBER:
			if (defaultValue instanceof Integer)
				return (T) (Integer)PREF.getInt(getLocalId(), (Integer) defaultValue);
			else if (defaultValue instanceof Float)
				return (T) (Float)PREF.getFloat(getLocalId(), (Float) defaultValue);
			else
				return (T) (Double)PREF.getDouble(getLocalId(), (Double) defaultValue);
		case MULTI_CHOICE:
			strChoice = PREF.get(getLocalId(), null);
			if (strChoice==null)
				return defaultValue;
			logger.debug("ConfigOptionImpl: convert to array: "+strChoice);
			StringTokenizer tok = new StringTokenizer(strChoice);
			List<T> converted = new ArrayList<T>();
			while (tok.hasMoreTokens()) {
				String token = tok.nextToken();
				if (converter!=null)
					converted.add(converter.fromString(token));
				else
					System.err.println("No converter for MULTICHOICE string of "+getPathID()+" found");
			}
			return (T) converted.toArray();
		default:
			return (T) PREF.get(getLocalId(), null);
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getStringValue()
	 */
	@Override
	public String getStringValue() {
		Preferences PREF = parent.getPreferences();
//		logger.debug("getValue("+PREF+"  id="+getLocalId()+") is type "+type);
		switch (type) {
		case BOOLEAN:
			return String.valueOf(PREF.getBoolean(getLocalId(), (Boolean)defaultValue));
		case TEXT:
		case PASSWORD:
		case CHOICE:
			if (converter==null)
				return PREF.get(getLocalId(),(String)defaultValue);
			return PREF.get(getLocalId(), converter.toString(defaultValue));
		case NUMBER:
			return String.valueOf(PREF.getInt(getLocalId(), (Integer) defaultValue));
		default:
			return String.valueOf(PREF.get(getLocalId(), null));
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#set(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void set(Object newVal) {
		logger.info("set "+this.getName()+" to "+newVal);
		Preferences PREF = parent.getPreferences();
		String oldVal = PREF.get(getLocalId(), null);
		if ((oldVal==null && newVal==null) || (oldVal!=null && newVal!=null && oldVal.equals(newVal)))
				return;
		
		if (newVal==null || String.valueOf(newVal).isEmpty())
			PREF.remove(getLocalId());
		else {
			switch (type) {
			case MULTI_CHOICE:
				List<String> multiS = new ArrayList<>();
				if (newVal instanceof Object[]) {
					logger.debug("Array found");
				} else if (newVal instanceof List) {
					logger.debug("List found");
					for (T val : (List<T>)newVal) {
						logger.debug("  list item "+val+" / "+val.getClass());
						if (converter!=null && !val.getClass().isEnum()) {
							multiS.add(converter.toString((T) val));
						} else
							multiS.add(String.valueOf(val));
						logger.debug("  multis now "+multiS);
					}
					newVal = String.join(" ", multiS);
				} else {
					logger.debug("No array");
					if (converter!=null)
						newVal = converter.toString((T) newVal);
				}
				PREF.put(getLocalId(), String.valueOf(newVal));
				break;
			default:

				PREF.put(getLocalId(), String.valueOf(newVal));
			}
		}
		logger.debug("Option "+getPathID()+" changed "+((type==Type.PASSWORD)?"":"to "+getValue())+"   PREF KEY = "+getLocalId());
		super.parent.markRecentlyChanged(this);
		super.parent.fireConfigChange();

	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#setValueConverter(de.rpgframework.StringConverter)
	 */
	@Override
	public void setValueConverter(StringConverter<T> converter) {
		this.converter = converter; 
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.ConfigOption#getValueConverter()
	 */
	@Override
	public StringConverter<T> getValueConverter() {
		return converter;
	}

}
