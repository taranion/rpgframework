/**
 *
 */
package de.rpgframework;

/**
 * @author prelle
 *
 */
public interface ConfigOption<T> extends ConfigNode {

	public enum Type {
		TEXT,
		PASSWORD,
		BOOLEAN,
		NUMBER,
		CHOICE,
		MULTI_CHOICE,

		/**
		 * A text containing a valid path to a directory.
		 */
		DIRECTORY,

		/**
		 * A text containing a valid path to a file
		 */
		FILE,
	}

	//--------------------------------------------------------------------
	public Type getType();

	//--------------------------------------------------------------------
	/**
	 * Returns an optional default value for this option.
	 * @return A default value or <code>null</code>
	 */
	public T getDefaultValue();

	//--------------------------------------------------------------------
	/**
	 * Only for type CHOICE
	 */
	public T[] getChoiceOptions();

	//--------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public void setOptions(T...choices);

	//--------------------------------------------------------------------
	public void setValueConverter(StringConverter<T> converter);

	//--------------------------------------------------------------------
	public StringConverter<T> getValueConverter();

	//--------------------------------------------------------------------
	/**
	 * Return a printable name for an options returned from getChoiceOptions()
	 */
	public String getOptionName(T obj);

	//--------------------------------------------------------------------
	/**
	 * Only for type NUMBER
	 */
	public int getLowerLimit();

	//--------------------------------------------------------------------
	/**
	 * Only for type NUMBER
	 */
	public int getUpperLimit();

	//--------------------------------------------------------------------
	public T getValue();

	//--------------------------------------------------------------------
	public String getStringValue();

	//--------------------------------------------------------------------
	public void set(T newVal);

}
