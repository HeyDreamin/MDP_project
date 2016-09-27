package application;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class Grid extends Label {
	public static BackgroundFill FILL_FREESPACE = new BackgroundFill(Color.ALICEBLUE, null, null);
	public static BackgroundFill FILL_WALL = new BackgroundFill(Color.BLACK, null, null);
	public static BackgroundFill FILL_HOVER = new BackgroundFill(Color.ORANGE, null, null);
	public static Background BG_FREESPACE = new Background(FILL_FREESPACE);
	public static Background BG_WALL = new Background(FILL_WALL);
	public static Background BG_HOVER = new Background(FILL_HOVER);
	public static BackgroundFill FILL_START = new BackgroundFill(Color.RED, null, null);
	public static Background BG_START = new Background(FILL_START);
	public static BackgroundFill FILL_GOAL = new BackgroundFill(Color.GREEN, null, null);
	public static Background BG_GOAL = new Background(FILL_GOAL);	

	private Status status;
	private boolean explored;
	private boolean freeSpace;
	
	private int row, col;
	
	public Grid(int row, int col) {
		setFreeSpace(true);
		explored = false;
		this.row = row;
		this.col = col;
	}
	
	public int getRowIndex() {
		return row;
	}
	
	public int getColumnIndex() {
		return col;
	}

	public void setStart() {
		setBackground(BG_START);
	}
	
	public void setGoal() {
		setBackground(BG_GOAL);
	}
	
	public boolean isFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(boolean freeSpace) {
		this.freeSpace = freeSpace;
		invalidate();
	}
	
	public void hover() {
		setBackground(BG_HOVER);
	}
	
	public void invalidate() {
		if (freeSpace) {
			setBackground(BG_FREESPACE);
		} else {
			setBackground(BG_WALL);
		}
	}
	
	public enum Status {
		FREE_SPACE, WALL, UNKNOWN
	}
}
