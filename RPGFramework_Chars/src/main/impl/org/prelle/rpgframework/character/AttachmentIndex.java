/**
 * 
 */
package org.prelle.rpgframework.character;

import java.util.ArrayList;

import org.prelle.simplepersist.ElementList;
import org.prelle.simplepersist.Root;

/**
 * @author prelle
 *
 */
@SuppressWarnings("serial")
@Root(name="index")
@ElementList(entry="entry",type=AttachmentEntry.class,inline=true)
public class AttachmentIndex extends ArrayList<AttachmentEntry> {

	//-------------------------------------------------------------------
	public AttachmentIndex() {
	}

}
