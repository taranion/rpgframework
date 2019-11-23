/**
 * 
 */
package de.rpgframework.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * @author prelle
 *
 */
public class CommandBus {
	
	private static List<CommandBusListener> listener;

	//----------------------------------------------------------------
	static {
		listener = new ArrayList<CommandBusListener>();
	}

	//----------------------------------------------------------------
	public static void registerBusCommandListener(CommandBusListener callback) {
		if (!listener.contains(callback))
			listener.add(callback);
	}

	//----------------------------------------------------------------
	public static void unregisterBusCommandListener(CommandBusListener callback) {
		listener.remove(callback);
	}

	//----------------------------------------------------------------
	public static boolean canProcessCommand(Object src, CommandType type, Object... param) {
		for (CommandBusListener callback : listener) {
			try {
				if (callback.willProcessCommand(src, type, param)) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter err = new StringWriter();
				e.printStackTrace(new PrintWriter(err));
				return false;
			}
		}			
		return false;
	}

	//----------------------------------------------------------------
	public static List<String> findCommandProcessors(Object src, CommandType type, Object... param) {
		List<String> ret = new ArrayList<>();
		for (CommandBusListener callback : listener) {
			try {
				if (callback.willProcessCommand(src, type, param)) {
					ret.add(callback.getReadableName());
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter err = new StringWriter();
				e.printStackTrace(new PrintWriter(err));
			}
		}			
		return ret;
	}

	//----------------------------------------------------------------
	public static CommandResult fireCommand(Object src, CommandType type, Object... param) {
//		System.out.println("##########Fire "+type+" with "+Arrays.toString(param));
//		try {
//			throw new RuntimeException("Trace");
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		for (CommandBusListener callback : listener) {
			try {
				if (callback.willProcessCommand(src, type, param)) {
//					System.out.println("##########"+callback+" will process "+type);
					return callback.handleCommand(src, type, param);
				}
//				System.out.println(callback+" wont process "+type);
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter err = new StringWriter();
				e.printStackTrace(new PrintWriter(err));
				return new CommandResult(type, false, err.toString());
			}
		}			
		return new CommandResult(type, false, "No responsible plugin found", false);
	}

	//----------------------------------------------------------------
	public static CommandResult fireCommand(String pluginID, Object src, CommandType type, Object... param) {
//		System.out.println("##########Fire "+type+" with "+Arrays.toString(param));
		for (CommandBusListener callback : listener) {
			if (!callback.getReadableName().equals(pluginID))
				continue;
			try {
				if (callback.willProcessCommand(src, type, param)) {
					System.out.println("##########"+callback+" will process "+type);
					return callback.handleCommand(src, type, param);
				}
				System.out.println(callback+" wont process "+type);
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter err = new StringWriter();
				e.printStackTrace(new PrintWriter(err));
				return new CommandResult(type, false, err.toString());
			}
		}			
		return new CommandResult(type, false, "No responsible plugin found", false);
	}

}
