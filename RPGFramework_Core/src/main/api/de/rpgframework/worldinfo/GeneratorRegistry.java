/**
 *
 */
package de.rpgframework.worldinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.rpgframework.core.AppliedFilter;

/**
 * @author Stefan
 *
 */
public class GeneratorRegistry {

	private static Map<WorldInformationType, List<Generator<?>>> generators = new HashMap<>();

	//--------------------------------------------------------------------
	public static void register(Generator<?> value) {
		List<Generator<?>> list = generators.get(value.getType());
		if (list==null) {
			list = new ArrayList<>();
			generators.put(value.getType(), list);
		}

		list.add(value);
	}

	//--------------------------------------------------------------------
	public static List<Generator<?>> getGenerators(WorldInformationType type, AppliedFilter[] filter) {
		List<Generator<?>> ret = new ArrayList<>();
		if (generators.get(type)!=null) {
			// Collect all possible generatory
			for (Generator<?> gen : generators.get(type)) {
				if (gen.willWork(filter))
					ret.add(gen);
			}
			// Sort them by matching supported filters
			Collections.sort(ret, new Comparator<Generator<?>>() {
				public int compare(Generator<?> o1, Generator<?> o2) {
					Long val1 = Arrays.asList(filter).stream().filter(obj -> o1.getSupportedFilter().contains(((AppliedFilter)obj).getFilter())).count();
					Long val2 = Arrays.asList(filter).stream().filter(obj -> o2.getSupportedFilter().contains(((AppliedFilter)obj).getFilter())).count();
					return val1.compareTo(val2);
				}
			});
		}
		return ret;
	}

}
