package org.prelle.rpgframework.jfx;

import de.rpgframework.genericrpg.ToDoElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;

public abstract class Section extends Control {

	public enum SectionType {
		SINGLE,
		LEFT,
		RIGHT,
	}
	
	protected ObservableList<ToDoElement> todos = FXCollections.observableArrayList();
	
	//-------------------------------------------------------------------
	public ObservableList<ToDoElement> getToDoList() {
		return todos;
	}
	
	//-------------------------------------------------------------------
	public abstract void refresh();
	
}