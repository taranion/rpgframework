package org.prelle.rpgframework.jfx.print;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.ResourceI18N;
import de.rpgframework.character.RuleSpecificCharacterObject;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.print.ElementCell;
import de.rpgframework.print.LayoutGrid;
import de.rpgframework.print.MultiRowCell;
import de.rpgframework.print.PDFPrintElement;
import de.rpgframework.print.PDFPrintElement.RenderingParameter;
import de.rpgframework.print.PrintCell;
import de.rpgframework.print.PrintTemplate;
import de.rpgframework.print.TemplateController;
import de.rpgframework.print.TemplateFactory;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * @author Stefan Prelle
 *
 */
public class LayoutGridPane extends GridPane {

	private final static Logger logger = LogManager.getLogger("rpgframework.javafx");
	private final static ResourceBundle RES = ResourceBundle.getBundle(LayoutGridPane.class.getName());

	private ObjectProperty<LayoutGrid> input;
	private IntegerProperty colWidth;
	private Map<String, PDFPrintElement> elementMap;
	private int gap;

	private List<TemplateCell[]> lines = new ArrayList<TemplateCell[]>();
	private TemplateCell selected;
	private RuleSpecificCharacterObject character;

	private TemplateController control;
	private ContextMenu context;
	private MenuItem miDelete;
	private MenuItem miOrient;
	private MenuItem miGrowVert;
	private MenuItem miShrinkVert;
	private MenuItem miGrowHori;
	private MenuItem miShrinkHori;
	private MenuItem miFilter;
	private Menu miPick;

	//---------------------------------------------------------
	public LayoutGridPane(LayoutGrid input, TemplateController ctrl, int colWidth, int gap, Map<String, PDFPrintElement> elementMap) {
		getStyleClass().add("template-grid");
		this.gap = gap;
		this.colWidth = new SimpleIntegerProperty(colWidth);
		this.input    = new SimpleObjectProperty<LayoutGrid>(input);
		this.elementMap = elementMap;
		this.control  = ctrl;
		setMinHeight(1644);
		setMinWidth(1150);
		
		initComponents();
		setGridLinesVisible(true);
		setVgap(gap);
		setHgap(gap);
		refreshColumns();
		refreshCells();
		setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-effect: dropshadow(one-pass-box, black, 10, 1.0, 3, 3)");
		initInteractivity();
	}

	//---------------------------------------------------------
	private void initComponents() {
		context = new ContextMenu();
		miDelete = new MenuItem(ResourceI18N.get(RES, "context.delete"), null);
		miOrient = new MenuItem(ResourceI18N.get(RES, "context.orientation"), null);
		miGrowVert = new MenuItem(ResourceI18N.get(RES, "context.grow.vertical"), null);
		miShrinkVert = new MenuItem(ResourceI18N.get(RES, "context.shrink.vertical"), null);
		miGrowHori= new MenuItem(ResourceI18N.get(RES, "context.grow.horizontal"), null);
		miShrinkHori = new MenuItem(ResourceI18N.get(RES, "context.shrink.horizontal"), null);
		miPick = new Menu(ResourceI18N.get(RES, "context.pick"), null);
		miFilter = new MenuItem(ResourceI18N.get(RES, "context.filter"), null);
	}

	//---------------------------------------------------------
	/**
	 */
	private void initInteractivity() {
		setOnDragDone(ev -> logger.debug("Drag done"));
		setOnDragDropped(ev -> logger.debug("Drag dropped"));
		this.input.addListener( (ov,o,n) -> {
			refreshColumns();
			refreshCells();
		});
		this.heightProperty().addListener( (ov,o,n) -> {
			logger.info("Size is now "+getWidth()+"x"+getHeight());
		});
		miDelete.setOnAction(ev -> { 
			control.delete(getInput(), ((TemplateCell)context.getUserData()).getContent());
			refreshCells();
		});
		miGrowHori.setOnAction(ev -> {
			control.growHorizontal(getInput(), ((TemplateCell)context.getUserData()).getContent());
			refreshCells();
		});
		miGrowVert.setOnAction(ev -> {
			control.growVertical(getInput(), ((TemplateCell)context.getUserData()).getContent());
			refreshCells();
		});
		miShrinkHori.setOnAction(ev -> {
			control.shrinkHorizontal(getInput(), ((TemplateCell)context.getUserData()).getContent());
			refreshCells();
		});
		miShrinkVert.setOnAction(ev -> {
			control.shrinkVertical(getInput(), ((TemplateCell)context.getUserData()).getContent());
			refreshCells();
		});
		
		miFilter.setOnAction(ev -> {
			logger.debug("filter selected");
			TemplateCell tcell = ((TemplateCell)context.getUserData());
			ElementCell cell = (ElementCell)tcell.getContent();
			List<String> options = cell.getElement().getFilterOptions();
			logger.debug("Filter options of "+cell.getElement().getClass().getSimpleName()+" = "+options);

			ContextMenu ctx = new ContextMenu();
			ctx.setHideOnEscape(true);
			int pos=-1;
			for (String name : options) {
				pos++;
				MenuItem item = new MenuItem(name);
				item.setUserData(pos);
				item.setOnAction(ev2 -> {
					ctx.hide();
					cell.getSavedRenderOptions().setVariantIndex(
						(Integer)((MenuItem)ev2.getSource()).getUserData());
					logger.debug("Picked display variant "+cell.getSavedRenderOptions().getVariantIndex()+" from "+options);
					update();
					});
				ctx.getItems().add(item);
			}
			ctx.setAutoHide(false);
			ctx.show(context, context.getAnchorX()-1, context.getAnchorY()-1);
		});

	}

