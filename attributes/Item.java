package attributes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by William on Oct-30-2014.
 * This class is designed to be the base for any item added to the map.
 * Currently its the parent to: Hive,
 */

public abstract class Item extends SavableObject {
    //Grid is the width & height of every square on the map. Smallest graphical unit.
    protected final int grid;
    //This is the x & y coordinate based off of the tiles.
    protected int x = 0;
    protected int y = 0;

    //Width & height based off of tiles.
    protected int width = 0;
    protected int height = 0;

    protected GameImage graphic = null;

    public String realm = "undefined";

    public Item(){
        grid = ResolutionManager.getGrid().get();
    }


    //updateLocation will refresh the x & y coordinates based off of a tile/grid x & y coordinates.
    //Precondition: input a x & y tile location
    //Post-condition: updates x & y of object multiplied by the grid size;
    public void updateLocation(int inputX, int inputY){
        setLayoutX(inputX*grid);
        setLayoutY(inputY*grid);
        x = inputX;
        y = inputY;
    }
    public GameImage setGraphic(String ...inputs){
        graphic = new GameImage(inputs);
        if(!this.getChildren().contains(graphic)){
            this.getChildren().add(graphic);
        }
        return graphic;
    }
    public GameImage setGraphic(Image ...inputs){
        graphic = new GameImage(inputs);
        if(!this.getChildren().contains(graphic)){
            this.getChildren().add(graphic);
        }
        return graphic;
    }

    public ImageView getGraphic(){
        return graphic;
    }

    //This function is abstract and is used for child objects to update their variables.
    public abstract void everyFrame();
    public int getTileX(){
        return x;
    }
    public int getTileY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public void loadSave(String save){
        super.loadSave(save);
        realm = findTag("realm", save);
        x = Integer.valueOf(findTag("x", save));
        y = Integer.valueOf(findTag("y", save));
        width = Integer.valueOf(findTag("width", save));
        height = Integer.valueOf(findTag("height", save));
        updateLocation(x, y);
    }
    public String getSave(){
        StringBuilder output = new StringBuilder();
        output.append(super.getSave());
        output.append(createTag("realm", realm));
        output.append(createTag("x", x));
        output.append(createTag("y", y));
        output.append(createTag("width", width));
        output.append(createTag("height", height));
        return output.toString();
    }
    public void startHighlight(){
        //eh
    }
    public void stopHighlight(){
        //eh
    }
}
