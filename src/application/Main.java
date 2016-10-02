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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			
//		    Parent root = FXMLLoader.load(getClass().getResource("arenaview.fxml"));
		    Group root = new Group();
		    Scene scene = new Scene(root);

		    BorderPane bp = new BorderPane();
		   
		    arena = new Arena();
		    result = new Arena();		    
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
		    
		    Button mLoadMap = new Button("Load Map");
		    mLoadMap.setId("btn_load_map");
		    mLoadMap.setOnMouseClicked(this);
		    
		    Button mResetMap = new Button("Reset Map");
		    mResetMap.setId("btn_reset_map");
		    mResetMap.setOnMouseClicked(this);
		    
		    vbox.getChildren().addAll(mCoordinate, mExploration, mFastestPath, mSaveMap, mLoadMap, mResetMap);
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
	
	public Timer timer = new Timer();
	TimerTask timeLimit = new TimerTask() {			
		@Override
		public void run() {
			System.out.println("Time's up.");
			timer.cancel();
			System.exit(0);
			//add go back algo
		}
	};
	TimerTask timeDisplay = new TimerTask(){
		public void run() {
			preMinute = c.get(Calendar.MINUTE) - startMinute;
			preSecond = c.get(Calendar.SECOND) - startSecond;
			preMillSec = c.get(Calendar.MILLISECOND) - startMillSec;
			System.out.printf("Running Time: 0%d:%d:%d\n",preMinute,preSecond,preMillSec);			
		}
	};
	Calendar c = Calendar.getInstance();
	public int startMinute; 
	public int startSecond;
	public int startMillSec;
	public int preMinute;
	public int preSecond;
	public int preMillSec;
	
	
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
							fw.write(arena.encodeMapDescriptor());
							fw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					break;
				case "btn_exploration":
					result.getRobot().setMap(arena.grids);
					explorer = new Explorer(1, 18, 0, result.grids, result.getRobot());
					
					//add GUI set time limit
					explorer.setTimePerStep(400);
					
					startMinute = c.get(Calendar.MINUTE);
					startSecond = c.get(Calendar.SECOND);
					startMillSec = c.get(Calendar.MILLISECOND);
					//timer.scheduleAtFixedRate(timeDisplay, 0, 200);
					//timer.schedule(timeLimit, 3000);
					Thread th = new Thread(new Runnable() {						
						@Override
						public void run() {
							try {
								explorer.explore();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							/*for (int i=0;i<20;i++) {
								for (int j=0;j<15;j++) {
									if (result.grids[i][j].isFreeSpace())
										System.out.printf("%3d",0);
									else if (result.grids[i][j].isUnknown())
										System.out.printf("%3s", "*");
									else if (result.grids[i][j].isWall())
										System.out.printf("%3d",1);				
								}
								System.out.println();
							}*/
						}
					});
					
					th.setDaemon(true);
					th.start();
					break;
				case "btn_fastest_path":
					AStar astar = new AStar(arena);
					astar.start();
					break;
				case "btn_reset_map":
					//arena.resetMap();
					break;
			}			
		} 
	}
}