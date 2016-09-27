package application;

public class Explorer {
	private static final int SHORTRANGE = 5;
	private static final int LONGRANGE = 8;
	private int currentDir;
	private int x;
	private int y;
	private boolean done = false;
	private Arena arena = new Arena();
	//Arena.grid? rewrite
	private Robot robot = new Robot();
	private Grid[][] grid = new Grid[20][15];
	
	

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

	private boolean halfCheck() {
		if ((x>=12)&&(y>=17))
			return false;
		return true;		
	}
	
	private boolean notStart()
	{
		if ((x<=2)&&(y<=2))
			return false;
		return true;
	}
	
	private void moveForwardRobot(int dis, int dir) {
		//add robot control things here
		if ((halfCheck()) && (!notStart()))
			done = true;
		return;
	}	

	private void drawFront(int dir)
	{
		
		int i;
		int frontLeftDis = robot.getFrontLeftDis();
		int frontStraDis = robot.getFrontStraDis();
		int frontRightDis = robot.getFrontRightDis();
		switch (dir)
		{
			case 0:
				for (i = 0; i < frontLeftDis ; i++)
				{
					grid[y + 2 + i][x - 1].setFreeSpace(true);
				}
				if ((y + 2 + frontLeftDis < 20)&&(frontLeftDis < SHORTRANGE))
					grid[y + 2 + frontLeftDis][x - 1].setFreeSpace(false);
		
				for (i = 0; i < frontStraDis; i++)
				{
					grid[y + 2 + i][x].setFreeSpace(true);
				}
				if ((y + 2 + frontStraDis < 20)&&(frontStraDis < SHORTRANGE))
					grid[y + 2 + frontStraDis][x].setFreeSpace(false);

				for (i = 0; i < frontRightDis; i++)
				{
					grid[y + 2 + i][x + 1].setFreeSpace(true);
				}
				if ((y + 2 + frontRightDis < 20)&&(frontRightDis < SHORTRANGE))
					grid[y + 2 + frontRightDis][x + 1].setFreeSpace(false);
				break;
				
			case 1:
				for (i = 0; i < frontLeftDis; i++)
				{
					grid[y + 1][x + 2 + i].setFreeSpace(true);
				}
				if ((x + 2 + frontLeftDis < 15)&&(frontLeftDis < SHORTRANGE))
					grid[y + 1][x + 2 + frontLeftDis].setFreeSpace(false);
		
				for (i = 0; i < frontStraDis; i++)
				{
					grid[y][x + 2 + i].setFreeSpace(true);
				}
				if ((x + 2 + frontStraDis < 15)&&(frontStraDis < SHORTRANGE))
					grid[y][x + 2 + frontStraDis].setFreeSpace(false);

				for (i = 0; i < frontRightDis; i++)
				{
					grid[y - 1][x + 2 + i].setFreeSpace(true);
				}
				if ((x + 2 + frontRightDis < 15)&&(frontRightDis < SHORTRANGE))
					grid[y - 1][x + 2 + frontRightDis].setFreeSpace(false);
				break;

			case 2:
				for (i = 0; i < frontLeftDis; i++)
				{
					grid[y - 2 - i][x + 1].setFreeSpace(true);
				}
				if ((y - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
					grid[y - 2 - frontLeftDis][x + 1].setFreeSpace(false);
		
				for (i = 0; i < frontStraDis; i++)
				{
					grid[y - 2 - i][x].setFreeSpace(true);
				}
				if ((y - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
					grid[y - 2 - frontStraDis][x].setFreeSpace(false);

				for (i = 0; i < frontRightDis; i++)
				{
					grid[y - 2 - i][x - 1].setFreeSpace(true);
				}
				if ((y - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
					grid[y - 2 - frontRightDis][x - 1].setFreeSpace(false);
				break;

			case 3:
				for (i = 0; i < frontLeftDis; i++)
				{
					grid[y - 1][x - 2 - i].setFreeSpace(true);
				}
				if ((x - 2 - frontLeftDis >= 0)&&(frontLeftDis < SHORTRANGE))
				{
					grid[y - 1][x - 2 - frontLeftDis].setFreeSpace(false);
				}
		
				for (i = 0; i < frontStraDis; i++)
				{
					grid[y][x - 2 - i].setFreeSpace(true);
				}
				if ((x - 2 - frontStraDis >= 0)&&(frontStraDis < SHORTRANGE))
				{
					grid[y][x - 2 - frontStraDis].setFreeSpace(false);
				}

				for (i = 0; i < frontRightDis; i++)
				{
					grid[y + 1][x - 2 - i].setFreeSpace(true);
				}
				if ((x - 2 - frontRightDis >= 0)&&(frontRightDis < SHORTRANGE))
				{
					grid[y + 1][x - 2 - frontRightDis].setFreeSpace(false);
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
					for (i = 0; i < leftDis; ++i)
					{
						grid[y][x - 2 - i].setFreeSpace(true);
					}
					if ((x - 2 - leftDis >= 0)&&(leftDis < SHORTRANGE))
						grid[y][x - 2 - leftDis].setFreeSpace(false);
				}
				break;
				
			case 1:
				if (y < 18)
				{
					for (i = 0; i < leftDis; ++i)
					{
						grid[y + 2 + i][x].setFreeSpace(true);
					}
					if ((y + 2 + leftDis <= 19)&&(leftDis < SHORTRANGE))
						grid[y + 2 + leftDis][x].setFreeSpace(false);
				}
				break;

			case 2:
				if (x < 13)
				{
					for (i = 0; i < leftDis; ++i)
					{
						grid[y][x + 2 + i].setFreeSpace(true);
					}
					if ((x + 2 + leftDis <= 14)&&(leftDis < SHORTRANGE))
						grid[y][x + 2 + leftDis].setFreeSpace(false);
				}
				break;

			case 3:
				if (y > 1)
				{
					for (i = 0; i < leftDis; ++i)
					{
						grid[y - 2 - i][x].setFreeSpace(true);
					}
					if ((y - 2 - leftDis >= 0)&&(leftDis < SHORTRANGE))
						grid[y - 2 - leftDis][x].setFreeSpace(false);
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
						grid[y][x + 2 + i].setFreeSpace(true);
					}
					if ((x + 2 + rightDis <= 14)&&(rightDis < LONGRANGE))
						grid[y][x + 2 + rightDis].setFreeSpace(false);
				}
				break;
				
			case 1:
				if (y > 1)
				{
					for (i = 0; i < rightDis; i++)
					{
						grid[y - 2 - i][x].setFreeSpace(true);
					}
					if ((y - 2 - rightDis >= 0)&&(rightDis < LONGRANGE))
						grid[y - 2 - rightDis][x].setFreeSpace(false);
				}
				break;

			case 2:
				if (x > 1)
				{
					for (i = 0; i < rightDis; i++)
					{
						grid[y][x - 2 - i].setFreeSpace(true);
					}
					if ((x - 2 - rightDis >= 0)&&(rightDis < LONGRANGE))
						grid[y][x - 2 - rightDis].setFreeSpace(false);
				}
				break;

			case 3:
				if (y < 18)
				{
					for (i = 0; i < rightDis; i++)
					{
						grid[y + 2 + i][x].setFreeSpace(true);
					}
					if ((y + 2 + rightDis <= 19)&&(rightDis < LONGRANGE))
						grid[y + 2 + rightDis][x].setFreeSpace(false);
				}
				break;
		}
		return;
	}
	//draw front insert in while loop
	public void explore() {
		boolean needleft = false;
		int nextDir = currentDir;
		
		//get data from robot
		while(!done)
		{
			if (needleft)
			{
				nextDir = currentDir;
				drawFront(nextDir);
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
					if (robot.checkLeft())
					{	
						drawFront(currentDir);
						nextDir = 3;
						needleft = true;						
						break;
					}	
					while (robot.checkFront())
					{
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
							drawFront(currentDir);
							nextDir = 3;
							needleft = true;
							break;
						}
					}
					if (nextDir==3)
						break;
					nextDir = 1;

				case 1:
					currentDir = 1;
					if (robot.checkLeft())
					{
						drawFront(currentDir);
						nextDir = 0;
						needleft = true;
						break;
					}
					while (robot.checkFront())
					{
						drawFront(currentDir);
						moveForwardRobot(1, 1);
						if (done)
							return;
						robot.getLeftData();
						drawLeft(1);
						robot.getRightData();
						drawRight(1);					
						if (robot.checkLeft())
						{
							drawFront(currentDir);
							nextDir = 0;
							needleft = true;
							break;
						}
					}
					if (nextDir==0)
						break;
					nextDir = 2;

				case 2:
					currentDir = 2;
					if (robot.checkLeft())
					{
						drawFront(currentDir);
						nextDir = 1;
						needleft = true;
						break;
					}
					while (robot.checkFront())
					{
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
							drawFront(currentDir);
							nextDir = 1;
							needleft = true;
							break;
						}
					}
					if (nextDir==1)
						break;
					nextDir = 3;

				case 3:
					currentDir = 3;
					if (robot.checkLeft())
					{
						drawFront(currentDir);
						nextDir = 2;
						needleft = true;
						break;
					}
					while (robot.checkFront())
					{
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
							drawFront(currentDir);
							nextDir = 2;
							needleft = true;
							break;
						}
					}
					if (nextDir==2)
						break;
					nextDir = 0;
					break;
			}
		}
	}
	
}