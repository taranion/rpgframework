package org.prelle.rpgframework.jfx;

import de.rpgframework.character.HardcopyPluginData;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author Stefan Prelle
 *
 */
public class DescriptionPane extends VBox {

	private Label descrHead;
	private Label descrSubHead;
	private Label  descrNode;

	//-------------------------------------------------------------------
	public DescriptionPane() {
		descrHead    = new Label();
		descrHead.getStyleClass().add("title");
		descrSubHead = new Label();
		descrSubHead.getStyleClass().add("subtitle");
		descrNode    = new Label();
		descrHead.setWrapText(true);
		descrSubHead.setWrapText(true);
		descrNode.setWrapText(true);

		ScrollPane scroll = new ScrollPane(descrNode);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(Double.MAX_VALUE);
		scroll.setStyle("-fx-min-height: 30em");
		getChildren().addAll(descrHead, descrSubHead, scroll);
		getStyleClass().add("description-text");
		VBox.setVgrow(scroll, Priority.ALWAYS);
		VBox.setMargin(descrSubHead, new Insets(0,0,20,0));
	}

	//-------------------------------------------------------------------
	public void setText(String title, String pageRef, String descr) {
		this.descrHead.setText(title);
		this.descrSubHead.setText(pageRef);
		this.descrNode.setText(descr);
	}

	//-------------------------------------------------------------------
	public void setText(HardcopyPluginData data) {
		if (data!=null) {
			this.descrHead.setText(data.getName());
			this.descrSubHead.setText(data.getProductName()+" "+data.getPage());
			this.descrNode.setText(data.getHelpText());
		} else {
			this.descrHead.setText(null);
			this.descrSubHead.setText(null);
			this.descrNode.setText(null);
		}
	}

}
