/**
 * 
 */
package de.rpgframework;

import java.util.List;

/**
 * @author prelle
 *
 */
public class DummyRPGFrameworkInitCallback implements RPGFrameworkInitCallback {

	//-------------------------------------------------------------------
	/**
	 */
	public DummyRPGFrameworkInitCallback() {
		// TODO Auto-generated constructor stub
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFrameworkInitCallback#progressChanged(double)
	 */
	@Override
	public void progressChanged(double value) {
		// TODO Auto-generated method stub

	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFrameworkInitCallback#message(java.lang.String)
	 */
	@Override
	public void message(String mess) {
		System.out.println(mess);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFrameworkInitCallback#errorOccurred(java.lang.String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void errorOccurred(String location, String detail, Throwable exception) {
		System.err.println(location+": "+detail);
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.RPGFrameworkInitCallback#showConfigOptions(java.lang.String, java.util.List)
	 */
	@Override
	public void showConfigOptions(String id, List<ConfigOption<?>> configuration) {
		// TODO Auto-generated method stub

	}

}
