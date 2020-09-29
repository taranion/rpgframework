package org.prelle.rpgframework.jfx.print;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PDFPrintElement.RenderingParameter;
import de.rpgframework.print.PageDefinition;
import de.rpgframework.print.PositionedComponent;
import de.rpgframework.print.TemplateController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * @author Stefan Prelle
 *
 */
public class LayoutGrid extends GridPane {
	
	private final static Logger logger = LogManager.getLogger("rpgframework.javafx");

	private ObjectProperty<PageDefinition> input;
	private IntegerProperty colWidth;
	private Map<String, PDFPrintElement> elementMap;
	
	private List<TemplateCell[]> lines = new ArrayList<TemplateCell[]>();
	
	private TemplateController control;

	//---------------------------------------------------------
	public LayoutGrid(PageDefinition input, TemplateController ctrl, int colWidth, int gap, Map<String, PDFPrintElement> elementMap) {
		getStyleClass().add("template-grid");
		this.colWidth = new SimpleIntegerProperty(colWidth);
		this.input    = new SimpleObjectProperty<PageDefinition>(input);
		this.elementMap = elementMap;
		this.control  = ctrl;

		setGridLinesVisible(true);
		setVgap(gap);
		setHgap(gap);
		refreshColumns();
		refreshCells();
	}

	//---------------------------------------------------------
	private void refreshColumns() {
		super.getColumnConstraints().clear();
		for (int i=0; i<input.get().getRequiredColumns(); i++) {
			ColumnConstraints con = new ColumnConstraints(colWidth.get());
			super.getColumnConstraints().add(con);
		}		
	}

	//---------------------------------------------------------
	private void addNewLine() {
		TemplateCell[] line = new TemplateCell[input.get().getRequiredColumns()];
		for (int i=0; i<line.length; i++) {
			line[i] = new TemplateCell(i,lines.size(),this);
		}
		lines.add(line);
	}

	//---------------------------------------------------------
	private void refreshCells() {
		super.getChildren().clear();
		lines.clear();
		addNewLine();
		
		for (PositionedComponent comp : input.get().getComponents()) {
			int x = comp.getColumn();
			int y = comp.getLine();
			
			// Make sure lines exist
			while (y<lines.size()) {
				addNewLine();
			}
						
			RenderingParameter param = new RenderingParameter();
			param.setOrientation(comp.getOptions().getOrientation());
			param.setVerticalGrowthOffset(comp.getOptions().getVerticalGrow());
			param.setHorizontalGrowthOffset(comp.getOptions().getHorizontalGrow());
			param.setIndex( comp.getOptions().getSelectedIndex() );
			param.setFilterOption( comp.getOptions().getVariantIndex() );

			// Find named component
			PDFPrintElement item = elementMap.get(comp.getElementReference());
			if (item==null) {
				// Could not resolve reference

			} else {
				// Render component
				Image img = new Image(new ByteArrayInputStream(item.render(param)));
				ImageView iview = new ImageView(img);
				iview.setFitWidth(img.getWidth()*0.70);
				iview.setPreserveRatio(true);
				
				super.add(iview, x,y, item.getRequiredColumns(),1);
				for (int i=x; i<(x+item.getRequiredColumns()); i++) {
					lines.get(y)[x] = null;
				}
			}

		}
		
		// Now add remaining empty cells
		for (int y=0; y<lines.size(); y++) {
			for (int x=0; x<lines.get(y).length; x++) {
				Node node = lines.get(y)[x]; 
				if (node!=null) {
					super.add(node, x,y);
				}
			}
		}
	}
	
	//---------------------------------------------------------
	private void clearCellStates() {
		for (TemplateCell[] line : lines) {
			for (TemplateCell cell : line) {
				cell.setPossible(false);
				cell.setImpossible(false);
			}
		}
	}

	//---------------------------------------------------------
	public void dragOver(int x, int y, String reference) {
		// TODO Auto-generated method stub
		clearCellStates();
		PDFPrintElement elem = elementMap.get(reference);
		logger.debug("Dragged "+reference+" at "+x+","+y);
		if (elem!=null) {
			boolean possible = control.canBeAdded(input.get(), elem, x, y);
			if (y>=lines.size()) {
				logger.error("Dragged over "+x+","+y+" but only have "+lines.size()+" rows");
				return;
			}
			TemplateCell[] line = lines.get(y);
			int max = Math.min(input.get().getRequiredColumns(), x+elem.getRequiredColumns());
			for (int i=x; i<max; i++) {
				logger.debug("CHange "+i+"/"+line[i]);
				line[i].setPossible(possible);
				line[i].setImpossible(!possible);
			}
		}
	}

	//---------------------------------------------------------
	public void dragDropped(int x, int y, String reference) {
		// TODO Auto-generated method stub
		PDFPrintElement elem = elementMap.get(reference);
		logger.debug("Dropped "+reference+"/"+elem+" at "+x+","+y);
	}

}
