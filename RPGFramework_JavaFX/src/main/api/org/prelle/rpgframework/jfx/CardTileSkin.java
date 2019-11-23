/**
 * 
 */
package org.prelle.rpgframework.jfx;

import org.prelle.javafx.AttentionPane;
import org.prelle.javafx.FlipControl;
import org.prelle.rpgframework.jfx.CardTile.Side;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;

/**
 * @author prelle
 *
 */
public class CardTileSkin extends SkinBase<CardTile> {

	private AttentionPane attention;
	private FlipControl flip;
	private Label       front;
	private Label       backTitle;
	private VBox		back;
	
	//-------------------------------------------------------------------
	/**
	 * @param control
	 */
	public CardTileSkin(CardTile control) {
		super(control);
		initComponents();
		initLayout();
		initStyle();
		initInteractivity();
		
		getChildren().add(attention);
		updateVisible();
	}

	//-----------------------------------------------------------------
	private void initComponents() {
		flip = new FlipControl(Orientation.VERTICAL, true);
		flip.setId(getSkinnable().getId());
		front= new Label(getSkinnable().getText());
		front.setWrapText(true);
		front.setAlignment(Pos.TOP_LEFT);

		backTitle = new Label(getSkinnable().getText());
		back = new VBox();
		
		attention = new AttentionPane(flip);
	}

	//-----------------------------------------------------------------
	private void initLayout() {
		front.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		back.getChildren().add(backTitle);
		if (getSkinnable().getBackside()!=null) {
			back.getChildren().add(getSkinnable().getBackside());
		}
		
		flip.getItems().addAll(front, back);
		flip.setVisibleIndex(0);
	}

	//-----------------------------------------------------------------
	private void initStyle() {
		front.getStyleClass().addAll("text-small-subheader", "front");
		back.getStyleClass().addAll("back");
		backTitle.getStyleClass().add("section-head");
	}

	//-----------------------------------------------------------------
	private void initInteractivity() {
		getSkinnable().textProperty().addListener((ov,o,n) -> front.setText(n));
		getSkinnable().backsideProperty().addListener((ov,o,n) -> {
			if (o!=null)
				flip.getItems().remove(o);
			flip.getItems().add(n);
		});
		getSkinnable().attentionFlagProperty().addListener((ov,o,n) -> attention.setAttentionFlag(n));
		getSkinnable().attentionTextProperty().addListener((ov,o,n) -> attention.setAttentionToolTip(n));
		getSkinnable().visibleSideProperty().addListener( (ov,o,n) -> {
			updateVisible();
		});
		flip.visibleIndexProperty().addListener( (ov,o,n) -> {
			if ((Integer)n==0) getSkinnable().setVisibleSide(Side.FRONT);
			if ((Integer)n==1) getSkinnable().setVisibleSide(Side.BACK);
		});
		
		// React to mouse clicks
		flip.setOnMouseClicked(event -> getSkinnable().fire());
		
		// Pass drag & drop events from FlipControl
		flip.onDragDetectedProperty().bind(getSkinnable().onDragDetectedProperty());
		flip.onDragEnteredProperty ().bind(getSkinnable().onDragEnteredProperty());
		flip.onDragOverProperty    ().bind(getSkinnable().onDragOverProperty());
		flip.onDragExitedProperty  ().bind(getSkinnable().onDragExitedProperty());
		flip.onDragDroppedProperty ().bind(getSkinnable().onDragDroppedProperty());
	}
	//-----------------------------------------------------------------
	private void updateVisible() {
		switch (getSkinnable().getVisibleSide()) {
		case FRONT: 
			if (flip.getVisibleIndex()!=0)
				flip.flip();
			break;
		case BACK:
			if (flip.getVisibleIndex()!=1)
				flip.flip();
			break;
		}
	}

}
