package application;
	
import java.awt.Paint;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
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
	private Arena arena;
	private Stage stage;
	private File workingDir, mapDir;
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			
//		    Parent root = FXMLLoader.load(getClass().getResource("arenaview.fxml"));
		    Group root = new Group();
		    Scene scene = new Scene(root);

		    BorderPane bp = new BorderPane();
		   
		    arena = new Arena();
		    
		    bp.setLeft(arena);
		    
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
		    bp.setRight(vbox);
		    
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
					
					/*file = new File(arena.mapFilePath);
					if (file != null) {
						try {
							arena.readMapFromFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}*/
					try {
						arena.readMapFromFile();
						System.out.println("sada");
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					break;
				case "btn_save_map":
					filechooser = new FileChooser();
					filechooser.setTitle("Save Map");
					filechooser.setInitialDirectory(mapDir);
					file = filechooser.showSaveDialog(stage);
					
					if (file != null) {
//						arena.loadMap(arena.generateMapDescriptor());
//						System.out.println();
					}
					break;
			}			
		} 
	}
}