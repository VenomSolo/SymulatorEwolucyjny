package sample;

import agh.cs.projekt1.Grid;
import agh.cs.projekt1.SimulationScene;
import agh.cs.projekt1.Statistics;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.stage.*;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {
    public Statistics stats;
    public static AtomicBoolean paused1 = new AtomicBoolean(false);
    public static AtomicBoolean paused2 = new AtomicBoolean(false);
    private static final int PREFERRED_WIDTH = 1920;
    private static final int PREFERRED_HEIGHT = 1080;
    private javafx.scene.Scene scene;
    private Pane root;

    class Params {
        public int width;
        public int height ;
        public int startEnergy;
        public int moveEnergy;
        public int plantEnergy;
        public float jungleRatio;

        public Params(){}

        public Params(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio)
        {
            this.width = width;
            this.height = height;
            this.startEnergy = startEnergy;
            this.moveEnergy = moveEnergy;
            this.plantEnergy = plantEnergy;
            this.jungleRatio = jungleRatio;
        }
    }

    public static void infoBox(String infoMessage, String titleBar)
    {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        infoBox(infoMessage, titleBar, null);
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

@Override
    public void start(Stage primaryStage) throws Exception{

        Params par = new Params(1,1,1,1,1,1.0f);
        String jsonString;
        Gson gson = new Gson();
        System.out.println(gson.toJson(par));
        Params params = null;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "json");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            params = gson.fromJson(new FileReader(chooser.getSelectedFile().getAbsolutePath()),Params.class);
        }
        else
        {
            infoBox( chooser.getSelectedFile().getName() + " is not JSON!", "Wrong file type");
            return;
        }

        SimulationScene.startEnergy = params.startEnergy;
        SimulationScene.moveEnergy = params.moveEnergy;
        SimulationScene.plantEnergy = params.plantEnergy;

        Grid grid1 = new Grid(params.width, params.height, 500, 500);
        Grid grid2 = new Grid(params.width, params.height, 500, 500);
        Statistics stat1 = new Statistics(500, 500);
        Statistics stat2 = new Statistics(500, 500);
        agh.cs.projekt1.SimulationScene simScene1 = new SimulationScene(true, new Grid[]{grid1}, new Statistics[]{stat1}, params.jungleRatio);
        agh.cs.projekt1.SimulationScene simScene2 = new SimulationScene(true, new Grid[]{grid2}, new Statistics[]{stat2}, params.jungleRatio);


        root = new Pane();


        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER);
        vBox1.getChildren().add(grid1.getTiles());
        javafx.scene.control.Button pause1 = new javafx.scene.control.Button("Pause");
        pause1.setOnAction(event ->
        {
            if(!paused1.get())
            {
                pause1.setText("Start");
                paused1.set(true);
            }
            else
            {
                pause1.setText("Pause");
                paused1.set(false);

                // Resume
                synchronized(simScene1)
                {
                    simScene1.notify();
                }
            }
        });
        vBox1.getChildren().add(pause1);
        vBox1.getChildren().add(stat1.getBox());


        VBox vBox2 = new VBox();
        vBox2.setAlignment(Pos.CENTER);
        vBox2.getChildren().add(grid2.getTiles());
        javafx.scene.control.Button pause2 = new javafx.scene.control.Button("Pause");
        pause2.setOnAction(event ->
        {
            if(!paused2.get())
            {
                pause2.setText("Start");
                paused2.set(true);
            }
            else
            {
                pause2.setText("Pause");
                paused2.set(false);
                // Resume
                synchronized(simScene2)
                {
                    simScene2.notify();
                }
            }
        });
        vBox2.getChildren().add(pause2);
        vBox2.getChildren().add(stat2.getBox());

        HBox hbox = new HBox();
        hbox.getChildren().add(vBox1);
        hbox.getChildren().add(vBox2);
        hbox.setSpacing(100);


        root.getChildren().add(hbox);
        //LineChart chart = new LineChart();
        scene = new javafx.scene.Scene(root);
        root.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Symulator ewolucyjny");
        primaryStage.show();
        simScene1.start();
        simScene2.start();

}


    public static void main(String[] args) {
        launch(args);
    }
}
