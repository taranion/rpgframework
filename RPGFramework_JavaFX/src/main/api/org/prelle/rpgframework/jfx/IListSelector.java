package org.prelle.rpgframework.jfx;

import javafx.scene.Node;
import javafx.scene.control.SelectionModel;

/**
 * @author Stefan Prelle
 *
 */
public interface IListSelector<T> {
	
	public Node getNode();
	
	public SelectionModel<T> getSelectionModel();

}
