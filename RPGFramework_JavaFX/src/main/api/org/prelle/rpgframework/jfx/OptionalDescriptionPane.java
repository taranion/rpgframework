/**
 * 
 */
package org.prelle.rpgframework.jfx;

import org.prelle.javafx.ResponsiveControl;
import org.prelle.javafx.WindowMode;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author prelle
 *
 */
public class OptionalDescriptionPane extends HBox implements ResponsiveControl {

	private Node description;

	//-------------------------------------------------------------------
	public OptionalDescriptionPane() {
		setStyle("-fx-spacing: 2em");
	}

	//-------------------------------------------------------------------
	public OptionalDescriptionPane(Node content, Node description) {
		this.description = description;

		getChildren().addAll(content, description);
		setAlignment(Pos.TOP_LEFT);
		HBox.setHgrow(content, Priority.ALWAYS);
		HBox.setHgrow(description, Priority.ALWAYS);
		setStyle("-fx-spacing: 2em");
	}

	//-------------------------------------------------------------------
	public void setChildren(Node content, Node description) {
		this.description = description;

		HBox.setHgrow(content, Priority.ALWAYS);
		HBox.setHgrow(description, Priority.ALWAYS);
		getChildren().clear();
		getChildren().addAll(content, description);
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.ResponsiveControl#setResponsiveMode(org.prelle.javafx.WindowMode)
	 */
	@Override
	public void setResponsiveMode(WindowMode value) {
		if (description!=null)  {
			description.setManaged(value==WindowMode.EXPANDED);
			description.setVisible(value==WindowMode.EXPANDED);
		}
	}

	//-------------------------------------------------------------------
	public void setText(String title, String pageRef, String descr) {
		getChildren().retainAll(  getChildren().get(0)  );
		description = new DescriptionPane();
		((DescriptionPane)description).setText(title, pageRef, descr);
		getChildren().addAll(description);
	}

	//-------------------------------------------------------------------
	public void setDescriptionNode(Node description) {
		getChildren().retainAll(  getChildren().get(0)  );
		this.description = description;
		getChildren().addAll(description);
	}
}
