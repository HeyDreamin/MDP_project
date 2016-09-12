package application;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Robot extends BorderPane {
	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	// Background color that the robot cover
    private Color c = new Color(0.7, 0.3, 0.4, 0.2);
    private BackgroundFill frbg = new BackgroundFill(c, null, null);
    private Background rbg = new Background(frbg);
    
	private int x;
	private int y;
	private int direction;

	private ImageView robot;
	
	public Robot() {
		super();
		
		x = 17;
		y = 0;
		direction = 0;
		
	    setBackground(rbg);
	    setPrefWidth(90);
	    setPrefHeight(90);

	    robot = new ImageView("images/ic_mdp_robot.png");
	    robot.setFitHeight(70);
	    robot.setFitWidth(70);
	    
	    setCenter(robot);
	    setVisible(false);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		setLayoutX(x*30);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		setLayoutY(y*30);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
		robot.setRotate(direction);
	}
	
	public void turnLeft() {
		setDirection((direction-90)%360);
	}
	
	public void turnRight() {
		setDirection((direction-90)%360);
	}
	
	public void updatePosition(int x, int y) {
		setX(x);
		setY(y);
	}
}
