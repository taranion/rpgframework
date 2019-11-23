/**
 *
 */
package org.prelle.rpgframework.jfx;

import org.prelle.javafx.ManagedScreenPage;
import org.prelle.javafx.ResponsiveControl;
import org.prelle.javafx.WindowMode;
import org.prelle.rpgframework.jfx.skin.CharacterDocumentViewPane;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author prelle
 *
 */
public class CharacterDocumentView extends ManagedScreenPage implements ResponsiveControl, IHasDescriptionPane {

	public static final String DEFAULT_STYLE_CLASS = "character-document-view";

	@FXML
	private ObservableList<Section> sectionListProperty = FXCollections.observableArrayList();
	@FXML
	private FloatProperty pointsFreeProperty = new SimpleFloatProperty();
	@FXML
	private StringProperty pointsNameProperty = new SimpleStringProperty();


	private transient CharacterDocumentViewPane pane;
	private transient Label descrHead;
	private transient Label descrSubHead;
	private transient Node  descrNode;
	private transient VBox  descriptionBX;
	private transient ScrollPane scroll;

	//-------------------------------------------------------------------
	public CharacterDocumentView() {
		super("Title not set");
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		//		setSkin(new CharacterDocumentViewSkin(this));
		//		setStyle("-fx-background-color: #808080; -fx-border-width: 5px; -fx-border-color: red;");

		initComponents();
		initLayout();
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		pane = new CharacterDocumentViewPane();
		descrHead    = new Label();
		descrHead.getStyleClass().add("title");
		descrSubHead = new Label();
		descrSubHead.getStyleClass().add("subtitle");
		descrNode    = new Label();
		descrHead.setWrapText(true);
		descrSubHead.setWrapText(true);
		((Label)descrNode).setWrapText(true);
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		descriptionBX = new VBox(descrHead, descrSubHead, descrNode);
		descriptionBX.getStyleClass().add("description-text");
		VBox.setVgrow(descrNode, Priority.ALWAYS);
		VBox.setMargin(descrSubHead, new Insets(0,0,20,0));
		VBox dummy = new VBox(descriptionBX);
		dummy.getStyleClass().add(CharacterDocumentView.DEFAULT_STYLE_CLASS+"-description");
		OptionalDescriptionPane paneWithDescr = new OptionalDescriptionPane(pane, dummy);

		setContent(paneWithDescr);
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		sectionListProperty.addListener(new ListChangeListener<Section>() {

			@Override
			public void onChanged(Change<? extends Section> c) {
				while (c.next()) {
					if (c.wasAdded())
						pane.getSectionList().addAll(c.getAddedSubList());
					if (c.wasRemoved())
						pane.getSectionList().removeAll(c.getRemoved());
					//					pane.getSectionList().clear();
					//					pane.getSectionList().addAll(sectionListProperty);
				}
			}});
		pane.pointsFreeProperty().bind(pointsFreeProperty);
		pane.pointsNameProperty().bind(pointsNameProperty);
	}

	//-------------------------------------------------------------------
	public ObservableList<Section> getSectionList() { return sectionListProperty; }

	//-------------------------------------------------------------------
	public FloatProperty pointsFreeProperty() { return pointsFreeProperty; }
	public void setPointsFree(float value) { pointsFreeProperty.set(value); }
	public float getPointsFree() { return pointsFreeProperty.get(); }

	//-------------------------------------------------------------------
	public StringProperty pointsNameProperty() { return pointsNameProperty; }
	public void setPointsNameProperty(String value) { pointsNameProperty.set(value); }
	public String getPointsNameProperty() { return pointsNameProperty.get(); }

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.ResponsiveControl#setResponsiveMode(org.prelle.javafx.WindowMode)
	 */
	@Override
	public void setResponsiveMode(WindowMode value) {
		pane.setResponsiveMode(value);
	}

	//-------------------------------------------------------------------
	public void jumpTo(Section section) {

	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.IHasDescriptionPane#setDescriptionHeading(java.lang.String)
	 */
	@Override
	public void setDescriptionHeading(String value) {
		descrHead.setText(value);
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.IHasDescriptionPane#setDescriptionPageRef(java.lang.String)
	 */
	@Override
	public void setDescriptionPageRef(String value) {
		descrSubHead.setText(value);
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.IHasDescriptionPane#setDescriptionNode(javafx.scene.Node)
	 */
	@Override
	public void setDescriptionNode(Node value) {
		descriptionBX.getChildren().remove(descrNode);
		descriptionBX.getChildren().remove(scroll);
		descrNode    = value;
		if (value!=null) {
			VBox.setVgrow(descrNode, Priority.ALWAYS);
			scroll = new ScrollPane(descrNode);
			scroll.setFitToWidth(true);
			scroll.setMaxHeight(Double.MAX_VALUE);
			scroll.setStyle("-fx-pref-height: 50em");
			VBox.setVgrow(scroll, Priority.ALWAYS);
			descriptionBX.getChildren().add(scroll);
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.rpgframework.jfx.IHasDescriptionPane#setDescriptionText(java.lang.String)
	 */
	@Override
	public void setDescriptionText(String value) {
		descriptionBX.getChildren().remove(descrNode);
		descriptionBX.getChildren().remove(scroll);
		descrNode    = new Label(value);
		VBox.setVgrow(descrNode, Priority.ALWAYS);
		((Label)descrNode).setWrapText(true);
		scroll = new ScrollPane(descrNode);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(Double.MAX_VALUE);
		scroll.setStyle("-fx-pref-height: 50em");
		VBox.setVgrow(scroll, Priority.ALWAYS);
		descriptionBX.getChildren().add(scroll);
	}

}
