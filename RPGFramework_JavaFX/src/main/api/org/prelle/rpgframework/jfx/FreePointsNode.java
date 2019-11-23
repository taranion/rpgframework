package org.prelle.rpgframework.jfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;



public class FreePointsNode extends Control {

	static final String DEFAULT_STYLE_CLASS = "free-points";

	private static PseudoClass TOSPEND_PSEUDO_CLASS = PseudoClass.getPseudoClass("tospend");
	private static PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

	private FloatProperty points;
	private StringProperty name;

	//-------------------------------------------------------------------
	public FreePointsNode() {
		getStyleClass().add(DEFAULT_STYLE_CLASS);
		points = new SimpleFloatProperty(0);
		name   = new SimpleStringProperty();

		points.addListener( (ov,o,n) -> {
			Float val = (Float)n;
			if (val==null || val==0.0f) {
				toSpend.set(false);
				invalid.set(false);
			} else if (val<0) {
				toSpend.set(false);
				invalid.set(true);
			} else if (val>0) {
				toSpend.set(true);
				invalid.set(false);
			}
		});

		setSkin(new FreePointsNodeSkin(this));
	}

	//-------------------------------------------------------------------
	public FloatProperty pointsProperty() { return points; }
	public void setPoints(float value) { points.set(value); }
	public float getPoints() { return points.get(); }

	//-------------------------------------------------------------------
	public StringProperty nameProperty() { return name; }
	public void setName(String value) { name.set(value); }
	public String getName() { return name.get(); }

	//-------------------------------------------------------------------
	BooleanProperty toSpend = new BooleanPropertyBase(false) {
		public void invalidated() {
			pseudoClassStateChanged(TOSPEND_PSEUDO_CLASS, get());
		}
		@Override public Object getBean() { return FreePointsNode.this; }
		@Override public String getName() { return "tospend"; }
	};
	public void setToSpend(boolean value) {
		this.toSpend.set(value);
	}

	//-------------------------------------------------------------------
	BooleanProperty invalid = new BooleanPropertyBase(false) {
		public void invalidated() {
			pseudoClassStateChanged(INVALID_PSEUDO_CLASS, get());
		}
		@Override public Object getBean() { return FreePointsNode.this; }
		@Override public String getName() { return "invalid"; }
	};
	public void setInvalid(boolean invalid) {
		this.invalid.set(invalid);
	}
}