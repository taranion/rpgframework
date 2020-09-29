package de.rpgframework.print2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;
import org.prelle.simplepersist.Persister;
import org.prelle.simplepersist.SerializationException;

import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PositionedComponent;
import de.rpgframework.print.PrintTemplate;

public class PersistenceTest {
	
	private static Persister persist;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		persist = new Persister();
	}

	//---------------------------------------------------------
	@Test
	public void testPage() throws SerializationException, IOException {
		PageDefinition page = new PageDefinition();
		PositionedComponent comp = page.addComponent(2, 0, "Hallo");
		assertNotNull(comp);
		assertEquals("Hallo", comp.getElementReference());
		
		StringWriter out = new StringWriter();
		persist.write(page, out);
		System.out.println(out.toString());
	}

	//---------------------------------------------------------
	@Test
	public void testTemplate() throws SerializationException, IOException {
		PageDefinition page = new PageDefinition();
		PositionedComponent comp = page.addComponent(2, 0, "Hallo");
		assertNotNull(comp);
		assertEquals("Hallo", comp.getElementReference());
		
		PrintTemplate temp = new PrintTemplate();
		temp.add(page);
		
		StringWriter out = new StringWriter();
		persist.write(temp, out);
		System.out.println(out.toString());
	}

}
