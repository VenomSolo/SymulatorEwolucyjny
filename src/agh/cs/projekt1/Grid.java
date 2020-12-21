package agh.cs.projekt1;

import agh.cs.po.Pawn;
import agh.cs.po.Vector2d;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;

import javax.swing.text.Highlighter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Grid {
    private agh.cs.po.Map map;
    private TilePane tiles;
    private HashMap<Vector2d, Label> labels;
    private int n;
    private Vector2d lastPicked = null;
    private String lastStyle = null;
    final int HGAP = 2;
    final int VGAP = 2;

    public Grid(int columns, int rows, int width, int height)
    {
        n = columns;
        Vector2d tempVector;
        Label tempLabel;
        labels = new HashMap<>();
        tiles = new TilePane();
        tiles.setPrefColumns(columns);
        tiles.setPrefRows(rows);
        //tiles.setPrefSize(width, height);
        tiles.setMaxSize(width, height);
        tiles.setStyle("-fx-background-color: #000000;");
        tiles.setPadding(new Insets(HGAP/2,VGAP/2,HGAP/2,VGAP/2));
        tiles.setHgap(HGAP);
        tiles.setVgap(VGAP);
        for(int i = columns-1; i >= 0; i--)
        {
            for(int j = 0; j <= rows-1; j++)
            {
                tempVector = new Vector2d(j,i);
                tempLabel = new Label();
                tempLabel.setStyle("-fx-background-color: #FFFFFF;");
                //tempLabel.setText(tempVector.toString());
                tempLabel.setMaxSize(width/columns-VGAP, height/rows-HGAP);
                tempLabel.setPrefSize(width/columns-VGAP, height/rows-HGAP);
                labels.put(tempVector, tempLabel);
                this.tiles.getChildren().add(tempLabel);
            }
        }
        for(Label label : labels.values())
        {
            label.setOnMouseClicked(event ->
            {
                for(Map.Entry<Vector2d, Label> entry : labels.entrySet())
                {
                    if(entry.getValue() == label)
                    {
                        if(lastPicked != null && lastStyle != null)
                        {
                            labels.get(lastPicked).setStyle(lastStyle);
                        }
                        lastPicked = entry.getKey();
                        lastStyle = entry.getValue().getStyle();
                        HighlightOne(entry.getKey());
                    }
                }
            });
        }
    }

    public void setMap(agh.cs.po.Map map) {
        this.map = map;
    }

    public int getN() {return n;}

    public TilePane getTiles()
    {
        return tiles;
    }

    public synchronized void ClearGrid()
    {
        for(Map.Entry<Vector2d, Label> entry : labels.entrySet())
        {
            labels.get(entry.getKey()).setStyle("-fx-background-color: #FFFFFF;");
        }
    }

    public synchronized void UpdateGrid(Vector2d position, String newColor)
    {
        if(labels.containsKey(position))
        {
            labels.get(position).setStyle("-fx-background-color: " + newColor + ";");
        }
    }

    public synchronized void Highlight(int[] genes)
    {
        Genom controller = Genom.existingGenoms.get(genes);
        for(Pawn pawn : controller.getPossessedPawns())
        {
            if(labels.containsKey(pawn.getPosition()))
            {
                labels.get(pawn.getPosition()).setStyle("-fx-background-color: " + "#FFA500" + ";");
            }
        }
    }

    private synchronized void HighlightOne(Vector2d position)
    {
        if(map.ObjectAtTop(position) instanceof Animal)
        {
            labels.get(position).setStyle("-fx-background-color: " + "#ffd700" + ";");
            ((SimulationMap)map).stats.SetChartTitle(Arrays.toString(((Genom) ((Animal) map.ObjectAtTop(position)).getController()).genes));
        }

    }
}
