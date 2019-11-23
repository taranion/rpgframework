/**
 *
 */
package de.rpgframework.core;

import java.text.Collator;

/**
 * @author Stefan
 *
 */
public class AppliedFilter implements Comparable<AppliedFilter> {

	private Filter filter;

	private Object value;

	//--------------------------------------------------------------------
	public AppliedFilter(Filter filter, Object value) {
		this.filter = filter;
		this.value  = value;
	}

	//--------------------------------------------------------------------
	public String toString() {
		return filter.getName()+"="+value;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	//--------------------------------------------------------------------
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	//--------------------------------------------------------------------
	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	//--------------------------------------------------------------------
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	//--------------------------------------------------------------------
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AppliedFilter other) {
		int cmp = filter.getInfoLevel().compareTo(other.getFilter().getInfoLevel());
		if (cmp!=0)
			return cmp;
		return Collator.getInstance().compare(value.toString(), other.getValue().toString());
	}

}
