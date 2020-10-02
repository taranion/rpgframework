package de.rpgframework.print2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.prelle.simplepersist.Persister;
import org.prelle.simplepersist.SerializationException;

import de.rpgframework.character.RuleSpecificCharacterObject;
import de.rpgframework.print.ElementCell;
import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PDFPrintElementFeature;
import de.rpgframework.print.PrintTemplate;
import de.rpgframework.print.TemplateFactory;
import de.rpgframework.print.PDFPrintElement.RenderingParameter;

public class PersistenceTest {
	
	private static Persister persist;
	private static Map<String,PDFPrintElement> elementMap;
	
	@Before
	public void setUpBeforeClass() throws Exception {
		persist = new Persister();
		elementMap = new HashMap<String, PDFPrintElement>();
		elementMap.put("Hallo", new PDFPrintElement() {
			
			@Override
			public byte[] render(RenderingParameter parameter) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean hasFeature(PDFPrintElementFeature feature) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getRequiredColumns() {
				// TODO Auto-generated method stub
				return 3;
			}
			
			@Override
			public int getPreviousHorizontalGrowth(RenderingParameter parameter) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getNextHorizontalGrowth(RenderingParameter parameter) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> getFilterOptions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getIndexableObjectNames(RuleSpecificCharacterObject character) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	//---------------------------------------------------------
	@Test
	public void testPage() throws SerializationException, IOException {
		LayoutGrid page = TemplateFactory.createPageDefinition(6);
		TemplateFactory.newTemplateController(page, elementMap);
		ElementCell comp = page.addComponent(2, 0, elementMap.get("Hallo"));
		assertNotNull(comp);
		assertEquals("Hallo", comp.getElementId());
		
		StringWriter out = new StringWriter();
		persist.write(page, out);
		System.out.println(out.toString());
	}

	//---------------------------------------------------------
	@Test
	public void testTemplate() throws SerializationException, IOException {
		LayoutGrid page = TemplateFactory.createPageDefinition(6);
		TemplateFactory.newTemplateController(page, elementMap);
		ElementCell comp = page.addComponent(2, 0, elementMap.get("Hallo"));
		assertNotNull(comp);
		assertEquals("Hallo", comp.getElementId());
		
		PrintTemplate temp = new PrintTemplate();
		temp.add(page);
		
		StringWriter out = new StringWriter();
		persist.write(temp, out);
		System.out.println(out.toString());
	}

}
