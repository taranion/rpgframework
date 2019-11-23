/**
 * 
 */
package de.rpgframework.media;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author prelle
 *
 */
public class TagRegistry {
	
	private static List<Class<? extends MediaTag>> taxonomy;

	//-------------------------------------------------------------------
	static {
		taxonomy = new ArrayList<Class<? extends MediaTag>>();
		register(TagAge.class);
		register(TagGender.class);
		register(TagLandscape.class);
		register(TagLifeform.class);
		register(TagMood.class);
		register(TagPlaces.class);
		register(TagTempo.class);
		register(TagWildness.class);
	}

	//-------------------------------------------------------------------
	public static void register(Class<? extends MediaTag> cls) {
		if (!cls.isEnum())
			throw new IllegalArgumentException("Can only register enums of MediaTag");
		//cls.getEnumConstants();
		
		if (!taxonomy.contains(cls))
			taxonomy.add(cls);
	}

	//-------------------------------------------------------------------
	public static Collection<Class<? extends MediaTag>> getKnownTagTypes() {
		return taxonomy;
	}

	//-------------------------------------------------------------------
	public static String getTagTypeID(MediaTag tag) {
		return tag.getClass().getSimpleName();
	}

	//-------------------------------------------------------------------
	public static MediaTag resolveTag(String simpleClsName, String value) {
		for (Class<? extends MediaTag> cls : taxonomy) {
			if (cls.getSimpleName().equals(simpleClsName)) {
				try {
					return (MediaTag)cls.getMethod("valueOf", String.class).invoke(null, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