	//---------------------------------------------------------
	private void refreshColumns() {
		super.getColumnConstraints().clear();
		for (int i=0; i<input.get().getColumnCount(); i++) {
			ColumnConstraints con = new ColumnConstraints(colWidth.get());
			super.getColumnConstraints().add(con);
		}
		logger.info("refreshColumns: "+input.get().getColumnCount()+" columns a "+colWidth.get()+" size");
	}

	//---------------------------------------------------------
	private void addNewLine() {
		TemplateCell[] line = new TemplateCell[input.get().getColumnCount()];
		for (int i=0; i<line.length; i++) {
			line[i] = new TemplateCell(i,lines.size(),this);
			TemplateCell cell = line[i];
			cell.setOnMouseClicked(ev -> mouseClicked(ev, cell));
			this.add(line[i], i, lines.size());
		}
		lines.add(line);
	}

	//---------------------------------------------------------
	private void refreshCells() {
		logger.debug("refreshCells");
		super.getChildren().clear();
		lines.clear();

		for (PrintCell comp : input.get().getComponents()) {
			int x = comp.getX();
			int y = comp.getY();

			// Make sure lines exist
			while (y>=lines.size()) {
				addNewLine();
			}

			switch (comp.getType()) {
			case EMPTY:
				break;
			case ELEMENT:
				ElementCell cell = (ElementCell)comp;
				RenderingParameter param = cell.getSavedRenderOptions().getAsRenderingParameter();
				// Find named component
				PDFPrintElement item = elementMap.get(cell.getElementId());
				if (item==null) {
					// Could not resolve reference

				} else {
					// Render component
					logger.debug("  saved "+cell.getSavedRenderOptions());
					logger.debug("  render "+cell.getElementId()+" with parameter "+param);
					Image img = new Image(new ByteArrayInputStream(item.render(param)));
					logger.info("Width of "+cell.getElementId()+" is "+img.getWidth()+"x"+img.getHeight());
					ImageView iview = new ImageView(img);
					//					iview.setFitWidth(img.getWidth()*0.50);
					//					iview.setPreserveRatio(true);
					cell.setDisplay(iview);

					lines.get(y)[x].setContent(cell);
					GridPane.setColumnSpan(lines.get(y)[x], cell.getWidth());
					for (int i=x+1; i<(x+cell.getWidth()); i++) {
						lines.get(y)[i].setVisible(false);
					}
					//					super.add(iview, x,y, item.getRequiredColumns(),1);
					//					for (int i=x; i<(x+item.getRequiredColumns()); i++) {
					//						lines.get(y)[i] = null;
					//					}
				}
				break;
			case GRID:
				logger.error("Not supported yet: "+comp.getType());
				System.err.println("Not supported yet: "+comp.getType());
				MultiRowCell grid = (MultiRowCell)comp;
				LayoutGridPane gridPane = new LayoutGridPane(
						grid.getInnerGrid(),
						TemplateFactory.newTemplateController(grid.getInnerGrid(), elementMap), 
						colWidth.get(),
						gap,
						elementMap);
				grid.setDisplay(gridPane);
				GridPane.setColumnSpan(lines.get(y)[x], grid.getWidth());
				break;
			}

		}
		addNewLine();

		//		// Now add remaining empty cells
		//		for (int y=0; y<lines.size(); y++) {
		//			for (int x=0; x<lines.get(y).length; x++) {
		//				Node node = lines.get(y)[x]; 
		//				//				logger.debug("Node at "+x+","+y+" = "+node);
		//				if (node!=null) {
		//					super.add(node, x,y);
		//				}
		//			}
		//		}
	}

	//---------------------------------------------------------
	public void update() {
		refreshCells();
	}

	//---------------------------------------------------------
	private void clearCellStates() {
		for (TemplateCell[] line : lines) {
			for (TemplateCell cell : line) {
				if (cell!=null) {
					cell.setPossible(false);
					cell.setImpossible(false);
				}
			}
		}
	}

