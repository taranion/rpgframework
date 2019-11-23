/**
 * 
 */
package de.rpgframework.music;

/**
 * @author Stefan
 *
 */
public class Filter {

	public enum Type {
		GENRE,
		MOOD,
	}
	
	private Type type;
	private Object value;
	
	//--------------------------------------------------------------------
	public Filter(Type type, Object val) {
		this.type = type;
		this.value = val;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

}
