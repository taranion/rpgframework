/**
 *
 */
package org.prelle.rpgframework.jfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;

/**
 * @author prelle
 *
 */
public class ThreeColumnPane extends Control {

	private BooleanProperty headersVisible;
	private ObjectProperty<Node> column1Node;
	private ObjectProperty<Node> column2Node;
	private ObjectProperty<Node> column3Node;
	private StringProperty column1Header;
	private StringProperty column2Header;
	private StringProperty column3Header;

	//-------------------------------------------------------------------
	/**
	 */
	public ThreeColumnPane() {
		headersVisible = new SimpleBooleanProperty();
		column1Node    = new SimpleObjectProperty<>();
		column2Node    = new SimpleObjectProperty<>();
		column3Node    = new SimpleObjectProperty<>();
		column1Header  = new SimpleStringProperty();
		column2Header  = new SimpleStringProperty();
		column3Header  = new SimpleStringProperty();

		setSkin(new ThreeColumnPaneFluentSkin(this));
	}

	//--------------------------------------------------------------------
	public BooleanProperty headersVisibleProperty() {return headersVisible;}
	public boolean isHeadersVisible() { return headersVisible.get(); }
	public void setHeadersVisible(boolean val) { headersVisible.set(val); }

	//--------------------------------------------------------------------
	public ObjectProperty<Node> column1NodeProperty() {return column1Node;}
	public Node getColumn1Node() { return column1Node.get(); }
	public void setColumn1Node(Node val) { column1Node.set(val); }

	//--------------------------------------------------------------------
	public ObjectProperty<Node> column2NodeProperty() {return column2Node;}
	public Node getColumn2Node() { return column2Node.get(); }
	public void setColumn2Node(Node val) { column2Node.set(val); }

	//--------------------------------------------------------------------
	public ObjectProperty<Node> column3NodeProperty() {return column3Node;}
	public Node getColumn3Node() { return column3Node.get(); }
	public void setColumn3Node(Node val) { column3Node.set(val); }

	//--------------------------------------------------------------------
	public StringProperty column1HeaderProperty() {return column1Header;}
	public String getColumn1Header() { return column1Header.get(); }
	public void setColumn1Header(String val) { column1Header.set(val); }

	//--------------------------------------------------------------------
	public StringProperty column2HeaderProperty() {return column2Header;}
	public String getColumn2Header() { return column2Header.get(); }
	public void setColumn2Header(String val) { column2Header.set(val); }

	//--------------------------------------------------------------------
	public StringProperty column3HeaderProperty() {return column3Header;}
	public String getColumn3Header() { return column3Header.get(); }
	public void setColumn3Header(String val) { column3Header.set(val); }

}
