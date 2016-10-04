package application;

import java.util.*;

/**
 * Created by JCHANG007 on 30/9/16.
 */
public class AStar {
    private Robot robot;
    private Grid[][] grids;

    private Vertex[][] vertex;
    private Set<Vertex> openList;
    private Set<Vertex> closeList;
    private ArrayList<Vertex> shortestPath;
    private PriorityQueue<Vertex> pQueue = new PriorityQueue<>(150, new VertexComparator());

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public AStar(Grid[][] grids, Robot robot, int startX, int startY, int endX, int endY) {
        this.grids = grids;
        this.robot = robot;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        
        vertex = new Vertex[20][15];

        openList  = new HashSet<>();
        closeList = new HashSet<>();

        shortestPath = new ArrayList<>();

        init(endX,endY);
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

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public void init(int endX, int endY) {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 15; col++) {
                Vertex v = null;

                if (checkStatus(grids[row][col].status)
                		&& isValidVertex(col, row)) {
                    v = new Vertex(col, row);
                    v.setHeuristic(Heuristic.compute(col, row, endX, endY));
                }

                vertex[row][col] = v;
            }
        }
    }
    
    public boolean checkStatus(Grid.Status status) {
    	switch (status) {
    		case FREE_SPACE:
    		case START:
    		case GOAL:
    			return true;
    		default:
    			return false;
    	}    	
    }

    public void reset() {
        openList.clear();
        closeList.clear();
        pQueue.clear();
        shortestPath.clear();
    }

    public void compute(int fromX, int fromY, int toX, int toY) {
        reset();

        Vertex source = vertex[fromY][fromX];
        Vertex target = vertex[toY][toX];

        openList.add(source);
        pQueue.add(source);

        while (openList.size() > 0) {
            Vertex cur = pQueue.poll();

            openList.remove(cur);
            closeList.add(cur);

            if (cur.equals(target)) break;

            //get adjacent grid's vertex (neighbors)
            ArrayList<Vertex> neighbors = getNeighbors(cur);

            for (Vertex neighbor : neighbors) {

                // for each neighbor's vertex
                // that is not null (valid vertex) and not contain in the closeset
                if (neighbor != null && !closeList.contains(neighbor)) {
                    // we will check the if
                    // this current neighbor is in the openlist
                    if (openList.contains(neighbor)) {
                        // and neighbor pathcost is greater than the new cost
                        // then we will update the neighbor vertex since it has smaller path cost
                        if (neighbor.getCost() > cur.getCost() + computeDistance(cur, neighbor)) {
                            updateVertex(cur, neighbor);
                        }
                    }
                    // otherwise, we will update this current neighbor vertex
                    // and add into the openset and priority queue
                    else {
                        updateVertex(cur, neighbor);
                        openList.add(neighbor);
                        pQueue.add(neighbor);
                    }
                }
            }
        }

        // retrieve the cells for shortest path
        shortestPath = retrieveShortestPath(source, target);

        for (Vertex v: shortestPath) {
            grids[v.getY()][v.getX()].hover();
        }
    }

    private boolean isValidVertex(int cX, int cY) {
        try {
            Grid grid;

            // discovering every neighbours of current tile being examined
            // for possibility of being the next node that makes fastest path
            for(int nX = -1; nX < 2; nX++) {
                for(int nY = -1; nY < 2; nY++) {
                    // current vertex already examined
                    if ((nX == 0) && (nY == 0)) continue;

                    // actual neighbour location determined
                    int actualNeighbourLocX = nX + cX;
                    int actualNeighbourLocY = nY + cY;

                    grid = grids[actualNeighbourLocY][actualNeighbourLocX];

                    if (!checkStatus(grid.status)) {
                        return false;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    public ArrayList<Vertex> getNeighbors(Vertex cur){
        ArrayList<Vertex> neighbors = new ArrayList<>();

        int col = cur.getX();
        int row = cur.getY();

        // SOUTH
        if(row > 0) neighbors.add(vertex[row-1][col]);

        // NORTH
        if(row < 14) neighbors.add(vertex[row+1][col]);

        // WEST
        if(col > 0) neighbors.add(vertex[row][col-1]);

        // EAST
        if(col < 19) neighbors.add(vertex[row][col+1]);

        return neighbors;
    }

    public int computeDistance(Vertex cur, Vertex neighbor) {
        Vertex prev = cur.getParent();

        int total = 10;
        int direction = 0;

        if (prev != null) {
            if (cur.getX() > prev.getX())       // facing east
                direction = 90;
            else if (cur.getY() > prev.getY())  // facing south
                direction = 180;
            else if (cur.getX() < prev.getX())       // facing west
                direction = 270;
        }

        //robot moving towards East
        if(cur.getX() < neighbor.getX()) {
            switch(direction) {
                case 0:   // facing north
                case 180: // facing south
                    total += 1;
                    break;
                case 270: // facing west
                    total += 2;
                    break;
            }
        }
        //robot moving towards West
        else if(cur.getX() > neighbor.getX()){
            switch(direction) {
                case 0:   // facing north
                case 180: // facing south
                    total += 1;
                    break;
                case 90: // facing east
                    total += 2;
                    break;
            }
        }
        //robot moving towards North
        else if(cur.getY() > neighbor.getY()){
            switch(direction) {
                case 90:  // facing east
                case 270: // facing west
                    total += 1;
                    break;
                case 180: // facing south
                    total += 2;
                    break;
            }
        }
        //robot moving towards South
        else if(cur.getY() < neighbor.getY()){
            switch(direction) {
                case 0: // facing north
                    total += 2;
                    break;
                case 90:  // facing east
                case 270: // facing west
                    total += 1;
                    break;
            }
        }

        return total;
    }
    
    public void updateVertex(Vertex current, Vertex neighbor){
        neighbor.setCost(current.getCost() + computeDistance(current,neighbor));
        neighbor.determineParent(current);
        neighbor.setTotalCost(neighbor.getCost() + neighbor.getHeuristic());
    }

    public ArrayList<Vertex> retrieveShortestPath(Vertex source, Vertex target) {
        ArrayList<Vertex> shortestPath = new ArrayList<>();

        Vertex cur = target;

        while (!cur.equals(source)) {
            shortestPath.add(cur);
            cur = cur.getParent();
        }

        Collections.reverse(shortestPath);
        return shortestPath;
    }

    public void performAction(Vertex cur, Vertex neighbor) throws InterruptedException {
        Vertex prev = cur.getParent();

        int direction = 0;

        if (prev != null) {
            if (cur.getX() > prev.getX())       // facing east
                direction = 90;
            else if (cur.getY() > prev.getY())  // facing south
                direction = 180;
            else if (cur.getX() < prev.getX())  // facing west
                direction = 270;
        }

        //robot moving towards East
        if(cur.getX() < neighbor.getX()) {
            switch(direction) {
                case 0:   // facing north
                    robot.turnRight();
                    break;
                case 180: // facing south
                    robot.turnLeft();
                    break;
                case 270: // facing west
                    robot.turnLeft();
                    robot.turnLeft();
                    break;
            }
        }
        //robot moving towards West
        if(cur.getX() > neighbor.getX()){
            switch(direction) {
                case 0:   // facing north
                    robot.turnLeft();
                    break;
                case 90: // facing east
                    robot.turnLeft();
                    robot.turnLeft();
                    break;
                case 180: // facing south
                    robot.turnRight();
                    break;
            }
        }
        //robot moving towards North
        if(cur.getY() > neighbor.getY()){
            switch(direction) {
                case 90: // facing east
                    robot.turnLeft();
                    break;
                case 180: // facing south
                    robot.turnRight();
                    robot.turnRight();
                    break;
                case 270:   // facing west
                    robot.turnRight();
                    break;
            }
        }
        //robot moving towards South
        if(cur.getY() < neighbor.getY()){
            switch(direction) {
                case 0:   // facing north
                    robot.turnLeft();
                    robot.turnLeft();
                    break;
                case 90: // facing east
                    robot.turnRight();
                    break;
                case 270: // facing west
                    robot.turnLeft();
                    break;
            }
        }

        robot.moveForward(1, direction);
    }

    public void start() {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                compute(startX, startY, endX, endY);
                arenaSimulation(startX, startY);
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public void arenaSimulation(int fromX, int fromY) {
        Vertex cur = vertex[fromY][fromX];

        for (Vertex neighbor: shortestPath) {
            try {
                performAction(cur, neighbor);
                cur = neighbor;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class VertexComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex first, Vertex second) {
            if(first.getTotalCost() < second.getTotalCost()){
                return -1;
            }else if(first.getTotalCost() > second.getTotalCost()){
                return 1;
            }else{
                if(first.getCost()<second.getCost()){
                    return -1;
                }
                else if(first.getCost()>second.getCost()){
                    return 1;
                }
                else
                    return 0;
            }
        }
    }
    
}