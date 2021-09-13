/**
 *
 */
package org.prelle.rpgframework.jfx;

import org.prelle.javafx.AlertType;
import org.prelle.javafx.CloseType;
import org.prelle.javafx.ManagedScreenPage;
import org.prelle.javafx.ResponsiveControl;
import org.prelle.javafx.SymbolIcon;
import org.prelle.javafx.WindowMode;
import org.prelle.rpgframework.jfx.skin.CharacterDocumentViewPane;

import de.rpgframework.ResourceI18N;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
	protected transient Button descrBtnEdit;
	private transient ScrollPane scroll;
	private transient String currentText;

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
		
		descrBtnEdit = new Button(null,new SymbolIcon("edit"));
//		descrBtnEdit.setVisible(false);
//		descrBtnEdit.setManaged(false);
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		descriptionBX = new VBox(descrHead, descrSubHead, descrBtnEdit, descrNode);
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
		
		descrBtnEdit.setOnAction(ev -> onEditClicked(ev));
	}

	//-------------------------------------------------------------------
	public void onEditClicked(ActionEvent ev) {
		TextArea ta = new TextArea(currentText);
		if (getScreenManager()==null) {
			Button btnOK = new Button("OK");
			Button btnCancel = new Button("Cancel");
			TilePane buttons = new TilePane(10,10, btnOK, btnCancel);
			VBox layout = new VBox(20, ta, buttons);
			
			Scene scene = new Scene(layout);
			Stage stage = new Stage();
			stage.setScene(scene);

			btnOK.setOnAction( ae -> {
				stage.close();
				currentText = ta.getText();
				changeCustomTextTo(currentText);
				if (descrNode instanceof Label) {
					((Label)descrNode).setText(currentText);
				}
			}); 
			btnCancel.setOnAction( ae -> {
				stage.close();
			}); 
			
			stage.showAndWait();
		} else {
			String title = ResourceI18N.get( RPGFrameworkJFXConstants.UI, "dialog.enterCustom.title");
			CloseType closed = getScreenManager().showAlertAndCall(AlertType.QUESTION, title, ta);
			if (closed==CloseType.OK) {
				currentText = ta.getText();
				changeCustomTextTo(currentText);
				if (descrNode instanceof Label) {
					((Label)descrNode).setText(currentText);
				}
			}
		}
	}

	//-------------------------------------------------------------------
	public void changeCustomTextTo(String text) {
		System.err.println("ToDo: overwrite CharacterDocumentView.changeCustomTextTo() in "+this.getClass());
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
		currentText = value;
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
