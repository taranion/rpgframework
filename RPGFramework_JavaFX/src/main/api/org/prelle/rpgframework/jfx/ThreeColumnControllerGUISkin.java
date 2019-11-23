/**
 *
 */
package org.prelle.rpgframework.jfx;

import de.rpgframework.character.HardcopyPluginData;
import de.rpgframework.genericrpg.ModifyableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * @author prelle
 *
 */
public class ThreeColumnControllerGUISkin<T extends HardcopyPluginData, M extends ModifyableValue<T>>  extends SkinBase<ThreeColumnControllerGUI<T,M>> {

	private ThreeColumnPane content;
	private ListView<T> lvAvailable;
	private ListView<M> lvSelected;
	private Label descTitle;
	private Label descPageRef;
	private Label descText;
	private HBox trash;
	private VBox bxInfo;

	private Label phAvailable;
	private Label phSelected;

	//-------------------------------------------------------------------
	/**
	 * @param control
	 */
	public ThreeColumnControllerGUISkin(ThreeColumnControllerGUI<T,M> control) {
		super(control);

		initComponents();
		initLayout();
		initStyle();
		initInteractivity();

		getChildren().add(content); // Call getControlChildren of SkinBase
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		lvAvailable = new ListView<>();
		lvSelected  = new ListView<>();

		phAvailable = new Label("No content");
		phAvailable.setWrapText(true);
		phSelected = new Label("No content");
		phSelected.setWrapText(true);
		lvAvailable.setPlaceholder(phAvailable);
		lvSelected.setPlaceholder(phSelected);

		descTitle   = new Label();
		descPageRef = new Label();
		descText    = new Label();
		descText.setWrapText(true);
		Region spacing = new Region();
		spacing.setMaxHeight(Double.MAX_VALUE);
		spacing.setMaxWidth(Double.MAX_VALUE);
		VBox.setVgrow(spacing, Priority.ALWAYS);
		trash = new HBox();
		trash.setAlignment(Pos.CENTER);
		trash.setMaxWidth(Double.MAX_VALUE);
		bxInfo = new VBox(20);
		bxInfo.getChildren().addAll(descTitle, descPageRef, descText, spacing, trash);

		content = new ThreeColumnPane();
	}

	//-------------------------------------------------------------------
	private void initStyle() {
		lvAvailable.setId("available");
		lvAvailable.getStyleClass().addAll("content","available");
		lvSelected.getStyleClass().addAll("content","selected");
		lvSelected.setId("selected");
		bxInfo.getStyleClass().addAll("content","info");
		bxInfo.setId("info");

		descTitle.getStyleClass().add("section-head");
		descPageRef.getStyleClass().add("text-secondary");
		descText.getStyleClass().add("text-body");

		content.column1HeaderProperty().bind(getSkinnable().column1HeaderProperty());
		content.column2HeaderProperty().bind(getSkinnable().column2HeaderProperty());
		content.column3HeaderProperty().bind(getSkinnable().column3HeaderProperty());
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		lvAvailable.setStyle("-fx-min-width: 10em");
		lvSelected.setStyle("-fx-min-width: 10em");
		bxInfo.setStyle("-fx-min-width: 10em");

		lvAvailable.setMaxHeight(Double.MAX_VALUE);
		lvSelected .setMaxHeight(Double.MAX_VALUE);
		bxInfo     .setMaxHeight(Double.MAX_VALUE);

		content.setColumn1Node(lvAvailable);
		content.setColumn2Node(lvSelected);
		content.setColumn3Node(bxInfo);
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		content.headersVisibleProperty().bind(getSkinnable().headersVisibleProperty());

		lvAvailable.itemsProperty().bind(getSkinnable().itemsAvailableProperty());
		lvSelected.itemsProperty().bind(getSkinnable().itemsSelectedProperty());
		phAvailable.textProperty().bind(getSkinnable().placeholderAvailableProperty());
		phSelected.textProperty().bind(getSkinnable().placeholderSelectedProperty());

		lvAvailable.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> getSkinnable().selectedProperty().set(n));
		lvSelected.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> {
			if (n!=null)
				getSkinnable().selectedProperty().set(n.getModifyable());
			});
		getSkinnable().selectedProperty().addListener( (ov,o,n) -> {
			if (n==null) {
				descTitle.setText(null);
				descPageRef.setText(null);
				descText.setText(null);
			} else {
				descTitle.setText(n.toString());
				descPageRef.setText(n.getProductName()+" "+n.getPage());
				descText.setText(n.getHelpText());
			}
		});
		getSkinnable().trashAreaProperty().addListener( (ov,o,n) -> {
			trash.getChildren().clear();
			if (n!=null)
				trash.getChildren().add(n);
		});

//		lvAvailable.cellFactoryProperty().bind(getSkinnable().cellFactoryAvailableProperty());
//		lvSelected.cellFactoryProperty().bind(getSkinnable().cellFactorySelectedProperty());

		lvAvailable.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
			public ListCell<T> call(ListView<T> param) {
				ListCell<T> cell = getSkinnable().getCellFactoryAvailable().call(param);
				cell.setOnDragDetected(event -> dragFromAvailableStarted(event, cell));
				cell.setOnDragDropped(event -> dropAtAvailable(event));
				return cell;
			}
		});
		lvSelected.setCellFactory(new Callback<ListView<M>, ListCell<M>>() {
			public ListCell<M> call(ListView<M> param) {
				ListCell<M> cell = getSkinnable().getCellFactorySelected().call(param);
				cell.setOnDragDetected(event -> dragFromSelectedStarted(event, cell));
				cell.setOnDragDropped(event -> dropAtSelected(event));
				return cell;
			}
		});

		lvAvailable.setOnDragOver(event -> dragOver(event));
		lvSelected.setOnDragOver(event -> dragOver(event));
	}

	//-------------------------------------------------------------------
	private void dragFromAvailableStarted(MouseEvent event, ListCell<T> cell) {
		if (getSkinnable().getDragAvailable()!=null)
			getSkinnable().getDragAvailable().accept(event, cell.getItem());
    }

	//-------------------------------------------------------------------
	private void dragFromSelectedStarted(MouseEvent event, ListCell<M> cell) {
		if (getSkinnable().getDragSelected()!=null)
			getSkinnable().getDragSelected().accept(event, cell.getItem());
    }

	//-------------------------------------------------------------------
	private void dragOver(DragEvent event) {
		Node target = (Node) event.getSource();
		if (event.getGestureSource() != target && event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
	}

	//-------------------------------------------------------------------
	private void dropAtAvailable(DragEvent event) {
		if (getSkinnable().getDropAtAvailable()!=null)
			getSkinnable().getDropAtAvailable().accept(event);
    }

	//-------------------------------------------------------------------
	private void dropAtSelected(DragEvent event) {
		if (getSkinnable().getDropAtSelected()!=null)
			getSkinnable().getDropAtSelected().accept(event);
    }

}
