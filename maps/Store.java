package maps;

import attributes.GameImage;
import attributes.Item;
import attributes.ResolutionManager;
import dataObjects.Player;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modules.MapEngine;

/**
 * Created by William on Nov-14-2014.
 */
public abstract class Store extends Group{
    protected Group catalog = new Group();
    public VBox catalogList = new VBox(ResolutionManager.getGrid().get());

    private boolean displayToggle = false;
    private GameImage graphic = new GameImage("assets/original/CatalogGraphic.png", "assets/low/CatalogGraphic.png");
    private GameImage icon = new GameImage("assets/original/shoppingCartButton.png", "assets/low/shoppingCartButton.png");

    public Store(){
        setLayoutX(0);
        setLayoutY(0);
        catalog.setLayoutX(0);
        catalog.setLayoutY(0);
        int catalogX = ResolutionManager.getGrid().get()*3;
        int catalogY = ResolutionManager.getGrid().get()*4;
        this.getChildren().add(icon);
        catalogList.setLayoutX(catalogX);
        catalogList.setLayoutY(catalogY);
        graphic.setLayoutX(ResolutionManager.getGrid().get());
        graphic.setLayoutY(ResolutionManager.getGrid().get());
        icon.setLayoutX(ResolutionManager.getGrid().get()*.5);
        icon.setLayoutY(ResolutionManager.getHeight().get() - ResolutionManager.getGrid().get()*.5 - icon.getImage().getHeight());

        icon.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (displayToggle) {
                close();
            } else {
                display();
            }
        });
        catalog.getChildren().addAll(graphic, catalogList);
        this.getChildren().add(catalog);
    }

    public abstract void buildCatalog();
    protected Node addItemToCatalog(final Class input, final double cost){
        HBox output = new HBox();
        Item thisItem;
        try {
            thisItem = (Item) (input.newInstance());
        }catch(Exception e){
            System.out.print(e.toString());
            return null;
        }
        final VBox purchase = new VBox(ResolutionManager.getGrid().get()*.1);
        output.setSpacing(ResolutionManager.getGrid().get());
        Label object = new Label(thisItem.name + ".");
        object.setStyle(ResolutionManager.textStyle[0]);
        Button price = new Button("Buy $" + cost + ".");
        Label errors = new Label("Not enough funds\nYou need $" + String.format("%.2f", cost - Player.money.get()) + " more.");
        price.setStyle(ResolutionManager.getButtonStyle().get());
        errors.setMaxWidth(ResolutionManager.getGrid().get() * 7);
        errors.setWrapText(true);
        errors.setStyle(ResolutionManager.textStyle[0]);
        if(Player.money.get() >= cost){
            errors.setText("After purchase you will have: $" + String.format("%.2f", Player.money.get() - cost));
        }
        if(Player.money.get() >= cost){
            price.setOnAction((e)->{
                close();
                ((MapEngine)(getParent())).placingNewItem(input);
                Player.money.set(Player.money.get() - cost);
            });
        }
        purchase.getChildren().addAll(object, price);
        output.getChildren().addAll(thisItem.getGraphic(), purchase, errors);
        catalogList.getChildren().add(output);
        return output;
    }
    private void close(){
        //catalogList.getChildren().clear();
        this.getChildren().remove(catalog);
        displayToggle = false;
    }
    private void display(){
        if(!this.getChildren().contains(catalog))
            this.getChildren().add(catalog);
        getParent().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (displayToggle) {
                    close();
                }
                getParent().removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
            }
        });
        //catalogList.getChildren().clear();
        buildCatalog();
        displayToggle = true;
    }
}
