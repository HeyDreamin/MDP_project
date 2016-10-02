package application;

import java.util.*;

public class PathFinder {
/*
	public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;
	private Grid[][] grids;
	private PriorityQueue<Grid> open;
	private boolean closed[][];
	private int startX, startY, goalX, goalY;
	
	public PathFinder(Grid[][] grids, int startX, int startY, int goalX, int goalY) {
		this.grids = grids;
		this.startX = startX;
		this.startY = startY;
		this.goalX = goalX;
		this.goalY = goalY;
	}
	
	public Grid[][] getGrids() {
		return grids;
	}
	
	public void setGrids(Grid[][] grids) {
		this.grids = grids;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public void setStartX(int startX) {
		this.startX = startX;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	public int getGoalX() {
		return goalX;
	}
	
	public void setGoalX(int goalX) {
		this.goalX = goalX;
	}
	
	public int getGoalY() {
		return goalY;
	}
	
	public void setGoalY(int goalY) {
		this.goalY = goalY;
	}
	
	public void updateCost(Grid current, Grid t, int cost) {
		if (t==null || closed[t.getRowIndex()][t.getColumnIndex()])
			return;
		
		int finalCost = t.gethCost() + cost;
		boolean inOpen = open.contains(t);
		if (!inOpen || finalCost<t.getfinalCost()) {
			t.setfinalCost(finalCost);
			t.setGridParent(current);
			if (!inOpen)
				open.add(t);
		}
	}
	
	public void Astar() {
		open.add(grids[startY][startX]);
		Grid current;
		
		
		while (true) {
			current = open.poll();
			if (current==null)
				break;
			closed[current.getRowIndex()][current.getColumnIndex()] = true;
			
			if (current.equals(grids[goalY][goalX]))
				return;
			
			Grid t;
			if (current.getRowIndex() - 1 >= 0) {
				t = grids[current.getRowIndex() - 1][current.getColumnIndex()];
				updateCost(current, t, current.getfinalCost() + V_H_COST);
				
				if (current.getColumnIndex() - 1 >= 0) {
					t = grids[current.getRowIndex() - 1][current.getColumnIndex()];
					updateCost(current, t, current.getfinalCost() + DIAGONAL_COST);
				}
				
				if(current.getColumnIndex() + 1 < 15){
					t = grids[current.getRowIndex()-1][current.getColumnIndex()+1];
					updateCost(current, t, current.getfinalCost()+DIAGONAL_COST);
				}
			}
			
			if(current.getColumnIndex() - 1 >= 0){
				t = grids[current.getRowIndex()][current.getColumnIndex()-1];
				updateCost(current, t, current.getfinalCost()+V_H_COST); 
			}
			
			if(current.getColumnIndex()+1<grids[0].length){
				t = grids[current.getRowIndex()][current.getColumnIndex()+1];
				updateCost(current, t, current.getfinalCost()+V_H_COST);
			}
			
			if(current.getRowIndex()+1<20){
				t = grids[current.getRowIndex()+1][current.getColumnIndex()];
				updateCost(current, t, current.getfinalCost()+V_H_COST);
				
				if(current.getColumnIndex()-1>=0){
					t = grids[current.getRowIndex()+1][current.getColumnIndex()-1];
					updateCost(current, t, current.getfinalCost()+DIAGONAL_COST);
				}
			
				if(current.getColumnIndex()+1<grids[0].length){
					t = grids[current.getRowIndex()+1][current.getColumnIndex()+1];
					updateCost(current, t, current.getfinalCost()+DIAGONAL_COST);
				}
			}
		}
	}
	*/
	
	
}
