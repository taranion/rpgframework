/**
 * 
 */
package org.prelle.rpgframework.jfx.print;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rpgframework.print.ElementCell;
import de.rpgframework.print.PrintCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

/**
 * @author stefa
 *
 */
public class TemplateCell extends StackPane {
	
	private final static Logger logger = LogManager.getLogger("rpgframework.javafx");

	private static PseudoClass EMPTY_PSEUDO_CLASS = PseudoClass.getPseudoClass("empty");
	private static PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
	private static PseudoClass POSSIBLE_PSEUDO_CLASS = PseudoClass.getPseudoClass("possible");
	private static PseudoClass IMPOSSIBLE_PSEUDO_CLASS = PseudoClass.getPseudoClass("impossible");

	private int x,y;
	private LayoutGridPane grid;
	private PrintCell content;
	
	//---------------------------------------------------------
	public TemplateCell(int x, int y, LayoutGridPane grid) {
		this.x    = x;
		this.y    = y;
		this.grid = grid;
		getStyleClass().add("template-cell");
		setEmpty(true);
		
		setOnDragOver(ev -> dragOver(ev));
		setOnDragDropped(ev -> dragDropped(ev));
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
						if (grid.dragOver(x,y, tail)) {
							event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
						}
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

			int pos = enhanceID.indexOf(":");
			if (pos>0) {
				String head = enhanceID.substring(0, pos);
				String tail = enhanceID.substring(pos+1);
				if (head.equals("element")) {
					grid.dragDropped(x, y, tail);
					success = true;
				}
			}
		}
		/* let the source know whether the string was successfully
		 * transferred and used */
		event.setDropCompleted(success);

		event.consume();
	}

	//---------------------------------------------------------
	/**
	 * @param content
	 */
	public void setContent(PrintCell content) {
		this.content = content;
		
		getChildren().clear();
		if (content==null) {
			setEmpty(true);
			return;
		}
		switch (content.getType()) {
		case EMPTY:
			setEmpty(true);
			break;
		case ELEMENT:
			setEmpty(false);
			ImageView iView = (ImageView) ((ElementCell)content).getDisplay();
			getChildren().add(iView);
			break;
		}
	}
	
	//---------------------------------------------------------
	public PrintCell getContent() {
		return content;
	}
}
