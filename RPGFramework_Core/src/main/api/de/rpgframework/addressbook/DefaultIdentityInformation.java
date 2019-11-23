/**
 * 
 */
package de.rpgframework.addressbook;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author prelle
 *
 */
public class DefaultIdentityInformation implements IdentityInformation {
	
	private InfoType type;
	protected String value;
	private Map<Parameter, String> parameter;

	//-----------------------------------------------------------------
	/**
	 */
	public DefaultIdentityInformation(InfoType type, String value) {
		this.type  = type;
		this.value = value;
		parameter  = new HashMap<Parameter, String>();
	}

	//-----------------------------------------------------------------
	/**
	 */
	@SuppressWarnings("unchecked")
	public DefaultIdentityInformation(InfoType type, String value, Entry<Parameter, String>... entries) {
		this.type  = type;
		this.value = value;
		parameter  = new HashMap<Parameter, String>();
		for (Entry<Parameter,String> entry: entries)
			parameter.put(entry.getKey(), entry.getValue());
	}

	//-----------------------------------------------------------------
	public String toString() {
		StringBuffer buf = new StringBuffer(type+"="+value);
		if (!parameter.isEmpty()) {
			buf.append("(");
			Iterator<Parameter> it = parameter.keySet().iterator();
			while (it.hasNext()) {
				Parameter p = it.next();
				buf.append(p+"="+parameter.get(p));
				if (it.hasNext())
					buf.append(",");
			}
			buf.append(")");
		}
		
		return buf.toString();
	}

	//-----------------------------------------------------------------
	/**
	 * @see org.prelle.social.IdentityInformation#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.addressbook.IdentityInformation#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.addressbook.IdentityInformation#getType()
	 */
	@Override
	public InfoType getType() {
		return type;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.addressbook.IdentityInformation#getParameter(de.rpgframework.addressbook.Parameter)
	 */
	@Override
	public String getParameter(Parameter param) {
		return parameter.get(param);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.addressbook.IdentityInformation#setParameter(de.rpgframework.addressbook.Parameter, java.lang.String)
	 */
	@Override
	public void setParameter(Parameter param, String value) {
		parameter.put(param, value);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.addressbook.IdentityInformation#getParameters()
	 */
	@Override
	public Set<Entry<Parameter, String>> getParameters() {
		return parameter.entrySet();
	}

}
