package application;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import communication.CommManager;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import net.sf.json.JSONObject;

public class Explorer {
	private static final int SHORTRANGE = 2;
	private static final int LONGRANGE = 5;
	private int currentDir;//0 90 180 270 360
	private int x;
	private int y;
	private boolean done = false, half = false;
	private Arena result;
	private Grid[][] grids;
	private Robot robot;
	private List<PathNode> exploredRoutes;
	
	private int timePerStep;
	private int timeLimit;
	private int steps;
	private int coverage;
	private int coverageLimit;
	
	public Explorer(int x, int y, int currentDir, Arena result) {
		super();
		this.x = x;
		this.y = y;
		this.currentDir = currentDir;
		this.result = result;
		this.grids = result.grids;
		this.robot = result.getRobot();
		robot.updatePosition(x, y);
		exploredRoutes = new ArrayList<>();
		setSteps(0);
		setCoverage(0);
		for (int i=0;i<3;i++)
			for (int j=0;j<3;j++) {
				grids[i+17][j].setStart();
				grids[i][j+12].setGoal();				
			}
	}	

	public int getCoverageLimit() {
		return coverageLimit;
	}
	
	public void setCoverageLimit(int coverageLimit) {
		this.coverageLimit = coverageLimit;
	}
	
	public int getCoverage() {
		return coverage;
	}
	
	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}
	
	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	} 	

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(int currentDir) {
		this.currentDir = currentDir;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getTimePerStep() {
		return timePerStep;
	}
	
	public void setTimePerStep(int timePerStep) {
		this.timePerStep = timePerStep;
	}
	
	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	private boolean halfCheck() {
		if ((x>=13)&&(y<=1))
			return true;
		else
			return false;		
	}
	
	private boolean notStart()
	{
		if ((x<=1)&&(y>=18))
			return false;
		return true;
	}
	
	private void moveForwardRobot(int dis, int dir) throws InterruptedException {
		robot.moveForward(dis, dir);
		robot.setVisible(true);
		if (halfCheck()&&(!half))
			half = true;
		x = robot.getX();
		y = robot.getY();
		exploredRoutes.add(new PathNode(x, y, currentDir));
			
		return;
	}
	
	private boolean checkTimeCoverageLimit() {
		/*
		double temp =  coverage / 2.82;
		System.out.printf("Step No%3d  x:%2d  |  y:%2d  |  dir:%3d  | cover:%.2f%s\n",
				steps, robot.getX(), robot.getY(), currentDir, temp, "%");
		if (steps*timePerStep>=timeLimit) {
			System.out.println("Time out. Terminate.");
			return true;
			
		}
		if ((coverage / 2.82) > coverageLimit) {
			System.out.println("Coverage reached. Terminate.");
			return true;
		}
		*/
		return false;
	}

	private void backToHome() {
		System.out.println("From explore1: "+robot.getY()+" / "+robot.getX());
		AStar astar = new AStar(grids, robot, robot.getX(), robot.getY(), 1, 18);
		astar.start();
		System.out.println("From explore2: "+robot.getY()+" / "+robot.getX());
	}

	public void printMap() {
		System.out.println("---------------------------------------------------");
		System.out.println(robot.getRightDis());
		System.out.printf("x:%2d  |  y:%2d  |  dir:%3d\n", x, y, currentDir*90);
		grids[y][x].setRobot();
		for (int i=0;i<20;i++) {
			for (int j=0;j<15;j++) {
				if (grids[i][j].isFreeSpace())
					System.out.printf("%3d",0);
				else if (grids[i][j].isUnknown())
					System.out.printf("%3s", "*");
				else if (grids[i][j].isWall())
					System.out.printf("%3d",1);	
				else if (grids[i][j].isRobot()) 
					drawRobot(currentDir*90);
			}
			System.out.println();
		}
		grids[y][x].setFreeSpace();
	}
	
	public void drawRobot(int dir) {
		switch (dir) {
			case 0:
				System.out.printf("  \u2191");
				break;
			case 90:
				System.out.printf("  \u2192");
				break;
			case 180:
				System.out.printf("  \u2193");
				break;
			case 270:
				System.out.printf("  \u2190");
				break;		
		}
	}

	private void drawFront(int dir) {
		int i;
		int frontLeftDis = robot.getFrontLeftDis();
		int frontStraDis = robot.getFrontStraDis();
		int frontRightDis = robot.getFrontRightDis();
		dir = dir / 90;
		switch (dir)
		{
			case 0:
				if ((frontLeftDis>=1)||(frontLeftDis<=2)) {
					for (i = 0; i < frontLeftDis ; i++)
						drawFreeSpace(grids[y - 2 - i][x - 1]);
					if ((y - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
						drawWall(grids[y - 2 - frontLeftDis][x - 1]);
				}
				if ((frontStraDis>=1)||(frontStraDis>=2)) {
					for (i = 0; i < frontStraDis; i++)
						drawFreeSpace(grids[y - 2 - i][x]);
					if ((y - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
						drawWall(grids[y - 2 - frontStraDis][x]);
				}
				if ((frontRightDis>=1)||(frontRightDis<=2)) {
					for (i = 0; i < frontRightDis; i++)
						drawFreeSpace(grids[y - 2 - i][x + 1]);
					if ((y - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
						drawWall(grids[y - 2 - frontRightDis][x + 1]);
				}
				break;
				
			case 1:
				if ((frontLeftDis>=1)||(frontLeftDis<=2)) {
					for (i = 0; i < frontLeftDis; i++) 
						drawFreeSpace(grids[y - 1][x + 2 + i]);
					if ((x + 2 + frontLeftDis < 15)&&(frontLeftDis < SHORTRANGE))
						drawWall(grids[y - 1][x + 2 + frontLeftDis]);
				}
				if ((frontStraDis>=1)||(frontStraDis>=2)) {
					for (i = 0; i < frontStraDis; i++)
						drawFreeSpace(grids[y][x + 2 + i]);
					if ((x + 2 + frontStraDis < 15)&&(frontStraDis < SHORTRANGE))
						drawWall(grids[y][x + 2 + frontStraDis]);
				}
				if ((frontRightDis>=1)||(frontRightDis<=2)) {
					for (i = 0; i < frontRightDis; i++)
						drawFreeSpace(grids[y + 1][x + 2 + i]);
					if ((x + 2 + frontRightDis < 15)&&(frontRightDis < SHORTRANGE))
						drawWall(grids[y + 1][x + 2 + frontRightDis]);
				}
				break;

			case 2:
				if ((frontLeftDis>=1)||(frontLeftDis<=2)) {
					for (i = 0; i < frontLeftDis; i++)
						drawFreeSpace(grids[y + 2 + i][x + 1]);
					if ((y + 2 + frontLeftDis <= 19)&&(frontLeftDis < SHORTRANGE))
						drawWall(grids[y + 2 + frontLeftDis][x + 1]);
				}
				if ((frontStraDis>=1)||(frontStraDis>=2)) {
					for (i = 0; i < frontStraDis; i++)
						drawFreeSpace(grids[y + 2 + i][x]);
					if ((y + 2 + frontStraDis <= 19)&&(frontStraDis < SHORTRANGE))
						drawWall(grids[y + 2 + frontStraDis][x]);
				}
				if ((frontRightDis>=1)||(frontRightDis<=2)) {
					for (i = 0; i < frontRightDis; i++)
						drawFreeSpace(grids[y + 2 + i][x - 1]);
					if ((y + 2 + frontRightDis <= 19)&&(frontRightDis < SHORTRANGE))
						drawWall(grids[y + 2 + frontRightDis][x - 1]);
				}
				break;

			case 3:
				if ((frontLeftDis>=1)||(frontLeftDis<=2)) {
					for (i = 0; i < frontLeftDis; i++)
						drawFreeSpace(grids[y + 1][x - 2 - i]);
					if ((x - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
						drawWall(grids[y + 1][x - 2 - frontLeftDis]);
				}
				if ((frontStraDis>=1)||(frontStraDis>=2)) {
					for (i = 0; i < frontStraDis; i++)
						drawFreeSpace(grids[y][x - 2 - i]);
					if ((x - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
						drawWall(grids[y][x - 2 - frontStraDis]);
				}
				if ((frontRightDis>=1)||(frontRightDis<=2)) {
					for (i = 0; i < frontRightDis; i++)
						drawFreeSpace(grids[y - 1][x - 2 - i]);
					if ((x - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
						drawWall(grids[y - 1][x - 2 - frontRightDis]);
				}
				break;
		}	
		return;
	}

	private void drawLeft(int dir) {
		int i;
		int leftFrontDis = robot.getLeftFrontDis();
		int leftBackDis = robot.getLeftBackDis();
		switch (dir/90)
		{
			case 0:
				if (x > 1)
				{
					if ((1<=leftFrontDis)||(leftFrontDis<=2)) {
						for (i = 0; i < leftFrontDis; i++)
							drawFreeSpace(grids[y - 1][x - 2 - i]);
						if ((x - 2 - leftFrontDis >= 0)&&(leftFrontDis < SHORTRANGE))
							drawWall(grids[y - 1][x - 2 - leftFrontDis]);
					}
					if ((1<=leftBackDis)||(leftBackDis<=2)) {
						for (i = 0; i < leftBackDis; i++)
							drawFreeSpace(grids[y + 1][x - 2 - i]);
						if ((x - 2 - leftBackDis >= 0)&&(leftBackDis < SHORTRANGE))
							drawWall(grids[y + 1][x - 2 - leftBackDis]);
					}
				}
				break;
				
			case 1:
				if (y < 18)
				{
					if ((1<=leftFrontDis)||(leftFrontDis<=2)) {
						for (i = 0; i < leftFrontDis; i++)
							drawFreeSpace(grids[y - 2 - i][x + 1]);
						if ((y - 2 - leftFrontDis >= 0)&&(leftFrontDis < SHORTRANGE))
							drawWall(grids[y - 2 - leftFrontDis][x + 1]);
					}
					
					if ((1<=leftBackDis)||(leftBackDis<=2)) {
						for (i = 0; i < leftBackDis; i++)
							drawFreeSpace(grids[y - 2 - i][x - 1]);
						if ((y - 2 - leftBackDis >= 0)&&(leftBackDis < SHORTRANGE))
							drawWall(grids[y - 2 - leftBackDis][x - 1]);
					}
				}
				break;

			case 2:
				if (x < 13)
				{
					if ((1<=leftFrontDis)||(leftFrontDis<=2)) {
						for (i = 0; i < leftFrontDis; i++)
							drawFreeSpace(grids[y + 1][x + 2 + i]);
						if ((x + 2 + leftFrontDis <= 14)&&(leftFrontDis < SHORTRANGE))
							drawWall(grids[y + 1][x + 2 + leftFrontDis]);
					}
					
					if ((1<=leftBackDis)||(leftBackDis<=2)) {
						for (i = 0; i < leftBackDis; i++)
							drawFreeSpace(grids[y - 1][x + 2 + i]);
						if ((x + 2 + leftBackDis <= 14)&&(leftBackDis < SHORTRANGE))
							drawWall(grids[y - 1][x + 2 + leftBackDis]);
					}
				}
				break;

			case 3:
				if (y > 1)
				{
					if ((1<=leftFrontDis)||(leftFrontDis<=2)) {
						for (i = 0; i < leftFrontDis; i++)
							drawFreeSpace(grids[y + 2 + i][x - 1]);
						if ((y + 2 + leftFrontDis <= 19)&&(leftFrontDis < SHORTRANGE))
							drawWall(grids[y + 2 + leftFrontDis][x - 1]);
					}
					
					if ((1<=leftBackDis)||(leftBackDis<=2)) {
						for (i = 0; i < leftBackDis; i++)
							drawFreeSpace(grids[y + 2 + i][x + 1]);
						if ((y + 2 + leftBackDis <= 19)&&(leftBackDis < SHORTRANGE))
							drawWall(grids[y + 2 + leftBackDis][x + 1]);
					}
				}
				break;
		}
		return;
	}

	private void drawRight(int dir) {
		int i;
		int rightDis = robot.getRightDis();
		dir /= 90;
		if ((rightDis < 4)||(rightDis > 5))
			return;
		switch (dir)
		{
			case 0:
				if (x < 13)
				{
					for (i = 0; i < rightDis; i++)
					{
						drawFreeSpace(grids[y][x + 2 + i]);
					}
					if ((x + 2 + rightDis <= 14)&&(rightDis < LONGRANGE))
						drawWall(grids[y][x + 2 + rightDis]);
				}
				break;
				
			case 1:
				if (y < 17)
				{
					for (i = 0; i < rightDis; i++)
					{
						drawFreeSpace(grids[y + 2 + i][x]);
					}
					if ((y + 2 + rightDis <= 19)&&(rightDis < LONGRANGE))
						drawWall(grids[y + 2 + rightDis][x]);
				}
				break;

			case 2:
				if (x > 1)
				{
					for (i = 0; i < rightDis; i++)
					{
						drawFreeSpace(grids[y][x - 2 - i]);
					}
					if ((x - 2 - rightDis >= 0)&&(rightDis < LONGRANGE))
						drawWall(grids[y][x - 2 - rightDis]);
				}
				break;

			case 3:
				if (y > 1)
				{
					for (i = 0; i < rightDis; i++)
					{
						drawFreeSpace(grids[y - 2 - i][x]);
					}
					if ((y - 2 - rightDis >= 0)&&(rightDis < LONGRANGE))
						drawWall(grids[y - 2 - rightDis][x]);
				}
				break;
		}
		return;
	}
	
	public void drawWall(Grid gridToDraw) {
		if (!gridToDraw.isWall())	{
			gridToDraw.setWall();
			coverage++;
		}
	}
	
	public void drawFreeSpace(Grid gridToDraw) {
		if (!gridToDraw.isFreeSpace())	{
			gridToDraw.setFreeSpace();
			coverage++;
		}
	}

	public List<PathNode> getExploredRoutes() {
        return exploredRoutes;
    }

	private boolean checkDone() {
		if ((half)&&(!notStart())) {
			System.out.println("Exploration Done.");
			return true;
		}
		return false;
	}
	
	private boolean checkRobotLeft() throws InterruptedException {
		if ((robot.getLeftFrontDis()>0)&&
			(robot.getLeftBackDis()>0)&&
			(getLeftMidGrid().isFreeSpace()))
			return true;		
		return false;
	}
	
	private Grid getLeftMidGrid() {
		switch (currentDir) {
			case 0:
				return grids[y][x - 2];
			case 90:
				return grids[y - 2][x];
			case 180:
				return grids[y][x + 2];
			case 270:
				return grids[y + 2][x];
			default:
				return null;
		}
	}
	
	private boolean checkRobotFront() {
		if ((robot.getFrontLeftDis()>0)&&(robot.getFrontStraDis()>0)&&(robot.getFrontRightDis()>0))
			return true;			
		return false;
	}
	
	private boolean checkRobotRight() {
		return true;
	}
	
	public void outputMapDescription() {
		
	}
	
 	public void explore() throws InterruptedException {
 		exploredRoutes.clear();
		
 		while (!robot.getCommMgr().readRPI().equals("beginExplore")) {}
 		
 		//System.out.printf("x:%2d  |  y:%2d  |  dir:%3d\n", robot.getX(), robot.getY(), currentDir);
		
		currentDir = 0;
		int lastAction = 2; //turn left:-1 move forward:0 turn right:1 default:2
		robot.setDirection(0);
		robot.setVisible(true);
		
		//communicate once here
		robot.getData();
		
		drawFront(0);
		drawLeft(0);
		drawRight(0);
		
		
		System.out.printf("\n------------------"
						+ "\nTime limit:%d"
						+ "\nCover Limit:%d"
						+ "\nSpeed:%d"
						+ "\n------------------"
						+ "\n",
				getTimeLimit(), getCoverageLimit(), getTimePerStep());
		
		JSONObject json = new JSONObject();
		int[] array = new int[3];
		String p1 = null, p2 = null;
		
		while (!done) {
			if (checkRobotLeft()&&(lastAction!=-1)) {
				robot.turnLeft();
				currentDir = robot.getDirection();
				steps++;
				if (checkTimeCoverageLimit())
					break;
				lastAction = -1;
			}
			else if (checkRobotFront()) {
				moveForwardRobot(1, currentDir);
				steps++;
				if (checkDone()||checkTimeCoverageLimit())
					break;
				lastAction = 0;
			}
			else {
				robot.turnRight();
				currentDir = robot.getDirection();
				steps++;
				if (checkTimeCoverageLimit())
					break;
				lastAction = 1;
			}
			
			robot.getData();					
			drawFront(currentDir);
			drawLeft(currentDir);
			drawRight(currentDir);
			array[0] = x;
			array[1] = y;
			array[2] = currentDir;
			p1 = result.encodeMapDescriptor(1);
			p2 = result.encodeMapDescriptor(2);			
			json.put("robotposition", array);
			json.put("P1", p1);
			json.put("P2", p2);
			robot.getCommMgr().writeRPI(json.toString());
		}
		
		outputMapDescription();
		//robot.getCommMgr().disconnect();
	}

}