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
	public static BackgroundFill FILL_UNKNOWN = new BackgroundFill(Color.DARKGRAY, null, null);
	public static Background BG_UNKNOWN = new Background(FILL_UNKNOWN);	

	public Status status;
	
	private int row, col, hCost, fianlCost;
	private Grid parent;
	
	public Grid(int row, int col) {
		//setFreeSpace(true);
		setUnknown();
		this.row = row;
		this.col = col;
		this.hCost = Math.abs(1 - row) + Math.abs(13 - col);
	}
	
	public void setGridParent(Grid parent) {
		this.parent = parent;
	}
	
	public Grid getGridParent() {
		return parent;
	}
	
	public int getRowIndex() {
		return row;
	}
	
	public int getColumnIndex() {
		return col;
	}

	public void setStart() {
		status = Status.START;
		setBackground(BG_START);
	}
	
	public void setGoal() {
		status = Status.GOAL;
		setBackground(BG_GOAL);
	}
	
	public boolean isUnknown() {
		return status==Status.UNKNOWN;
	}
	
	public void setUnknown() {
		status = Status.UNKNOWN;
		setBackground(BG_UNKNOWN);
	}

	public boolean isFreeSpace() {
		return status==Status.FREE_SPACE||status==Status.START||status==Status.GOAL;
	}

	public void setFreeSpace() {
		if (status==Status.START || status==Status.GOAL)
			return;
		status = Status.FREE_SPACE;
		setBackground(BG_FREESPACE);
	}
	
	public boolean isWall() {
		return status==Status.WALL;
	}
	
	public void setWall() {
		status = Status.WALL;
		setBackground(BG_WALL);
	}
	
	public boolean isRobot() {
		return status==Status.ROBOT;
	}
	
	public void setRobot() {
		status = Status.ROBOT;
		//setBackground(BG_ROBOT);
	}
	
	public void hover() {
		setBackground(BG_HOVER);
	}
	
	public void validate() {
		switch (status) {
			case UNKNOWN:
				setBackground(BG_UNKNOWN);
				break;
			case WALL:
				setBackground(BG_WALL);
				break;
			case FREE_SPACE:
				setBackground(BG_FREESPACE);
				break;
			case GOAL:
				setBackground(BG_GOAL);
				break;
			case START:
				setBackground(BG_START);
				break;
			default:
				break;
		}
	}
	
	public enum Status {
		FREE_SPACE, WALL, UNKNOWN, GOAL, START, ROBOT
	}

	public void sethCost(int hCost) {
		this.hCost = hCost;
	}
	
	public int gethCost() {
		return hCost;
	}
	
	public void setfinalCost(int finalCost) {
		this.fianlCost = finalCost;
	}
	
	public int getfinalCost() {
		return fianlCost;
	}
}