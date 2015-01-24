package modules;

import attributes.Item;
import attributes.ResolutionManager;
import attributes.SavableObject;
import dataObjects.Player;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.lang.Long;
import java.util.Vector;

/**
 * Created by William on 27-Oct-14.
 *
 */

//The classes Item Tile & BackgroundEngine all require the variable gird from this class.
public abstract class MapEngine extends SavableObject {
    //Constants defined in the hi-resolution version of the game.
    //Constants for low res has yet to be defined explicitly. See notes for details.
    public final static int grid = ResolutionManager.getGrid().get();

    //Scroll & Mouse Interactivity
    protected boolean isRightButtonDown = false;
    protected double prevMouseX = 0;
    protected double prevMouseY = 0;

    //Frame/Time:
    public Long frame;

    //This is the dynamic focus object.
    public ReadOnlyObjectWrapper<Item> focusObj;

    //This contains all items in the map.
    protected Group objectHolder = new Group();

    public BackgroundBuilder backgroundHolder;
   // protected Item objectToBePlaced = null;

    //This is a list of all items that need to be updated.
    //This needs to be made instead of the list in objectHolder
    //Because... i can fix that.
    public Vector<SavableObject> saveList = null;

    public Vector<Item> itemList = new Vector<Item>();

    public Player player = null;
    public abstract void constructor();
    public void initialize(Vector<SavableObject> save){
        saveList = save;
        for(SavableObject undefinedItem:saveList){
            if(undefinedItem.getClass().equals(Player.class)) {
                player = ((Player) (undefinedItem));
                focusObj = new ReadOnlyObjectWrapper<>(player);
                break;
            }else if(undefinedItem.getClass().equals(Item.class)){
                Item currentItem = ((Item)(undefinedItem));
                if(currentItem.realm.equals(name)) {
                    itemList.add(currentItem);
                    if (!checkItem(currentItem) || !addItem(currentItem)) {
                        System.out.print("\nItem was not added: " + currentItem.toString() + "\n");
                    }
                }
            }
        }
        SaveEngine.loadSpecific(player);

        constructor();

        if(backgroundHolder != null && objectHolder != null) {
            getChildren().addAll(backgroundHolder, objectHolder);
        }else{
            System.out.print("Background is null before map initialization.");
        }
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            int thisX = (int)(e.getX()/grid);
            int thisY = (int)(e.getY()/grid);
            focusOn(thisX, thisY);
        });
        backgroundHolder.toBack();
    }


    //This function "focusOn" is the interactivity of the display panel.
    //When an object is clicked on it will become the new focus object.
    //The default focus object is the player. When nothing is selected, the player is.
    public Item focusOn(int inputX, int inputY){
        for(Item temp:itemList){
            if (temp.getWidth() == 0 || temp.getHeight() == 0){
                throw(new Error("UNINITIALIZED ITEM. NO WIDTH OR HEIGHT."));
            }else if(inputX>=temp.getTileX() && inputY>=temp.getTileY()&&
                    inputX <= temp.getTileX()+temp.getWidth() && inputY <= temp.getTileY()+temp.getHeight()){
                focusObj.set(temp);
                return temp;
            }
        }
        focusObj.set(player);
        return null;
    }

    public void placingNewItem(Class input){
        final Item thisItem;
        try {
            thisItem = (Item) (input.newInstance());
        }catch(Exception e){
            System.out.print(e.toString());
            return;
        }
        getChildren().add(thisItem);
        thisItem.updateLocation(-100,-100);
        final EventHandler<MouseEvent> movementEffect = (e)-> {
            int thisX = (int)(e.getX()/grid);
            int thisY = (int)(e.getY()/grid);
            thisItem.setLayoutX(thisX*grid);
            thisItem.setLayoutY(thisY*grid);
            //if(checkItem(thisItem, thisX, thisY)) {
                //Animation
            //}
        };
        final EventHandler<MouseEvent> clickEffect = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int thisX = (int)(e.getX()/grid);
                int thisY = (int)(e.getY()/grid);
                if(!e.isSecondaryButtonDown() && checkItem(thisItem, thisX, thisY)) {
                    getChildren().remove(thisItem);
                    addItem(thisItem, thisX, thisY);
                    removeEventHandler(MouseEvent.MOUSE_MOVED, movementEffect);
                    removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
                }
            }
        };
        addEventHandler(MouseEvent.MOUSE_MOVED, movementEffect);
        addEventHandler(MouseEvent.MOUSE_CLICKED, clickEffect);
    }
    public boolean checkItem(Item inputItem){
        return checkItem(inputItem, inputItem.getTileX(), inputItem.getTileY());
    }
    public boolean checkItem(Item inputItem, int inputX, int inputY){
        for(int w = 0; w < inputItem.getWidth(); ++w){
            for(int h = 0; h < inputItem.getHeight(); ++h){
                if(backgroundHolder == null){
                    return false;
                }
                if(!backgroundHolder.checkEmpty(inputX + w, inputY + h)){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean addItem(Item inputItem){
        return addItem(inputItem, inputItem.getTileX(), inputItem.getTileY());
    }
    public boolean addItem(Item inputItem, int inputX, int inputY){
        inputItem.updateLocation(inputX, inputY);
        if(!objectHolder.getChildren().contains(inputItem))
            objectHolder.getChildren().add(inputItem);
        if(!itemList.contains(inputItem))
            itemList.add(inputItem);
        for(int w = 0; w < inputItem.getWidth(); ++w){
            for(int h = 0; h < inputItem.getHeight(); ++h){
                if(backgroundHolder == null){
                    return false;
                }
                if(!backgroundHolder.addItem(inputItem, inputX+w, inputY+h)){
                    return false;
                }
            }
        }
        inputItem.realm = this.name;
        return true;
    }
    public void removeItem(Item inputItem){
        objectHolder.getChildren().remove(inputItem);
        itemList.remove(inputItem);
        for(int w = 0; w < inputItem.getWidth(); ++w){
            for(int h = 0; h < inputItem.getHeight(); ++h){
                if(backgroundHolder!=null)
                     backgroundHolder.removeItem(inputItem.getTileX()+w, inputItem.getTileY()+h);
                else
                    System.out.print("Attempt to remove item from null background.");
            }
        }
    }
}