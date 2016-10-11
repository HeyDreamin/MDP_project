package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import communication.CommManager;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/*Command:	AW:move forward
 *			AA:turn left
 *			AD:turn right
*/

public class Robot extends BorderPane {
	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	}
	private static final int SHORTRANGE = 3;
	private static final int LONGRANGE = 5;

	// Background color that the robot cover
    private Color c = new Color(0.7, 0.3, 0.4, 0.2);
    private BackgroundFill frbg = new BackgroundFill(c, null, null);
    private Background rbg = new Background(frbg);
    
	private int x;
	private int y;
	private int direction;
	private int frontLeftDis;
	private int frontStraDis;
	private int frontRightDis;
	private int leftFrontDis;
	private int leftBackDis;
	private int rightDis;
	private int[] sensorData;
	private Grid[][] originMap;
	private boolean done = false;
	private ImageView robot;
	private CommManager commMgr;
	
	public Robot(int x, int y, int dir, Grid[][] map) {
		super();
		
		this.x = x;//ini 1
		this.y = y;//ini 18
		this.direction = dir;
		
	    setBackground(rbg);
	    setPrefWidth(90);
	    setPrefHeight(90);

	    robot = new ImageView("images/ic_mdp_robot.png");
	    robot.setFitHeight(70);
	    robot.setFitWidth(70);
	    this.originMap = map;
	    commMgr = new CommManager();
	    commMgr.writeRPI("AT");
	    System.out.println(commMgr.readRPI());
	    
	    sensorData = new int[6];
	    
	    setCenter(robot);
	    setVisible(false);
	}
	
	
	
	public CommManager getCommMgr() {
		return commMgr;
	}

	public void setCommMgr(CommManager commMgr) {
		this.commMgr = commMgr;
	}

	public int[] getSensorData() {
		return sensorData;
	}

	public void setSensorData(int[] sensorData) {
		this.sensorData = sensorData;
	}

	public void setMap(Grid[][] map) {
		originMap = map;
	}

	public int getFrontLeftDis() {
		return frontLeftDis;
	}

	public void setFrontLeftDis(int frontLeftDis) {
		this.frontLeftDis = frontLeftDis;
	}

	public int getFrontStraDis() {
		return frontStraDis;
	}

	public void setFrontStraDis(int frontStraDis) {
		this.frontStraDis = frontStraDis;
	}

	public int getFrontRightDis() {
		return frontRightDis;
	}

	public void setFrontRightDis(int frontRightDis) {
		this.frontRightDis = frontRightDis;
	}

	public int getLeftFrontDis() {
		return leftFrontDis;
	}

	public void setLeftFrontDis(int leftFrontDis) {
		this.leftFrontDis = leftFrontDis;
	}
	
	public int getLeftBackDis() {
		return leftBackDis;
	}

	public void setLeftBackDis(int leftBackDis) {
		this.leftBackDis = leftBackDis;
	}

	public int getRightDis() {
		return rightDis;
	}

	public void setRightDis(int rightDis) {
		this.rightDis = rightDis;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		setLayoutX((x-1)*30);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		setLayoutY((y-1)*30);
	}

	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) throws InterruptedException {
		Thread.sleep(200);
		this.direction = direction;
		robot.setRotate(direction);
	}
	
	public void getFrontData()
	{
		int i,j,dis;
		boolean reach_wall = false;
		dis = 0;
		for (i = 0; i < 3; i++)
		{
			for (j = 0; j < SHORTRANGE; j++)
			{
				switch (direction)
				{
					case 0:
						if ((y - 2 - j >= 0)&&(originMap[y - 2 - j][x + i - 1].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 90:
						if ((x + j + 2 <= 14)&&(originMap[y - 1 + i][x + j + 2].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 180:
						if ((y + 2 + j <= 19)&&(originMap[y + 2 + j][x - i + 1].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 270:
						if ((x - j - 2 >= 0)&&(originMap[y + 1 - i][x - j - 2].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					default:
						break;
				}
				if (reach_wall)
					break;
			}
			switch (i)
			{
				case 0:
					setFrontLeftDis(dis);
					break;
				case 1:
					setFrontStraDis(dis);
					break;
				case 2:
					setFrontRightDis(dis);
					break;
				default:
					break;
			}
			reach_wall = false;
			dis = 0;
		}
		return;
	}

	public void getLeftData()
	{
		int j, dis;
		boolean reach_wall = false;
		dis = 0;
		for (int i=-1;i<2;i++) {
			if (i==0)
				continue;
			for (j = 0; j < SHORTRANGE; j++) {
				switch (direction)
				{
					case 0:
						if ((x - 2 - j >= 0)&&(originMap[y + i][x - 2 - j].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 90:
						if ((y - 2 - j >= 0)&&(originMap[y - 2 - j][x - i].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 180:
						if ((x + 2 + j <= 14)&&(originMap[y - i][x + 2 + j].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					case 270:
						if ((y + 2 + j <= 19)&&(originMap[y + 2 + j][x + i].isFreeSpace())&&(!reach_wall))
						{
							dis++;
						}
						else
							reach_wall = true;
						break;
					default:
						break;
				}
				if (reach_wall)
					break;				
			}
			if (i==-1)
				setLeftFrontDis(dis);
			else if (i==1)
				setLeftBackDis(dis);
			dis = 0;
			reach_wall = false;
		}
		return;
	}

	public void getRightData()
	{
		int j, dis;
		boolean reach_wall = false;
		dis = 0;
		for (j = 0; j < LONGRANGE; j++)
		{
			switch (direction)
			{
				case 0:
					if ((x + 2 + j <= 14)&&(originMap[y][x + 2 + j].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				case 90:
					if ((y + 2 + j <= 19)&&(originMap[y + 2 + j][x].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				case 180:
					if ((x - 2 - j >= 0)&&(originMap[y][x - 2 - j].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				case 270:
					if ((y - 2 - j >= 0)&&(originMap[y - 2 - j][x].isFreeSpace())&&(!reach_wall))
					{
						dis++;
					}
					else
						reach_wall =true;
					break;
				default:
					break;
			}
			if (reach_wall)
				break;
		}
		setRightDis(dis);
	}

	public void getData() throws InterruptedException
	{
		System.out.println("ready to write."); 
		commMgr.writeRPI("AG");//get data command
		//Thread.sleep(500);
		System.out.println("Sent.");
		String[] data = commMgr.readRPI().split(" ");
		for (int i=0;i<6;i++) {
			sensorData[i] = Integer.parseInt(data[i]);
			System.out.printf("%2d,", sensorData[i]);
		}		
		System.out.println();

		//for real run
		setFrontRightDis((sensorData[0])/10);
		setFrontStraDis((sensorData[1])/10);
		setFrontLeftDis((sensorData[2])/10);
		setLeftFrontDis((sensorData[3])/10);
		setLeftBackDis((sensorData[4])/10);
		setRightDis((sensorData[5])/10);
	
		//for simulation
		/*
		getLeftData();
		getFrontData();
		getRightData();
		*/
		return;
	}
	
	public void checkAction(String str) {
		while (str.compareTo("ready")==0) {
			return;
		}
	}
	
	public void moveForward(int dis, int dir) throws InterruptedException
	{
		commMgr.writeRPI("AW");
		checkAction(commMgr.readRPI());
		//Thread.sleep(3000);
		switch (dir)
		{
			case 0:
				y -= dis;			
				break;

			case 1:
			case 90:
				x += dis;
				break;

			case 2:
			case 180:
				y += dis;
				break;

			case 3:
			case 270:
				x -= dis;
				break;
		}
		updatePosition(x, y);
	}
	
	public void turnLeft() throws InterruptedException {
		commMgr.writeRPI("AA");
		checkAction(commMgr.readRPI());
		//Thread.sleep(3000);
		setDirection((direction+270)%360);
		;
	}
	
	public void turnRight() throws InterruptedException {
		commMgr.writeRPI("AD");
		checkAction(commMgr.readRPI());
		//Thread.sleep(3000);
		setDirection((direction+90)%360);
	}
	
	public void updatePosition(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public boolean checkFront() throws InterruptedException
	{
		getData();		
		if ((frontLeftDis>0)&&(frontStraDis>0)&&(frontRightDis>0))
			return true;
		return false;
	}

	public boolean checkLeft() throws InterruptedException
	{
		getData();
		if (leftFrontDis>0)
		{
			turnLeft();
			if (checkFront())
				return true;
			else {
				return false;
			}
		}
		return false;
	}
	
}