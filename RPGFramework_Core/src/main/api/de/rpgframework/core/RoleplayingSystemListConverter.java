package de.rpgframework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.prelle.simplepersist.StringValueConverter;

import de.rpgframework.core.RoleplayingSystem;

/**
 * @author prelle
 *
 */
public class RoleplayingSystemListConverter implements StringValueConverter<Collection<RoleplayingSystem>> {

	//-------------------------------------------------------------------
	/**
	 */
	public RoleplayingSystemListConverter() {
		// TODO Auto-generated constructor stub
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.simplepersist.StringValueConverter#write(java.lang.Object)
	 */
	@Override
	public String write(Collection<RoleplayingSystem> value) throws Exception {
		StringBuffer buf = new StringBuffer();
		Iterator<RoleplayingSystem> it = value.iterator();
		while (it.hasNext()) {
			buf.append(it.next().name());
			if (it.hasNext())
				buf.append(",");
		}
		
		return buf.toString();
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.simplepersist.StringValueConverter#read(java.lang.String)
	 */
	@Override
	public Collection<RoleplayingSystem> read(String v) throws Exception {
		List<RoleplayingSystem> ret = new ArrayList<>();
		for (StringTokenizer tok=new StringTokenizer(v, ","); tok.hasMoreTokens(); )
			ret.add(RoleplayingSystem.valueOf(tok.nextToken()));
		return ret;
	}

}
