package application;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Explorer {
	private static final int SHORTRANGE = 5;
	private static final int LONGRANGE = 8;
	private int currentDir;//0 90 180 270 360
	private int x;
	private int y;
	private boolean done = false, half = false;
	private Grid[][] grids;
	private Robot robot;
	private boolean warden = false;
	private List<PathNode> exploredRoutes;
	
	private int timePerStep;
	private int timeLimit;
	private int steps;
	private float coverage;
	private float coverageLimit;
	
	public Explorer(int x, int y, int currentDir, Grid[][] grids, Robot robot) {
		super();
		this.x = x;
		this.y = y;
		this.currentDir = currentDir;
		this.grids = grids;
		this.robot = robot;
        exploredRoutes = new ArrayList<>();
		setSteps(0);
		setCoverage(0);
		for (int i=0;i<3;i++)
			for (int j=0;j<3;j++) {
				grids[i+17][j].setStart();
				grids[i][j+12].setGoal();				
			}
	}	

	public float getCoverageLimit() {
		return coverageLimit;
	}
	
	public void setCoverageLimit(int coverageLimit) {
		this.coverageLimit = coverageLimit;
	}
	
	public float getCoverage() {
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
		//add robot control things here
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		robot.moveForward(dis, dir);
		robot.setVisible(true);
		if (halfCheck()&&(!half))
			half = true;
		x = robot.getX();
		y = robot.getY();
		exploredRoutes.add(new PathNode(x, y, currentDir));
		
		steps++;
		checkTimeCoverageLimit();
			
		return;
	}
	
	private void checkTimeCoverageLimit() {
		if (steps*timePerStep>=timeLimit)
			backToHomeStart();
		if (((coverage + 9) / 300) > coverageLimit)
			backToHomeStart();
	}

	private void backToHomeStart() {
		//AStar astar = new AStar(grids, robot);
		//astar.start();
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

	private void drawFront(int dir)
	{		
		int i;
		int frontLeftDis = robot.getFrontLeftDis();
		int frontStraDis = robot.getFrontStraDis();
		int frontRightDis = robot.getFrontRightDis();
		dir = dir / 90;
		switch (dir)
		{
			case 0:
				for (i = 0; i < frontLeftDis ; i++)
				{
					drawFreeSpace(grids[y - 2 - i][x - 1]);
				}
				if ((y - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
					drawWall(grids[y - 2 - frontLeftDis][x - 1]);

				for (i = 0; i < frontStraDis; i++)
				{
					drawFreeSpace(grids[y - 2 - i][x]);
				}
				if ((y - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
					drawWall(grids[y - 2 - frontStraDis][x]);

				for (i = 0; i < frontRightDis; i++)
				{
					drawFreeSpace(grids[y - 2 - i][x + 1]);
				}
				if ((y - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
					drawWall(grids[y - 2 - frontRightDis][x + 1]);
				break;
				
			case 1:
				for (i = 0; i < frontLeftDis; i++)
				{
					drawFreeSpace(grids[y - 1][x + 2 + i]);
				}
				if ((x + 2 + frontLeftDis < 15)&&(frontLeftDis < SHORTRANGE))
					drawWall(grids[y - 1][x + 2 + frontLeftDis]);
				
				for (i = 0; i < frontStraDis; i++)
				{
					drawFreeSpace(grids[y][x + 2 + i]);
				}
				if ((x + 2 + frontStraDis < 15)&&(frontStraDis < SHORTRANGE))
					drawWall(grids[y][x + 2 + frontStraDis]);

				for (i = 0; i < frontRightDis; i++)
				{
					drawFreeSpace(grids[y + 1][x + 2 + i]);
				}
				if ((x + 2 + frontRightDis < 15)&&(frontRightDis < SHORTRANGE))
					drawWall(grids[y + 1][x + 2 + frontRightDis]);
				break;

			case 2:
				for (i = 0; i < frontLeftDis; i++)
				{
					drawFreeSpace(grids[y + 2 + i][x + 1]);
				}
				if ((y + 2 + frontLeftDis <= 19)&&(frontLeftDis < SHORTRANGE))
					drawWall(grids[y + 2 + frontLeftDis][x + 1]);
		
				for (i = 0; i < frontStraDis; i++)
				{
					drawFreeSpace(grids[y + 2 + i][x]);
				}
				if ((y + 2 + frontStraDis <= 19)&&(frontStraDis < SHORTRANGE))
					drawWall(grids[y + 2 + frontStraDis][x]);

				for (i = 0; i < frontRightDis; i++)
				{
					drawFreeSpace(grids[y + 2 + i][x - 1]);
				}
				if ((y + 2 + frontRightDis <= 19)&&(frontRightDis < SHORTRANGE))
					drawWall(grids[y + 2 + frontRightDis][x - 1]);
				break;

			case 3:
				for (i = 0; i < frontLeftDis; i++)
				{
					drawFreeSpace(grids[y + 1][x - 2 - i]);
				}
				if ((x - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
				{
					drawWall(grids[y + 1][x - 2 - frontLeftDis]);
				}
		
				for (i = 0; i < frontStraDis; i++)
				{
					drawFreeSpace(grids[y][x - 2 - i]);
				}
				if ((x - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
				{
					drawWall(grids[y][x - 2 - frontStraDis]);
				}

				for (i = 0; i < frontRightDis; i++)
				{
					drawFreeSpace(grids[y - 1][x - 2 - i]);
				}
				if ((x - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
				{
					drawWall(grids[y - 1][x - 2 - frontRightDis]);
				}
				break;
		}	
		return;
	}

	private void drawLeft(int dir)
	{
		int i;
		int leftDis = robot.getLeftDis();
		switch (dir)
		{
			case 0:
				if (x > 1)
				{
					for (i = 0; i < leftDis; i++)
					{
						drawFreeSpace(grids[y][x - 2 - i]);
					}
					if ((x - 2 - leftDis >= 0)&&(leftDis < SHORTRANGE))
						drawWall(grids[y][x - 2 - leftDis]);
				}
				break;
				
			case 1:
				if (y < 18)
				{
					for (i = 0; i < leftDis; i++)
					{
						drawFreeSpace(grids[y - 2 - i][x]);
					}
					if ((y - 2 - leftDis >= 0)&&(leftDis < SHORTRANGE))
						drawWall(grids[y - 2 - leftDis][x]);
				}
				break;

			case 2:
				if (x < 13)
				{
					for (i = 0; i < leftDis; i++)
					{
						drawFreeSpace(grids[y][x + 2 + i]);
					}
					if ((x + 2 + leftDis <= 14)&&(leftDis < SHORTRANGE))
						drawWall(grids[y][x + 2 + leftDis]);
				}
				break;

			case 3:
				if (y > 1)
				{
					for (i = 0; i < leftDis; i++)
					{
						drawFreeSpace(grids[y + 2 + i][x]);
					}
					if ((y + 2 + leftDis <= 19)&&(leftDis < SHORTRANGE))
						drawWall(grids[y + 2 + leftDis][x]);
				}
				break;
		}
		return;
	}

	private void drawRight(int dir)
	{
		int i;
		int rightDis = robot.getRightDis();
		dir /= 90;
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
	
	Calendar c = Calendar.getInstance();
	public int startMinute; 
	public int startSecond;
	public int startMillSec;
	public int preMinute;
	public int preSecond;
	public int preMillSec;
	
	public int getPreMinute() {
		return preMinute;
	}

	public void setPreMinute(int preMinute) {
		this.preMinute = preMinute;
	}

	public int getPreSecond() {
		return preSecond;
	}

	public void setPreSecond(int preSecond) {
		this.preSecond = preSecond;
	}

	public int getPreMillSec() {
		return preMillSec;
	}

	public void setPreMillSec(int preMillSec) {
		this.preMillSec = preMillSec;
	}
	
	public String getRunningTime() {
		return ("Running Time: "+preMinute+":"+preSecond+":"+preMillSec);
	}

	/*public Timer timer = new Timer();
	TimerTask timeLimit = new TimerTask() {			
		@Override
		public void run() {
			System.out.println("Time's up.");
			timer.cancel();
			System.exit(0);
			//add go back algo
		}
	};*/
	TimerTask timeDisplay = new TimerTask(){
		public void run() {
			preMinute = c.get(Calendar.MINUTE) - startMinute;
			preSecond = c.get(Calendar.SECOND) - startSecond;
			preMillSec = c.get(Calendar.MILLISECOND) - startMillSec;
			System.out.printf("Running Time: 0%d:%d:%d\n",preMinute,preSecond,preMillSec);			
		}
	};
	
	private boolean checkDone() {
		if ((half)&&(!notStart())) {
			System.out.println("Exploration Done.");
			return true;
		}
		return false;
	}
	
	private boolean checkRobotLeft() throws InterruptedException {
		if (robot.checkLeft())
			return true;
		else
			return false;
	}
	
	public void outputMapDescription() {
		
	}
	
 	public void explore() throws InterruptedException {
 		exploredRoutes.clear();
		boolean needLeft = false;
		boolean needRight = false;
		//System.out.printf("x:%2d  |  y:%2d  |  dir:%3d\n", x, y, currentDir);
		
		currentDir = 0;
		robot.setDirection(0);
		robot.setVisible(true);
		robot.getFrontData();
		drawFront(0);
		robot.getLeftData();
		drawLeft(0);
		robot.getRightData();
		drawRight(0);
		
		while (!done) {
			if (checkDone())
				return;

			if (needRight) {
				robot.getFrontData();
				drawFront(robot.getDirection());				
				needRight = false;
			}
			else {
				if (needLeft)
					needLeft = false;
				else {
					if (checkRobotLeft()) {
						steps++;
						robot.getFrontData();
						drawFront(robot.getDirection());
						needLeft = true;
						continue;
					}
					else {
						steps++;
						robot.getFrontData();
						drawFront(robot.getDirection());
					}
				}
			}
			
			currentDir = robot.getDirection();

			while (robot.checkFront()) {
				robot.getFrontData();
				drawFront(currentDir);
				moveForwardRobot(1,currentDir);
				if (checkDone())
					return;
				robot.getLeftData();
				drawLeft(currentDir);
				robot.getRightData();
				drawRight(currentDir);
				if (checkRobotLeft()) {
					robot.getFrontData();
					drawFront(robot.getDirection());
					needLeft = true;
					break;
				}
				else {
					robot.getFrontData();
					drawFront(robot.getDirection());
				}
			}

			if (!needLeft) {
				robot.turnRight();
				steps++;
				needRight = true;
			}
		}
		outputMapDescription();
	}

}