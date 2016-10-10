package application;
	
import java.awt.Paint;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Main extends Application implements EventHandler<Event> {
	private static Arena arena;
	private static Arena result;
	private Stage stage;
	private File workingDir, mapDir;
	private Explorer explorer;
	Label mTimer = new Label("0");
	Label mCoverage = new Label("0%");
	Label mSpeed = new Label("0");
    TextField timeInput = new TextField();
	TextField coverageInput = new TextField();
	TextField speedInput = new TextField();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			
//		    Parent root = FXMLLoader.load(getClass().getResource("arenaview.fxml"));
		    Group root = new Group();
		    Scene scene = new Scene(root);

		    BorderPane bp = new BorderPane();		    
		    arena = new Arena();
		    arena.resetMap();
		    result = new Arena();
		    explorer = new Explorer(1, 18, 0, result.grids, result.getRobot());
		    
		    bp.setLeft(arena);
		    bp.setRight(result);
		    
		    VBox vbox = new VBox(1);
		    vbox.setPadding(new Insets(10));

		    Button mCoordinate = new Button("Set Coordinate");
		    mCoordinate.setId("btn_coordinate");
		    mCoordinate.setOnMouseClicked(this);
		    
		    Button mExploration = new Button("Exploration");
		    mExploration.setId("btn_exploration");
		    mExploration.setOnMouseClicked(this);

		    Button mFastestPath = new Button("Fastest Path");
		    mFastestPath.setId("btn_fastest_path");
		    mFastestPath.setOnMouseClicked(this);
		    
		    Button mSaveMap = new Button("Save Map");
		    mSaveMap.setId("btn_save_map");
		    mSaveMap.setOnMouseClicked(this);

			Button mSaveResult = new Button("Save Result");
			mSaveResult.setId("btn_save_result");
			mSaveResult.setOnMouseClicked(this);

		    Button mLoadMap = new Button("Load Map");
		    mLoadMap.setId("btn_load_map");
		    mLoadMap.setOnMouseClicked(this);
		    
		    Button mResetMap = new Button("Reset Map");
		    mResetMap.setId("btn_reset_map");
		    mResetMap.setOnMouseClicked(this);

			HBox hboxTimer = new HBox(1);
			hboxTimer.getChildren().addAll(new Label("Time: "), mTimer);

			Button mTakeTime = new Button("OK");
		    mTakeTime.setId("btn_take_time");
		    mTakeTime.setOnMouseClicked(this);
			
			HBox hboxCoverage = new HBox(1);
			hboxCoverage.getChildren().addAll(new Label("Coverage: "), mCoverage);
			
			Button mTakeCover = new Button("OK");
		    mTakeCover.setId("btn_take_cover");
		    mTakeCover.setOnMouseClicked(this);
			
		    HBox hboxSpeed = new HBox(1);
			hboxSpeed.getChildren().addAll(new Label("Time per step: "), mSpeed);

			Button mTakeSpeed = new Button("OK");
		    mTakeSpeed.setId("btn_take_speed");
		    mTakeSpeed.setOnMouseClicked(this);
			
		    
			vbox.getChildren().addAll(mCoordinate, mExploration, mFastestPath, 
					mSaveMap, mLoadMap, mResetMap, 
					hboxTimer, timeInput, mTakeTime, 
					hboxCoverage, coverageInput, mTakeCover, 
					hboxSpeed, speedInput, mTakeSpeed);
		    bp.setCenter(vbox);
		    
		    root.getChildren().add(bp);
		    primaryStage.setScene(scene);
		    primaryStage.show();
		    
		    workingDir = new File(System.getProperty("user.dir"));
		    workingDir = new File(workingDir, "src");
		    mapDir = new File(workingDir, "maps");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
		return;
	}
			
	@Override
	public void handle(Event event) {
		String evt = event.getEventType().getName();
		Button btn = (Button) event.getSource();
		FileChooser filechooser;
		File file;
		
		if (evt.equals("MOUSE_CLICKED")) {
			switch (btn.getId()) {
				case "btn_coordinate":
					arena.setChangeCoordinate(true);
					break;
				case "btn_load_map":				
					filechooser = new FileChooser();
					filechooser.setTitle("Load Map");
					filechooser.setInitialDirectory(mapDir);
					file = filechooser.showOpenDialog(stage);
					if (file != null) {
						try {
							arena.readMapFromFile(file);
							//arena.loadMap(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}					
					break;
				case "btn_save_map":
					filechooser = new FileChooser();
					filechooser.setTitle("Save Map");
					filechooser.setInitialDirectory(mapDir);
					file = filechooser.showSaveDialog(stage);
					
					if (file != null) {
						try {
							FileWriter fw = new FileWriter(file,false);
							fw.write(result.encodeMapDescriptor(1));
							fw.write("\n");
							fw.write(result.encodeMapDescriptor(2));
							fw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					break;
				case "btn_exploration":
					result.getRobot().setMap(arena.grids);
					Thread th = new Thread(new Runnable() {						
						@Override
						public void run() {
							try {
								//System.out.println("thread running");
								explorer.explore();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					
					th.setDaemon(true);
					th.start();
					
					break;
				case "btn_fastest_path":
					try {
						result.getRobot().setDirection(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					AStar astar = new AStar(result.grids, result.getRobot(), 1, 18, 13, 1);
					astar.start();
					break;
				case "btn_reset_map":
					arena.resetMap();
					break;
				case "btn_take_time":
					String timeStr = timeInput.getText();
					if ((timeStr!=null)&&(!timeStr.isEmpty())) {
						mTimer.setText(timeStr);
						explorer.setTimeLimit(Integer.parseInt(timeStr));
					}
					break;
				case "btn_take_cover":
					String coverStr = coverageInput.getText();
					if ((coverStr!=null)&&(!coverStr.isEmpty())) {
						mCoverage.setText(coverStr+"%");
						explorer.setCoverageLimit(Integer.parseInt(coverStr));
					}
					break;
				case "btn_take_speed":
					String speedStr = speedInput.getText();
					if ((speedStr!=null)&&(!speedStr.isEmpty())) {
						mSpeed.setText(speedStr);
						explorer.setTimePerStep(Integer.parseInt(speedStr));
					}
					break;
			}			
		} 
	}
}