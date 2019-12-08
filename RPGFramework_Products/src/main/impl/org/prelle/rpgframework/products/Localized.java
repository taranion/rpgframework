/**
 *
 */
package org.prelle.rpgframework.products;

import java.util.Locale;

import org.prelle.simplepersist.Attribute;

/**
 * @author Stefan
 *
 */
public abstract class Localized {

	@Attribute
	protected String lang;

	//--------------------------------------------------------------------
	protected Localized() {
	}

	//--------------------------------------------------------------------
	protected Localized(Locale locale) {
		lang = locale.getLanguage();
	}

	//--------------------------------------------------------------------
	protected Localized(@Attribute(name="lang") String lang) {
		this.lang = lang;
	}

	//--------------------------------------------------------------------
	@Override
	public boolean equals(Object o) {
		if (o instanceof Localized)
			return ((Localized)o).getLanguage().equals(lang);
		return false;
	}

	//--------------------------------------------------------------------
	public String getLanguage() {
		return lang;
	}

	//--------------------------------------------------------------------
	public Locale getLocale() {
		for (Locale loc : Locale.getAvailableLocales()) {
			if (loc.getLanguage().equals(lang))
				return loc;
		}
		return null;
	}

	//--------------------------------------------------------------------
	public void setLanguage(String lang) {
		this.lang = lang;
	}

	//--------------------------------------------------------------------
	public void setLocale(Locale loc) {
		lang = loc.getLanguage();
	}
}
