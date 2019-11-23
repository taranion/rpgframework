/**
 * 
 */
package de.rpgframework.addressbook;

/**
 * @author spr
 *
 */
public enum InfoType {
	
	UNKNOWN, 
	
	FIRSTNAME,
	LASTNAME,
	MIDDLENAME,
	NICKNAME,
	IMAGE,
	
	BIRTHDAY,
	SPOUSE,
	HOMEPAGE(true,false),
	BLOG,
	
	ADDRESS(true,false),
	
	PHONE(true,false),
	
	EMAIL(true,true),
	
	IMPP_JABBER(true,true),
	IMPP_ICQ(true,false),
	IMPP_IRC(true,false),
	IMPP_COSPACE(true,true),
	IMPP_SKYPE(true,false),
	;
	
	boolean multiple;
	boolean useScope;

	//-----------------------------------------------------------------
	private InfoType() {
		multiple = false;
		useScope = false;
	}
	
	//-----------------------------------------------------------------
	private InfoType(boolean useScope, boolean multi) {
		this.useScope = useScope;
		multiple = multi;
	}
	
	//-----------------------------------------------------------------
	public boolean requiresScope() {
		return useScope;
	}
	
	//-----------------------------------------------------------------
	/**
	 * Can the type of information be used multiple times per scope
	 * @return
	 */
	public boolean canAppearMultipleTimes() {
		return multiple;
	}
	
}
