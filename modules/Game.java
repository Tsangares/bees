package modules;

import attributes.Item;
import attributes.ResolutionManager;
import dataObjects.Hive;
import dataObjects.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.util.Duration;
import maps.Grassland;
import maps.Hiveland;



/**
 * Created by William on 27-Oct-14.
 */
public class Game extends Scene {
    //Refresh rate, currently set to 60 fps.
    public static int frameRate = 5;
    private Duration timeStamp = Duration.millis(1000/frameRate);

    //This creates the thread game loop.
    private Timeline myTimeline;

    //This will contain the function for the game loop.
    private KeyFrame myLoop;

    //This is the map class for the Game class. In theory this can change.
    public MapEngine mainMap;

    public Group container = new Group();
    private Group outputGroup = new Group();

    //Intro
    private Intro menu;

    //Creates the right hand display window.
    public SideBar sideBarPanel;

    //Saves
    public SaveEngine save;

    public Game() {
        super(new Group());
        this.setRoot(outputGroup);
        ResolutionManager.updateResolution();

        menu = new Intro();
        outputGroup.getChildren().addAll(menu);
        save = menu.loadIntro(this);
        sideBarPanel = new SideBar(this);
    }
    public void startIntro(){
        menu = new Intro();
        save = menu.loadIntro(this);
    }
    public void changeMap(Item containingObject){
        container.getChildren().remove(mainMap);
        if(containingObject.getClass().equals(Hive.class)) {
            mainMap = new Hiveland();
            ((Hiveland)(mainMap)).setHive(((Hive)(containingObject)));
        }
        mainMap.initialize(save.saveList);
        container.getChildren().add(mainMap);
        mainMap.toBack();
    }
    public boolean initialize(){
        outputGroup.getChildren().remove(menu);
        outputGroup.getChildren().add(container);
        System.out.print(Grassland.class);
        mainMap = (Grassland)(save.findFirstObject(Grassland.class));
        if(mainMap == null){
            mainMap = new Grassland();
            save.saveList.add(mainMap);
        }
        //Maybe this is a way.
        mainMap.initialize(save.saveList);
        sideBarPanel.changeFocus(mainMap.focusObj);
        container.getChildren().addAll(mainMap, sideBarPanel);
        mainMap.focusObj.addListener((e)->sideBarPanel.changeFocus(mainMap.focusObj));
        //Defining the function for the game loop.
        myLoop = new KeyFrame(timeStamp, (e) -> {
            Player.frame.set(Player.frame.get()+1);
            Player.clock.update(Player.frame.get());
            if(Player.frame.get()%60 == 0){
                save.setSave();
            }
        });

        //Building the game loop.
        myTimeline = new Timeline(myLoop);
        myTimeline.setCycleCount(Animation.INDEFINITE);
        myTimeline.play();

        return true;
    }
}
