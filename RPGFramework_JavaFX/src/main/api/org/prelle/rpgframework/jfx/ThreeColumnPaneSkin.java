/**
 *
 */
package org.prelle.rpgframework.jfx;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author prelle
 *
 */
public class ThreeColumnPaneSkin extends SkinBase<ThreeColumnPane> {

	private HBox content;
	private VBox column1;
	private VBox column2;
	private VBox column3;

	private Label header1;
	private Label header2;
	private Label header3;

	//-------------------------------------------------------------------
	/**
	 * @param control
	 */
	public ThreeColumnPaneSkin(ThreeColumnPane control) {
		super(control);
		initComponents();
		initStyle();
		initLayout();
		initInteractivity();

		getChildren().add(content); // Call getControlChildren of SkinBase
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		column1 = new VBox();
		column2 = new VBox();
		column3 = new VBox();

		header1 = new Label();
		header2 = new Label();
		header3 = new Label();

		content = new HBox();
	}

	//-------------------------------------------------------------------
	private void initStyle() {
		header1.getStyleClass().addAll("text-subheader","section-head");
		header2.getStyleClass().addAll("text-subheader","section-head");
		header3.getStyleClass().addAll("text-subheader","section-head");

		column1.getStyleClass().add("column1");
		column2.getStyleClass().add("column2");
		column3.getStyleClass().add("column3");

		content.getStyleClass().add("three-column-pane");
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		content.setSpacing(40);
		content.setMaxWidth(Double.MAX_VALUE);

		column1.setSpacing(20);
		column2.setSpacing(20);
		column3.setSpacing(20);

		column1.setMaxHeight(Double.MAX_VALUE);
		column2.setMaxHeight(Double.MAX_VALUE);
		column3.setMaxHeight(Double.MAX_VALUE);

		content.getChildren().addAll(column1, column2, column3);
		content.setMaxHeight(Double.MAX_VALUE);

		HBox.setHgrow(column1, Priority.SOMETIMES);
		HBox.setHgrow(column2, Priority.SOMETIMES);
		HBox.setHgrow(column3, Priority.ALWAYS);
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		header1.textProperty().bind(getSkinnable().column1HeaderProperty());
		header2.textProperty().bind(getSkinnable().column2HeaderProperty());
		header3.textProperty().bind(getSkinnable().column3HeaderProperty());

		getSkinnable().headersVisibleProperty().addListener( (ov,o,n) -> {
			if (n) {
				column1.getChildren().add(0, header1);
				column2.getChildren().add(0, header2);
				column3.getChildren().add(0, header3);
			} else {
				column1.getChildren().remove(header1);
				column2.getChildren().remove(header2);
				column3.getChildren().remove(header3);
			}
		});

		getSkinnable().column1NodeProperty().addListener( (ov,o,n) -> {
			if (o!=null) { column1.getChildren().remove(o); }
			if (n!=null) {
				column1.getChildren().add(n);
				VBox.setVgrow(n, Priority.ALWAYS);
			}
		});
		getSkinnable().column2NodeProperty().addListener( (ov,o,n) -> {
			if (o!=null) { column2.getChildren().remove(o); }
			if (n!=null) {
				column2.getChildren().add(n);
				VBox.setVgrow(n, Priority.ALWAYS);
			}
		});
		getSkinnable().column3NodeProperty().addListener( (ov,o,n) -> {
			if (o!=null) { column3.getChildren().remove(o); }
			if (n!=null) {
				column3.getChildren().add(n);
				VBox.setVgrow(n, Priority.ALWAYS);
			}
		});
	}

}
