package org.prelle.rpgframework.products;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

import org.prelle.simplepersist.XMLElementConverter;
import org.prelle.simplepersist.marshaller.XmlNode;
import org.prelle.simplepersist.unmarshal.XMLTreeItem;

import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.products.Adventure;
import de.rpgframework.products.ProductServiceLoader;

public class AdventureConverter implements XMLElementConverter<AdventureImpl> {

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.simplepersist.Converter#write(org.prelle.simplepersist.marshaller.XmlNode, java.lang.Object)
	 */
	@Override
	public void write(XmlNode node, AdventureImpl value) throws Exception {
		if (value==null)
			return;
//		XmlNode advRef = new XmlNode("adventure", null);
//		if (value.getRules()!=null)
//			advRef.setAttribute("rules", value.getRules().name());
//		advRef.setAttribute("ref", value.getId());
//		
//		node.getChildren().add(advRef);
		if (value.getRules()!=null)
			node.setAttribute("rules", value.getRules().name());
		node.setAttribute("ref", value.getId());
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.simplepersist.Converter#read(org.prelle.simplepersist.Persister.ParseNode, javax.xml.stream.events.StartElement)
	 */
	@Override
	public AdventureImpl read(XMLTreeItem node, StartElement ev, XMLEventReader evRd) throws Exception {
		if (ev.getAttributeByName(new QName("rules"))==null)
			throw new IllegalArgumentException("Missing 'rules' attribute in "+ev);
		if (ev.getAttributeByName(new QName("ref"))==null)
			throw new IllegalArgumentException("Missing 'ref' attribute in "+ev);

		String rulesID = ev.getAttributeByName(new QName("rules")).getValue();
		String advID = ev.getAttributeByName(new QName("ref")).getValue();

		Adventure ret =  ProductServiceLoader.getInstance().getAdventure(
				RoleplayingSystem.valueOf(rulesID),
				advID
				);
		if (ret==null) {
			System.err.println("No adventure with ID '"+advID+"' in rules "+rulesID);
			throw new RuntimeException("No adventure with ID '"+advID+"' in rules "+rulesID);
		}
		return (AdventureImpl)ret;
	}

}
