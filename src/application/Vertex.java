package application;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.awt.List;
import java.util.ArrayList;

public class Vertex {

	
	// the current route\path cost for this tile
	private float cost;
	private int depth;
	
	//a temporary ArrayList to store all the vertices
	private ArrayList tempList = new ArrayList();
	//the parent of this tile, 
	// during the search to find the fastest route
	private Vertex parent;
	// neighbour tile
	private int currentX; // current tile X coordinate
	private int currentY;// current tile Y coordinate
	// the heuristic cost to of this current tile
	private float heuristic;

	private float totalCost;
	
	// constructor: defining properties of a vertex
	public Vertex(int currentX, int currentY) {
		this.currentX = currentX;
		this.currentY = currentY;
		this.cost  = 0;
		this.depth = 0;
		this.totalCost = 0;

		this.heuristic = Integer.MAX_VALUE;
	}

	
	//  @determineParentTile():
	//	Method to determine the parent of the current tile
	public int determineParent(Vertex parent){
		depth = parent.depth + 1;
		this.parent = parent;
		return depth;
	}

	public Vertex getParent() {
		return parent;
	}

	public float getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(float heuristic) {
		this.heuristic = heuristic;
	}

	public float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public int getX() {
		return currentX;
	}

	public int getY() {
		return currentY;
	}
}
