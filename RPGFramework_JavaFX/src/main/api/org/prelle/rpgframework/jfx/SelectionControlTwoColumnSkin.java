/**
 * 
 */
package org.prelle.rpgframework.jfx;

import java.util.List;

import de.rpgframework.genericrpg.SelectableItem;
import de.rpgframework.genericrpg.SelectedValue;
import de.rpgframework.genericrpg.SelectionController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * @author prelle
 *
 */
public class SelectionControlTwoColumnSkin<T extends SelectableItem, V extends SelectedValue<T>> extends SkinBase<SelectionControllerNode<T,V>> {

	private ListView<T> listPossible;
	private ListView<V> listSelected;
	
	private Label lblAvailable;
	private Label lblSelected;
	
	private Callback<ListView<T>, ListCell<T>> cfAvailable;
	private Callback<ListView<V>, ListCell<V>> cfSelected;
	
	private HBox columns;
	
	//-------------------------------------------------------------------
	protected SelectionControlTwoColumnSkin(SelectionControllerNode<T,V> control) {
		super(control);
		initComponents();
		initLayout();
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		cfAvailable = new Callback<ListView<T>, ListCell<T>>() {
			public ListCell<T> call(ListView<T> param) {
				ListCell<T> cell = (getSkinnable().getAvailableCellFactory()!=null)?getSkinnable().getAvailableCellFactory().call(param):createDefaultCellImpl();
				cell.setOnMouseClicked(ev -> mouseClickedAvailable(cell, ev));
				cell.setOnDragDetected(ev -> dragDetectedAvailable(cell, ev));
				return cell;
			}
		};
		cfSelected = new Callback<ListView<V>, ListCell<V>>() {
			public ListCell<V> call(ListView<V> param) {
				ListCell<V> cell = (getSkinnable().getSelectedCellFactory()!=null)?getSkinnable().getSelectedCellFactory().call(param):createDefaultCellImpl();
				cell.setOnMouseClicked(ev -> mouseClickedSelected(cell, ev));
				cell.setOnDragDetected(ev -> dragDetectedSelected(cell, ev));
				return cell;
			}
		};
		
		listPossible = new ListView<T>();
		listPossible.setCellFactory(cfAvailable);
		listPossible.itemsProperty().bind(getSkinnable().availableProperty());
		
		listSelected = new ListView<V>();
		listSelected.setCellFactory(cfSelected);
		listSelected.itemsProperty().bind(getSkinnable().selectedProperty());
		
		lblAvailable = new Label(" "+getSkinnable().getAvailableHeading());
		lblSelected  = new Label(" "+getSkinnable().getSelectedHeading());
		lblAvailable.getStyleClass().addAll("text-small-subheader","list-heading");
		lblSelected.getStyleClass().addAll("text-small-subheader","list-heading");
		listPossible.setStyle(getSkinnable().getAvailableStyle());
		listSelected.setStyle(getSkinnable().getSelectedStyle());
		
		listPossible.setMaxHeight(Double.MAX_VALUE);

		
		columns = new HBox();
		columns.setStyle("-fx-spacing: 1em;");
		getChildren().add(columns);
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		columns.getChildren().clear();
		
		if (getSkinnable().getShowHeadings()) {
			VBox col1 = new VBox(lblAvailable, listPossible);
			VBox col2 = new VBox(lblSelected, listSelected);
			col1.setStyle("-fx-spacing: 1em");
			col2.setStyle("-fx-spacing: 1em");
			VBox.setVgrow(listPossible, Priority.ALWAYS);
			VBox.setVgrow(listSelected, Priority.ALWAYS);
			columns.getChildren().addAll(col1, col2);
		} else {
			columns.getChildren().addAll(listPossible, listSelected);
		}
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
	
//		getSkinnable().availableCellFactoryProperty().addListener( (ov,o,n) -> listPossible.setCellFactory(n));
//		getSkinnable().selectedCellFactoryProperty().addListener( (ov,o,n) -> listSelected.setCellFactory(n));
		
		getSkinnable().showHeadingsProperty().addListener( (ov,o,n) -> initLayout());
		
		getSkinnable().availableHeadingProperty().addListener( (ov,o,n) -> lblAvailable.setText(" "+n));
		getSkinnable().selectedHeadingProperty().addListener( (ov,o,n) -> lblSelected.setText(" "+n));
		getSkinnable().availableStyleProperty().addListener( (ov,o,n) -> listPossible.setStyle(n));
		getSkinnable().selectedStyleProperty().addListener( (ov,o,n) -> listSelected.setStyle(n));
		
		listPossible.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> getSkinnable().setShowHelpFor(n));
		listSelected.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> getSkinnable().setShowHelpFor((n!=null)?n.getModifyable():null));
		listPossible.setOnDragOver(ev -> dragOverAvailable(ev));
		listSelected.setOnDragOver(ev -> dragOverSelected(ev));
		listPossible.setOnDragDropped(ev -> dragDroppedAvailable(ev));
		listSelected.setOnDragDropped(ev -> dragDroppedSelected(ev));
	}

    //-------------------------------------------------------------------
    private static <T> ListCell<T> createDefaultCellImpl() {
        return new ListCell<T>() {
            @Override public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof Node) {
                    setText(null);
                    Node currentNode = getGraphic();
                    Node newNode = (Node) item;
                    if (currentNode == null || ! currentNode.equals(newNode)) {
                        setGraphic(newNode);
                    }
                } else {
                    /**
                     * This label is used if the item associated with this cell is to be
                     * represented as a String. While we will lazily instantiate it
                     * we never clear it, being more afraid of object churn than a minor
                     * "leak" (which will not become a "major" leak).
                     */
                    setText(item == null ? "null" : item.toString());
                    setGraphic(null);
                }
            }
        };
    }

	//-------------------------------------------------------------------
    private void userSelects(T toSelect) {
    	System.out.println("userSelects("+toSelect+")");
    	SelectionController<T, V> ctrl = getSkinnable().getController();
    	if (ctrl.canBeSelected(toSelect)) {
    		// Is there a need for a selection
    		if (ctrl.needsOptionSelection(toSelect)) {
    			// Yes, user must choose
    			List<?> options = ctrl.getOptions(toSelect);
    		   	System.out.println("called getOptions");
    			if (getSkinnable().getOptionCallback()!=null) {
    				Platform.runLater( () -> {
    	    		   	System.out.println("call getOptionCallback");
    					Object choice = getSkinnable().getOptionCallback().apply(toSelect, options);
    					if (choice!=null) {
    		    		   	System.out.println("call select(option)");
    						ctrl.select(toSelect, choice);
    					}
    				});
    			}
    		} else {
    			// No
    		   	System.out.println("call select before: "+getSkinnable().getSelected());
    		   	V selected = ctrl.select(toSelect);
       		   	System.out.println("call select after: "+getSkinnable().getSelected());
    		   	if (selected!=null) {
    		   		listSelected.getItems().add(selected);
//    		   		listSelected.getItems().setAll(getSkinnable().getSelected());
    		   	}
    		}
    	} else {
    		System.out.println("can not be Selected("+toSelect+")");
    	}
    }

	//-------------------------------------------------------------------
	private void mouseClickedAvailable(ListCell<T> cell, MouseEvent ev) {
		if (ev.getClickCount()==2) {
			userSelects(cell.getItem());
		}
	}

	//-------------------------------------------------------------------
	private void mouseClickedSelected(ListCell<V> cell, MouseEvent ev) {
		if (ev.getClickCount()==2) {
			if (getSkinnable().getController().canBeDeselected((V)cell.getItem())) {
				getSkinnable().getController().deselect((V)cell.getItem());
			}
		}
	}

	//-------------------------------------------------------------------
	protected void dragDetectedAvailable(ListCell<T> cell, MouseEvent event) {
		T data = cell.getItem();
		if (data==null)
			return;
		if (!getSkinnable().getController().canBeSelected(data))
			return;

		Node source = (Node) event.getSource();

		/* drag was detected, start a drag-and-drop gesture*/
        /* allow any transfer mode */
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        String id = data.getTypeId()+":"+data.getId();
        content.putString(id);
        db.setContent(content);

        /* Drag image */
        WritableImage snapshot = source.snapshot(new SnapshotParameters(), null);
        db.setDragView(snapshot);

        event.consume();
	}

	//-------------------------------------------------------------------
	protected void dragDetectedSelected(ListCell<V> cell, MouseEvent event) {
		V data = cell.getItem();
		if (data==null)
			return;
		if (!getSkinnable().getController().canBeDeselected(data))
			return;

		Node source = (Node) event.getSource();

		/* drag was detected, start a drag-and-drop gesture*/
        /* allow any transfer mode */
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        String id = data.getModifyable().getTypeId()+":"+data.getModifyable().getId();
        content.putString(id);
        db.setContent(content);

        /* Drag image */
        WritableImage snapshot = source.snapshot(new SnapshotParameters(), null);
        db.setDragView(snapshot);

        event.consume();
	}

	//-------------------------------------------------------------------
	private void dragOverSelected(DragEvent event) {
		Node target = (Node) event.getSource();
		if (event.getGestureSource() != target && event.getDragboard().hasString()) {
            String enhanceID = event.getDragboard().getString();
            // Find from available
            T toSelect = null;
            for (T tmp : getSkinnable().getController().getAvailable()) {
            	String cmp = tmp.getTypeId()+":"+tmp.getId();
            	if (enhanceID.equals(cmp)) {
            		toSelect = tmp;
            		break;
            	}
            }
            if (toSelect!=null && getSkinnable().getController().canBeSelected(toSelect)) {
            	/* allow for both copying and moving, whatever user chooses */
            	event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
        }
	}

	//-------------------------------------------------------------------
	private void dragDroppedSelected(DragEvent event) {
       /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            String enhanceID = db.getString();
            // Find from available
            T toSelect = null;
            for (T tmp : getSkinnable().getController().getAvailable()) {
            	String cmp = tmp.getTypeId()+":"+tmp.getId();
            	if (enhanceID.equals(cmp)) {
            		toSelect = tmp;
            		break;
            	}
            }
            if (toSelect!=null) {
            	userSelects(toSelect);
            }
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);

        event.consume();
	}

	//-------------------------------------------------------------------
	private void dragOverAvailable(DragEvent event) {
		Node target = (Node) event.getSource();
		if (event.getGestureSource() != target && event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
	}

	//-------------------------------------------------------------------
	private void dragDroppedAvailable(DragEvent event) {
       /* if there is a string data on dragboard, read it and use it */
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            String enhanceID = db.getString();
            // Find from available
            V toSelect = null;
            for (V tmp : getSkinnable().getController().getSelected()) {
            	String cmp = tmp.getModifyable().getTypeId()+":"+tmp.getModifyable().getId();
            	if (enhanceID.equals(cmp)) {
            		toSelect = tmp;
            		break;
            	}
            }
            if (toSelect!=null) {
            	getSkinnable().getController().deselect(toSelect);
            	getSkinnable().refresh();
            }
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        event.setDropCompleted(success);

        event.consume();
	}


}
