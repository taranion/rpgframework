/**
 * 
 */
package org.prelle.rpgframework.jfx;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author Stefan
 *
 */
public class CardTile extends Control {

	private static final String DEFAULT_STYLE_CLASS = "card-tile";
	
	public enum Side {
		FRONT,
		BACK;
	}
	
	private StringProperty text;
	private BooleanProperty attentionFlag;
	private ObjectProperty<List<String>> attentionText;
	private ObjectProperty<Parent> backside;
	private ObjectProperty<Side> visibleSide;
    	 
	//--------------------------------------------------------------------
	public CardTile(String id, String i18nTitle) {
		setId(id);
		text          = new SimpleStringProperty(i18nTitle);
		attentionFlag = new SimpleBooleanProperty(false);
		attentionText = new SimpleObjectProperty<>();
		backside      = new SimpleObjectProperty<>();
		visibleSide   = new SimpleObjectProperty<>(Side.FRONT);
		
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
	}

	//--------------------------------------------------------------------
	public StringProperty textProperty() { return text; }
	public String getText() { return text.getValue(); }
	public void setText(String text) { this.text.setValue(text); }
	//--------------------------------------------------------------------
	public BooleanProperty attentionFlagProperty() { return attentionFlag; }
	public boolean getAttentionFlag() { return attentionFlag.getValue(); }
	public void setAttentionFlag(boolean value) { this.attentionFlag.setValue(value); }
	//--------------------------------------------------------------------
	public ObjectProperty<List<String>>  attentionTextProperty() { return attentionText; }
	public List<String> getAttentionText() { return attentionText.getValue(); }
	public void setAttentionText(List<String> text) { this.attentionText.setValue(text); }
	//--------------------------------------------------------------------
	public ObjectProperty<Parent> backsideProperty() { return backside; }
	public Parent getBackside() { return backside.getValue(); }
	public void setBackside(Parent value) { this.backside.setValue(value); }
	//--------------------------------------------------------------------
	public ObjectProperty<Side> visibleSideProperty() { return visibleSide; }
	public Side getVisibleSide() { return visibleSide.getValue(); }
	public void setVisibleSide(Side value) { this.visibleSide.setValue(value); }

	//-------------------------------------------------------------------
	/**
	 * @see javafx.scene.control.Control#createDefaultSkin()
	 */
	@Override
	public Skin<CardTile> createDefaultSkin() {
		return new CardTileSkin(this);
	}

	//--------------------------------------------------------------------
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return CardTile.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };

    //-------------------------------------------------------------------
    /**
     * @see javafx.scene.control.ButtonBase#fire()
     */
    public void fire() {
        if (!isDisabled()) {
            fireEvent(new ActionEvent());
        }
    }
   
}
