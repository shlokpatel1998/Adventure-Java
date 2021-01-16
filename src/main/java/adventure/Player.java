package adventure;

import java.util.ArrayList;

/**
 * This method individualizes every player with instance variables
 * for name, the save name and the list of Item objects named inventory
 */
public class Player implements java.io.Serializable {
    private static final long serialVersionUID = -3788086098781612036L;
    private String name;
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private Room current_room;
    private String saveName = "";
    public Boolean alreadySaved = false;

    /**
     * @Override toString() method
     */
    public String toString() {
        return this.name;
    }

    /**
     * Accessor for the player name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator to set player name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * If this method is called, tells further methods
     * that this filename has already been saved and can save 
     * automatically. Set to true after the adventure is saved
     * for the first time.
     */
    public void setIfAlreadySaved() { 
        this.alreadySaved = true;
    }

    /**
     * @return Boolean representing if player has saved before or not
     */
    public Boolean getIfAlreadySaved() {
        return this.alreadySaved;
    }

    /**
     * @return Array list of item representing the inventory
     */
    public ArrayList<Item> getInventory() {
        return this.inventory;
    }

    /**
     * Mutator to set inventory, one item at a time
     * Adds to the inventory list by using take command
     */
    public void setInventory(Item inventoryItem) {
        this.inventory.add(inventoryItem);
    }

    /**
     * @return Room object of current room
     */
    public Room getCurrentRoom() {
        return current_room;
    }

    /**
     * Mutator sets the current room 
     */
    public void setCurrentRoom(Room thisroom) {
        this.current_room = thisroom;
    }

    /**
     * @return String of name the game was saved as
     */
    public String getSaveGameName() {
        return this.saveName;
    }

    /**
     * Mutator to set the name of the game to save
     */
    public void setSaveGameName(String setter) {
        this.saveName = setter;
    }
}
