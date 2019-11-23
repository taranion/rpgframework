package de.rpgframework;

import java.util.List;

public interface RPGFrameworkInitCallback {
	
	public void progressChanged(double value);
	
	public void message(String mess);
	
	public void errorOccurred(String location, String detail, Throwable exception);

	//-------------------------------------------------------------------
	void showConfigOptions(String id, List<ConfigOption<?>> configuration);
	
}