package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class Arena extends AnchorPane implements EventHandler<Event> {
	private Grid[][] grids = new Grid[20][15];
	private int prev_row, prev_col;

	private Robot robot;
	private GridPane arena;
	private boolean changeCoordinate;
	
	public Arena() {
		super();

		// initialize the robot icon
		robot = new Robot();
		
		// 
		changeCoordinate = false;
		
		// add the grid to the arena
		arena = new GridPane();

	    for (int row = 0; row < 20; row++) {
	    	for (int col = 0; col < 15; col++) {
			    Grid grid = new Grid(row, col);
			    grid.setMinWidth(30);
			    grid.setMinHeight(30);
			    grid.setOnMouseEntered(this);
			    grid.setOnMouseExited(this);
			    grid.setOnMouseClicked(this);
			    				    
			    arena.add(grid, col, row);
			    grids[row][col] = grid;
		    }
	    }
	    
	    arena.setGridLinesVisible(true);
	    
	    // add it into the arena
	    getChildren().add(arena);
	    getChildren().add(robot);
	}
	
	public boolean isChangeCoordinate() {
		return changeCoordinate;
	}

	public void setChangeCoordinate(boolean changeCoordinate) {
		this.changeCoordinate = changeCoordinate;
		
		robot.setVisible(!changeCoordinate);
	}
	
	public void loadMap(String mdf) {
		int i, index = 1;
		String[] bin = mdf.split("");
		
		for (int row = 0; row < 20; row++) {
	    	for (int col = 0; col < 15; col++) {
	    		i = Integer.parseInt(bin[index++]);
	    		grids[row][col].setFreeSpace((i==0)); 
		    }
	    }
	}
	
	public String generateMapDescriptor() {
		StringBuilder sb = new StringBuilder();
		
		for (int row = 0; row < 20; row++) {
	    	for (int col = 0; col < 15; col++) {
	    	
			    if (grids[row][col].isFreeSpace()) {
			    	sb.append(0);
			    } else {
			    	sb.append(1);
			    }
			    
		    }
	    	
//	    	verify the map descriptor
//	    	sb.append("\n");
	    }
		
		return sb.toString();
	}
	
	public String encodeMapDescriptor() {
		int index = 0;

		String md = generateMapDescriptor();		
		StringBuilder sb = new StringBuilder();
		
		
		while (index < md.length()) {
			sb.append(binaryToHex(md.substring(index, Math.min(index + 4, md.length()))));
            index += 4;
        }
		
		return sb.toString();
	}
	
	public String decodeMapDescriptor(String mdf) {
		int index = 0;
		
		StringBuilder sb = new StringBuilder();
		
		while (index < mdf.length()) {
			sb.append(hexToBinary(mdf.substring(index, Math.min(index + 1, mdf.length()))));
            index += 1;
        }
		
		return sb.toString();
	}
	
	public String hexToBinary(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);

        while (bin.length() < 4) {
            bin = '0' + bin;
        }

        return bin;
    }
	
	public String binaryToHex(String bin) {
        int i = Integer.parseInt(bin, 2);
        String hex = Integer.toString(i, 16);
        
        return hex;
    }

	private void selectCoordinateHover(int row, int col) {
		if ((0 < row && row < 19) && (0 < col  && col < 14)) {
			int leftrow  = row-1;
			int rightrow = row+1;
			int topcol   = col-1;
			int bottomcol= col+1;

			grids[leftrow][col].hover();
			grids[leftrow][topcol].hover();
			grids[leftrow][bottomcol].hover();

			grids[row][col].hover();
			grids[row][topcol].hover();
			grids[row][bottomcol].hover();

			grids[rightrow][col].hover();
			grids[rightrow][topcol].hover();
			grids[rightrow][bottomcol].hover();
		}
	}
	
	private void selectCoordinateUnhover(int row, int col) {
		if ((0 < row && row < 19) && (0 < col  && col < 14)) {
			int leftrow  = row-1;
			int rightrow = row+1;
			int topcol   = col-1;
			int bottomcol= col+1;

			grids[leftrow][col].invalidate();
			grids[leftrow][topcol].invalidate();
			grids[leftrow][bottomcol].invalidate();

			grids[row][col].invalidate();
			grids[row][topcol].invalidate();
			grids[row][bottomcol].invalidate();

			grids[rightrow][col].invalidate();
			grids[rightrow][topcol].invalidate();
			grids[rightrow][bottomcol].invalidate();
		}
	}
	
	@Override
	public void handle(Event event) {
		// TODO Auto-generated method stub
		String evt = event.getEventType().getName();
		Grid grid = (Grid) event.getSource();

		int row = grid.getRowIndex();
		int col = grid.getColumnIndex();

		if (changeCoordinate) {
			if (evt.equals("MOUSE_CLICKED")) {
				if ((0 < row && row < 19) && (0 < col  && col < 14)) {
					selectCoordinateUnhover(row, col);
					
					robot.updatePosition(col-1, row-1);
					
					setChangeCoordinate(false);
				} else {
					System.out.println("out of bound");
				}
				// set robot coordinate
			} else if (evt.equals("MOUSE_ENTERED")) {			
				selectCoordinateHover(row, col);
			} else if (evt.equals("MOUSE_EXITED")) {	
				selectCoordinateUnhover(row, col);
			}
		} else {
			if (evt.equals("MOUSE_CLICKED")) {
				if (grid.isFreeSpace()) {
					grid.setFreeSpace(false);
				} else {
					grid.setFreeSpace(true);
				}
			} 
		}
		
	}
}
