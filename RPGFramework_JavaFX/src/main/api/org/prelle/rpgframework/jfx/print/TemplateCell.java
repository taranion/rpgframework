/**
 * 
 */
package org.prelle.rpgframework.jfx.print;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.Region;

/**
 * @author stefa
 *
 */
public class TemplateCell extends Region {
	
	private final static Logger logger = LogManager.getLogger("rpgframework.javafx");

	private static PseudoClass EMPTY_PSEUDO_CLASS = PseudoClass.getPseudoClass("empty");
	private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
	private static PseudoClass POSSIBLE_PSEUDO_CLASS = PseudoClass.getPseudoClass("possible");
	private static PseudoClass IMPOSSIBLE_PSEUDO_CLASS = PseudoClass.getPseudoClass("impossible");

	private int x,y;
	private LayoutGrid grid;
	
	//---------------------------------------------------------
	public TemplateCell(int x, int y, LayoutGrid grid) {
		this.x    = x;
		this.y    = y;
		this.grid = grid;
		getStyleClass().add("template-cell");
		setEmpty(true);
		
		setOnDragOver(ev -> dragOver(ev));
		setOnDragDropped(ev -> dragDropped(ev));
	}

	//---------------------------------------------------------
	public TemplateCell(int x, int y, LayoutGrid grid, ImageView node) {
		this(x,y, grid);
		getChildren().add(node);
		setEmpty(false);
	}

	/*************************************
	 * Pseudo class EMPTY
	 *************************************/
	BooleanProperty empty = new BooleanPropertyBase(false) {
		public void invalidated() {
			pseudoClassStateChanged(EMPTY_PSEUDO_CLASS, get());
		}
		@Override public Object getBean() { return TemplateCell.this; }
		@Override public String getName() { return "empty"; }
	};
	public void setEmpty(boolean value) {
		this.empty.set(value);
	}

	/*************************************
	 * Pseudo class SELECTED
	 *************************************/
	BooleanProperty selected = new BooleanPropertyBase(false) {
		public void invalidated() {
			pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, get());
		}
		@Override public Object getBean() { return TemplateCell.this; }
		@Override public String getName() { return "selected"; }
	};
	public void setSelected(boolean value) {
		this.selected.set(value);
	}

	/*************************************
	 * Pseudo class POSSIBLE
	 *************************************/
	BooleanProperty possible = new BooleanPropertyBase(false) {
		public void invalidated() {
			pseudoClassStateChanged(POSSIBLE_PSEUDO_CLASS, get());
		}
		@Override public Object getBean() { return TemplateCell.this; }
		@Override public String getName() { return "possible"; }
	};
	public void setPossible(boolean value) {
		this.possible.set(value);
	}

	/*************************************
	 * Pseudo class IMPOSSIBLE
	 *************************************/
	BooleanProperty impossible = new BooleanPropertyBase(false) {
		public void invalidated() {
			pseudoClassStateChanged(IMPOSSIBLE_PSEUDO_CLASS, get());
		}
		@Override public Object getBean() { return TemplateCell.this; }
		@Override public String getName() { return "impossible"; }
	};
	public void setImpossible(boolean value) {
		this.impossible.set(value);
	}
	//--------------------------------------------------------------------
	private void dragOver(DragEvent event) {
		Node target = (Node) event.getSource();
		try {
			if (event.getGestureSource() != target && event.getDragboard().hasString()) {
				String enhanceID = event.getDragboard().getString();

				int pos = enhanceID.indexOf(":");
				if (pos>0) {
					String head = enhanceID.substring(0, pos);
					String tail = enhanceID.substring(pos+1);
					if (head.equals("element")) {
						grid.dragOver(x,y, tail);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event.consume();
	}
	
	//--------------------------------------------------------------------
	private void dragDropped(DragEvent event) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (db.hasString()) {
			String enhanceID = db.getString();
			logger.debug("Dropped "+enhanceID);

			int pos = enhanceID.indexOf(":");
			if (pos>0) {
				String head = enhanceID.substring(0, pos);
				String tail = enhanceID.substring(pos+1);
				if (head.equals("element")) {
					grid.dragDropped(x, y, tail);
				}
			}
		}
		/* let the source know whether the string was successfully
		 * transferred and used */
		event.setDropCompleted(success);

		event.consume();
	}

}
