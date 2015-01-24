package maps;

import attributes.Item;
import attributes.SavableObject;
import javafx.application.Platform;
import modules.BackgroundBuilder;
import modules.EnvironmentManager;
import modules.MapEngine;

import java.util.Vector;

/**
 * Created by William on Dec-12-2014.
 */
/*time: 16:05*/
public class Grassland extends MapEngine {
    public EnvironmentManager weather;
    private Store_Grassland store = new Store_Grassland();
    public void constructor(){
        if(this.name.equals("undefined")){
            this.name = "Grassland";
        }
        backgroundHolder = new BackgroundBuilder(40,40,"grass");
        weather = new EnvironmentManager(this);
        this.getChildren().add(store);
    }
}
