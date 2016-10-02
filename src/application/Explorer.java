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
	
	public Explorer(int x, int y, int currentDir, Grid[][] grids, Robot robot) {
		super();
		this.x = x;
		this.y = y;
		this.currentDir = currentDir;
		this.grids = grids;
		this.robot = robot;
        exploredRoutes = new ArrayList<>();
		steps = 0;
		for (int i=0;i<3;i++)
			for (int j=0;j<3;j++) {
				grids[i+17][j].setStart();
				grids[i][j+12].setGoal();				
			}
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
		if ((x>=12)&&(y<=2))
			return true;
		else
			return false;		
	}
	
	private boolean notStart()
	{
		if ((x<=2)&&(y>=17))
			return false;
		return true;
	}
	
	private void moveForwardRobot(int dis, int dir) throws InterruptedException {
		//add robot control things here
		try {
			Thread.sleep(timePerStep);
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
		
		if ((steps*timePerStep)>=timeLimit)
			backToHomeStart();
			
		return;
	}
	
	private void backToHomeStart() {
		AStar astar = new AStar(grids, robot);
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
					grids[y - 2 - i][x - 1].setFreeSpace();
				}
				if ((y - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
					grids[y - 2 - frontLeftDis][x - 1].setWall();

				for (i = 0; i < frontStraDis; i++)
				{
					grids[y - 2 - i][x].setFreeSpace();
				}
				if ((y - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
					grids[y - 2 - frontStraDis][x].setWall();

				for (i = 0; i < frontRightDis; i++)
				{
					grids[y - 2 - i][x + 1].setFreeSpace();
				}
				if ((y - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
					grids[y - 2 - frontRightDis][x + 1].setWall();
				break;
				
			case 1:
				for (i = 0; i < frontLeftDis; i++)
				{
					grids[y - 1][x + 2 + i].setFreeSpace();
				}
				if ((x + 2 + frontLeftDis < 15)&&(frontLeftDis < SHORTRANGE))
					grids[y - 1][x + 2 + frontLeftDis].setWall();
				
				for (i = 0; i < frontStraDis; i++)
				{
					grids[y][x + 2 + i].setFreeSpace();
				}
				if ((x + 2 + frontStraDis < 15)&&(frontStraDis < SHORTRANGE))
					grids[y][x + 2 + frontStraDis].setWall();

				for (i = 0; i < frontRightDis; i++)
				{
					grids[y + 1][x + 2 + i].setFreeSpace();
				}
				if ((x + 2 + frontRightDis < 15)&&(frontRightDis < SHORTRANGE))
					grids[y + 1][x + 2 + frontRightDis].setWall();
				break;

			case 2:
				for (i = 0; i < frontLeftDis; i++)
				{
					grids[y + 2 + i][x + 1].setFreeSpace();
				}
				if ((y + 2 + frontLeftDis <= 19)&&(frontLeftDis < SHORTRANGE))
					grids[y + 2 + frontLeftDis][x + 1].setWall();
		
				for (i = 0; i < frontStraDis; i++)
				{
					grids[y + 2 + i][x].setFreeSpace();
				}
				if ((y + 2 + frontStraDis <= 19)&&(frontStraDis < SHORTRANGE))
					grids[y + 2 + frontStraDis][x].setWall();

				for (i = 0; i < frontRightDis; i++)
				{
					grids[y + 2 + i][x - 1].setFreeSpace();
				}
				if ((y + 2 + frontRightDis <= 19)&&(frontRightDis < SHORTRANGE))
					grids[y + 2 + frontRightDis][x - 1].setWall();
				break;

			case 3:
				for (i = 0; i < frontLeftDis; i++)
				{
					grids[y + 1][x - 2 - i].setFreeSpace();
				}
				if ((x - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
				{
					grids[y + 1][x - 2 - frontLeftDis].setWall();
				}
		
				for (i = 0; i < frontStraDis; i++)
				{
					grids[y][x - 2 - i].setFreeSpace();
				}
				if ((x - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
				{
					grids[y][x - 2 - frontStraDis].setWall();
				}

				for (i = 0; i < frontRightDis; i++)
				{
					grids[y - 1][x - 2 - i].setFreeSpace();
				}
				if ((x - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
				{
					grids[y - 1][x - 2 - frontRightDis].setWall();
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
						grids[y][x - 2 - i].setFreeSpace();
					}
					if ((x - 2 - leftDis >= 0)&&(leftDis < SHORTRANGE))
						grids[y][x - 2 - leftDis].setWall();
				}
				break;
				
			case 1:
				if (y < 18)
				{
					for (i = 0; i < leftDis; i++)
					{
						grids[y - 2 - i][x].setFreeSpace();
					}
					if ((y - 2 - leftDis >= 0)&&(leftDis < SHORTRANGE))
						grids[y - 2 - leftDis][x].setWall();
				}
				break;

			case 2:
				if (x < 13)
				{
					for (i = 0; i < leftDis; i++)
					{
						grids[y][x + 2 + i].setFreeSpace();
					}
					if ((x + 2 + leftDis <= 14)&&(leftDis < SHORTRANGE))
						grids[y][x + 2 + leftDis].setWall();
				}
				break;

			case 3:
				if (y > 1)
				{
					for (i = 0; i < leftDis; i++)
					{
						grids[y + 2 + i][x].setFreeSpace();
					}
					if ((y + 2 + leftDis <= 19)&&(leftDis < SHORTRANGE))
						grids[y + 2 + leftDis][x].setWall();
				}
				break;
		}
		return;
	}

	private void drawRight(int dir)
	{
		int i;
		int rightDis = robot.getRightDis();
		switch (dir)
		{
			case 0:
				if (x < 13)
				{
					for (i = 0; i < rightDis; i++)
					{
						grids[y][x + 2 + i].setFreeSpace();
					}
					if ((x + 2 + rightDis <= 14)&&(rightDis < LONGRANGE))
						grids[y][x + 2 + rightDis].setWall();
				}
				break;
				
			case 1:
				if (y < 17)
				{
					for (i = 0; i < rightDis; i++)
					{
						grids[y + 2 + i][x].setFreeSpace();
					}
					if ((y + 2 + rightDis <= 19)&&(rightDis < LONGRANGE))
						grids[y + 2 + rightDis][x].setWall();
				}
				break;

			case 2:
				if (x > 1)
				{
					for (i = 0; i < rightDis; i++)
					{
						grids[y][x - 2 - i].setFreeSpace();
					}
					if ((x - 2 - rightDis >= 0)&&(rightDis < LONGRANGE))
						grids[y][x - 2 - rightDis].setWall();
				}
				break;

			case 3:
				if (y > 1)
				{
					for (i = 0; i < rightDis; i++)
					{
						grids[y - 2 - i][x].setFreeSpace();
					}
					if ((y - 2 - rightDis >= 0)&&(rightDis < LONGRANGE))
						grids[y - 2 - rightDis][x].setWall();
				}
				break;
		}
		return;
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
	
 	public void explore() throws InterruptedException {
 		exploredRoutes.clear();
		boolean needleft = false;
		int nextDir = currentDir/90;
		//System.out.printf("x:%2d  |  y:%2d  |  dir:%3d\n", x, y, currentDir);
		
		startMinute = c.get(Calendar.MINUTE);
		startSecond = c.get(Calendar.SECOND);
		startMillSec = c.get(Calendar.MILLISECOND);
		//timer.scheduleAtFixedRate(timeDisplay, 0, 200);
		//timer.schedule(timeLimit, 3000);
		
		
		robot.setDirection(0);
		robot.getFrontData();
		drawFront(0);
		robot.getLeftData();
		drawLeft(0);
		robot.getRightData();
		drawRight(0);
		
		
		while(!done)
		{
			
			if ((half) && (!notStart())) {
				done = true;
				System.out.println("Done.");
				return;
			}
			if (needleft)
			{
				currentDir = nextDir;
				robot.setDirection(nextDir*90);
				moveForwardRobot(1, nextDir);
				if (done)
					return;
				robot.getLeftData();
				drawLeft(nextDir);
				robot.getRightData();
				drawRight(nextDir);
				needleft = false;
			}
			
			switch(nextDir)
			{
				case 0:
					currentDir = 0;
					robot.setDirection(0);
					if (robot.checkLeft())
					{	
						robot.getFrontData();
						drawFront(robot.getDirection());
						nextDir = 3;
						needleft = true;					
						break;
					}
					else {
						robot.getFrontData();
						drawFront(robot.getDirection());
					}
					robot.setDirection(0);
					while (robot.checkFront())
					{
						robot.getFrontData();
						drawFront(currentDir);
						moveForwardRobot(1, 0);
						if (done)
							return;
						robot.getLeftData();
						drawLeft(0);
						robot.getRightData();
						drawRight(0);
						if (robot.checkLeft())
						{
							robot.getFrontData();
							drawFront(robot.getDirection());
							nextDir = 3;
							needleft = true;
							break;
						}
						else {
							robot.getFrontData();
							drawFront(robot.getDirection());
						}
						robot.setDirection(0);
					}
					if (nextDir==3)
						break;
					nextDir = 1;
					
				case 1:
					currentDir = 90;
					robot.setDirection(90);
					if (robot.checkLeft())
					{
						robot.getFrontData();
						drawFront(robot.getDirection());
						nextDir = 0;
						needleft = true;
						break;
					}
					else {
						robot.getFrontData();
						drawFront(robot.getDirection());
					}
					robot.setDirection(90);
					while (robot.checkFront())
					{
						robot.getFrontData();
						drawFront(90);						
						moveForwardRobot(1, 1);
						if (done)
							return;
						robot.getLeftData();
						drawLeft(1);
						robot.getRightData();
						drawRight(1);					
						if (robot.checkLeft())
						{
							robot.getFrontData();
							drawFront(robot.getDirection());
							nextDir = 0;
							needleft = true;
							break;
						}
						else {
							robot.getFrontData();
							drawFront(robot.getDirection());
						}
						robot.setDirection(90);						
					}
					if (nextDir==0)
						break;
					nextDir = 2;

				case 2:
					currentDir = 180;
					robot.setDirection(180);
					if (robot.checkLeft())
					{
						robot.getFrontData();
						drawFront(robot.getDirection());
						nextDir = 1;
						needleft = true;
						break;
					}
					else {
						robot.getFrontData();
						drawFront(robot.getDirection());
					}
					robot.setDirection(180);
					while (robot.checkFront())
					{
						robot.getFrontData();
						drawFront(currentDir);
						moveForwardRobot(1, 2);
						if (done)
							return;
						robot.getLeftData();
						drawLeft(2);
						robot.getRightData();
						drawRight(2);					
						if (robot.checkLeft())
						{
							robot.getFrontData();
							drawFront(robot.getDirection());
							nextDir = 1;
							needleft = true;
							break;
						}
						else {
							robot.getFrontData();
							drawFront(robot.getDirection());
						}
						robot.setDirection(180);
					}
					if (nextDir==1)
						break;
					nextDir = 3;

				case 3:
					currentDir = 270;
					robot.setDirection(270);
					if (robot.checkLeft())
					{
						robot.getFrontData();
						drawFront(robot.getDirection());
						nextDir = 2;
						needleft = true;
						break;
					}
					else {
						robot.getFrontData();
						drawFront(robot.getDirection());
					}
					robot.setDirection(270);
					while (robot.checkFront())
					{
						robot.getFrontData();
						drawFront(currentDir);
						moveForwardRobot(1, 3);
						if (done)
							return;
						robot.getLeftData();
						drawLeft(3);
						robot.getRightData();
						drawRight(3);					
						if (robot.checkLeft())
						{
							robot.getFrontData();
							drawFront(robot.getDirection());
							nextDir = 2;
							needleft = true;
							break;
						}
						else {
							robot.getFrontData();
							drawFront(robot.getDirection());
						}
						robot.setDirection(270);
					}
					if (nextDir==2)
						break;
					nextDir = 0;
					break;
			}
		}
		
	}	
}