/**
 * 
 */
package org.prelle.rpgframework;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import de.rpgframework.core.License;
import de.rpgframework.core.RoleplayingSystem;

/**
 * @author Stefan
 *
 */
public class LicenseImpl implements License {

	// MANDATORY: Name of the Licensed party
	private String name;
	// MANDATORY: Emailaddress of the licensed party
	private String mail;
	// MANDATORY: Licensed roleplaying system 
	private RoleplayingSystem system;
	// OPTIONAL: Language license applies to - default is "de"
	private String language = "de";
	/* 
	 * MANDATORY: Value of the license - depends on the plugin
	 * E.g. RULETEXTS to display excerpts from the rulebooks
	 */
	private String value;
	// OPTIONAL: License is valid for all products of this company
	private String company;
	private long from;
	private long until;
	
	//--------------------------------------------------------------------
	public LicenseImpl() {
	}

	//--------------------------------------------------------------------
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(String.format("Licensed for %s(%s) from %s for %s (lang=%s) - ", name, mail, company, system, language));
		if (until==0)
			buf.append("Does not expire");
		else {
			buf.append(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(until)));
		}
		return buf.toString();
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getMail()
	 */
	@Override
	public String getMail() {
		return mail;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getSystem()
	 */
	@Override
	public RoleplayingSystem getSystem() {
		return system;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getCompany()
	 */
	@Override
	public String getCompany() {
		return company;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getFrom()
	 */
	@Override
	public long getFrom() {
		return from;
	}

	//--------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.api.License#getUntil()
	 */
	@Override
	public long getUntil() {
		return until;
	}

	//--------------------------------------------------------------------
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	//--------------------------------------------------------------------
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	//--------------------------------------------------------------------
	/**
	 * @param system the system to set
	 */
	public void setSystem(RoleplayingSystem system) {
		this.system = system;
	}

	//--------------------------------------------------------------------
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	//--------------------------------------------------------------------
	/**
	 * @param from the from to set
	 */
	public void setFrom(long from) {
		this.from = from;
	}

	//--------------------------------------------------------------------
	/**
	 * @param until the until to set
	 */
	public void setUntil(long until) {
		this.until = until;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.core.License#getLanguage()
	 */
	@Override
	public String getLanguage() {
		if (language==null)
			return Locale.GERMAN.getLanguage();
		return language;
	}

	//-------------------------------------------------------------------
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

}