	//---------------------------------------------------------
	public boolean dragOver(int x, int y, String reference) {
		clearCellStates();
//		logger.warn("dragOver("+x+","+y+","+reference+")");
		PDFPrintElement elem = elementMap.get(reference);
		//		logger.debug("Dragged "+reference+" at "+x+","+y);
		if (elem!=null) {
			boolean possible = control.canBeAdded(input.get(), elem, x, y);
			if (y>=lines.size()) {
				logger.error("Dragged over "+x+","+y+" but only have "+lines.size()+" rows");
				return false;
			}
			TemplateCell[] line = lines.get(y);
			int max = Math.min(input.get().getColumnCount(), x+elem.getRequiredColumns());
			for (int i=x; i<max; i++) {
				if (line[i]!=null) {
					line[i].setPossible(possible);
					line[i].setImpossible(!possible);
				}
			}
			return possible;
		} else {
			try {
				int col = Integer.parseInt(reference);
				boolean possible = control.canBeAddedGrid(input.get(), x, y, col);
				if (y>=lines.size()) {
					logger.error("Dragged over "+x+","+y+" but only have "+lines.size()+" rows");
					return false;
				}
				TemplateCell[] line = lines.get(y);
				int max = Math.min(input.get().getColumnCount(), x+col);
				for (int i=x; i<max; i++) {
					if (line[i]!=null) {
						line[i].setPossible(possible);
						line[i].setImpossible(!possible);
					}
				}
				return possible;
			} catch (NumberFormatException e) {
			}
		}
		
		return false;
	}

	//---------------------------------------------------------
	public void dragDropped(int x, int y, String reference) {
		clearCellStates();
		PDFPrintElement elem = elementMap.get(reference);
		logger.info("Dropped "+reference+"/"+elem+" at "+x+","+y);
		if (elem!=null) {
			control.add(input.get(), elem, x, y);
			Platform.runLater( () -> {
				refreshCells();				
			});
		} else {
			try {
				int col = Integer.parseInt(reference);
				control.add(getInput(),  x, y, col);
				Platform.runLater( () -> {
					refreshCells();				
				});
			} catch (NumberFormatException e) {	
				logger.warn("No width",e);
			}
		}
	}

	//---------------------------------------------------------
	public ObjectProperty<LayoutGrid> inputProperty() { return input; }
	public void setInput(LayoutGrid value) { input.set(value); }
	public LayoutGrid getInput() { return input.get(); }

	//--------------------------------------------------------------------
	public void setBackgroundImage(File file) {
		this.setStyle("-fx-background-size: cover; -fx-background-image: url(\"file:///"+file.getAbsolutePath().replace("\\", "/").replace(" ", "%20")+"\")");
		logger.debug("Background style now: "+this.getStyle());
	}

	//---------------------------------------------------------
	private void mouseClicked(MouseEvent event, TemplateCell templateCell) {
		logger.info("mouseClicked "+event+"  secondary="+event.isSecondaryButtonDown());
		if (event.getButton()==MouseButton.PRIMARY) {
			logger.info("Select");
			// Clear old selection
			if (selected!=null)
				selected.setSelected(false);
			// Make new selection
			if (templateCell!=null && templateCell.getContent()!=null) {
				templateCell.setSelected(true);
			}
		} else
		if (event.getButton()==MouseButton.SECONDARY) {
			logger.info("SHow context");
			PrintCell cell = templateCell.getContent();
			context.getItems().clear();
			context.getItems().addAll(miDelete);
			if (control.canGrowVertical(getInput(), cell))
				context.getItems().add(miGrowVert);
			if (control.canGrowHorizontal(getInput(), cell))
				context.getItems().add(miGrowHori);
			if (control.canShrinkVertical(getInput(), cell))
				context.getItems().add(miShrinkVert);
			if (control.canShrinkHorizontal(getInput(), cell))
				context.getItems().add(miShrinkHori);
			if (control.hasFilter(getInput(), cell))
				context.getItems().add(miFilter);
			if (control.canPick(getInput(), cell)) {
				context.getItems().add(miPick);
				miPick.getItems().clear();
				//
				ElementCell cell2 = (ElementCell)cell;
				logger.debug("Get indices from "+cell2.getElement());
				List<String> options = cell2.getElement().getIndexableObjectNames(character);
				int pos=-1;
				for (String name : options) {
					pos++;
					MenuItem item = new MenuItem(name);
					item.setUserData(pos);
					item.setOnAction(ev2 -> {
						context.hide();
						int index = (Integer)((MenuItem)ev2.getSource()).getUserData();
						logger.info("Selected list index "+index+" for component "+cell2.getElement()+" = "+options.get(index));
						cell2.getSavedRenderOptions().setSelectedIndex(index);
						control.select(getInput(), cell2, index);
						update();
						});
					miPick.getItems().add(item);
				}
			}
			
			context.setUserData(templateCell);
			context.show(LayoutGridPane.this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
		}
	}

	//---------------------------------------------------------
	/**
	 * @param character the character to set
	 */
	public void setCharacter(RuleSpecificCharacterObject character) {
		this.character = character;
		refreshCells();
	}
}
