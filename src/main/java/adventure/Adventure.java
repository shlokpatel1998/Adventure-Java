package adventure;

import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * The adventure class stores all the information a player
 * would need to play the adventure. This class sets the 
 * rooms, items, and connections, which are used by the
 * game object. The Player object instance variable holds
 * the object to get inventory list.
 */
public class Adventure implements java.io.Serializable{
    
    private static final long serialVersionUID = -3788086098781612036L;
    private String roomDescription;
    private ArrayList<Room> finalRooms = new ArrayList<Room>();
    private ArrayList<Item> finalItems = new ArrayList<Item>();
    private JSONArray item_list;
    private JSONArray room_list;
    private Integer index;
    private Player player;

    public Adventure() {
    }
    
    /**
     * Constructor to set up rooms with items and connections
     * @param init : JSONObject "adventure"
     */
    public Adventure(JSONObject init) {
        setListOfItems((JSONArray) init.get("item"));
        setListOfRooms((JSONArray) init.get("room"));
        addListOfItems();
        addRoomWithItems();
        setItemsToContainingRoom();
        setStartID();
        setHashMapInRoom();
    }
    
    /**
     * @Override toString() method
     */
    public String toString() {
        return this.getCurrentRoomDescription();
    }

    /**
     * Mutator to set player object
     * @param play : Player object
     */
    public void setPlayer(Player play) {
        this.player = play;
    }

    /**
     * Accessor to get player object
     * @return Player object
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Method sets startID in room with "start"
     * Used to initialize starting Room
     */
    private void setStartID() {
        for(Object room: room_list) {
            JSONObject r = (JSONObject) room;
            Long id = (Long) r.get("id");

            if((String) r.get("start") != null) {
                for(Room i: finalRooms) {
                    if(i.getID().equals(id)) {
                        i.setStartID(id);
                    }
                }
            }
        } 
    }

    /**
     * Method returns current index of room
     * @return Integer 
     */
    public Integer getIndex() {
        return this.index;
    }

    /**
     * Mutator to set the index
     * Used by moveHero to update index when moving rooms
     * @param num
     */
    private void setIndex(Integer num) {
        this.index = num;
    }

    /**
     * 
     * @return String to main in Game.java to print start of adventure
     * @throws Exception
     */
    public String setStartRoom() throws Exception {
        int index = 0;
        for(Room r: finalRooms) {
            if(r.getStartID() != null) {
                this.index = index;
                String out = "THE START OF YOUR ADVENTURE WILL BE IN: " + r.getName();
                return out;
            }
            index++;
        }
        throw new Exception("Cannot find start of adventure");
    }

    /**
     * Helper method to set hashmap in each room
     * Use parseEntrance method in room to parse entrances/exits
     */
    private void setHashMapInRoom() {
        for(Room eachRoom: finalRooms) {
            eachRoom.parseEntrance(this.finalRooms);
        }
    }

    /**
     * Helper method to set the final list of all items
     * into an ArrayList of Item objects
     */
    private void addListOfItems() {
        for(Object item_current : item_list) {
            JSONObject item_current_object = (JSONObject) item_current;
            Item itemToAdd = new Item(item_current_object);
            this.finalItems.add(itemToAdd);
        }
    }

    /**
     * Mutator to set json array of items
     * @param initItem
     */
    public void setListOfItems(JSONArray initItem) {
        this.item_list = initItem;
    }

    /**
     * Mutator to set json array of rooms
     * @param initRoom
     */
    public void setListOfRooms(JSONArray initRoom) {
        this.room_list = initRoom;
    }

    /**
     * Helper method to add new Room for every room
     * along with the items in the room (based on "loot")
     */
    public void addRoomWithItems() {
        for(Object r: this.room_list) {
            JSONObject room = (JSONObject) r;
            Room roomtoAdd = new Room(room);
            if((JSONArray) room.get("loot") != null) {
                roomtoAdd.setListItems(this.setItemsInRoom((JSONArray) room.get("loot")));
                this.finalRooms.add(roomtoAdd);
            }
            else{
                this.finalRooms.add(roomtoAdd);
            }
        }
    }

    /**
     * 
     * @param lootlist json Array of the loot in each room
     * @return array list of item objects in the specified room
     */
    private ArrayList<Item> setItemsInRoom(JSONArray lootlist) {
        ArrayList<Item> itemsList = new ArrayList<Item>();
        for(Object currentLoot : lootlist) {
            JSONObject loot = (JSONObject) currentLoot;
            Long lootID = (Long) loot.get("id");

            for(Item i: finalItems) {
                if(i.getItemID().equals(lootID)) {
                    itemsList.add(i);  
                }
            }
        }
        return itemsList; 
    }

    /**
     * Helper method to simply set each item in each room to itself
     * Therefore one can call the containing room for each item
     */
    private void setItemsToContainingRoom(){ 
        for(Room r: finalRooms) {
            for(Item i: r.listItems()) {
                i.setContainingRoom(r);
            }
        }
    }

    /**
     * 
     * @return the list of all rooms
     */
    public ArrayList<Room> listAllRooms(){
        return finalRooms;
    }

    /**
     * 
     * @return the list of all items
     */
    public ArrayList<Item> listAllItems(){
        return finalItems;
    }

    /**
     * 
     * @return description of the current room
     */
    public String getCurrentRoomDescription(){
        return this.finalRooms.get(this.index).getLongDescription();
    }

    /**
     * 
     * @return the Room object for the current room
     */
    public Room getCurrentRoom() {
        return this.finalRooms.get(this.index);
    }
    
    /**
     * This method gets the connected room based on a directional string input
     * @param movement String representing a direction
     * @return Room after getConnectedRoom(a direction)
     * @throws InvalidCommandException 
     */
    public Room moveHero(String movement) throws InvalidCommandException {
        if(movement.equalsIgnoreCase("UP") | movement.equalsIgnoreCase("DOWN")) {
            movement = movement.toLowerCase();
        }
        if(this.finalRooms.get(this.getIndex()).getConnectedRoom(movement) != null) {
            int i = 0;
            Room room = this.finalRooms.get(this.getIndex()).getConnectedRoom(movement);
            for(Room r: this.finalRooms) {
                if(room == r) {
                    this.setIndex(i);
                    return room;
                }
                i++;                   
            }
        }
        else {
            throw new InvalidCommandException("Direction invalid");
        }
        return null;
    }
    
    /**
     * 
     * @param itemToLook String representing item name to view
     * @return  String with item long description
     * @throws InvalidCommandException
     */
    public String lookAtSomething(String itemToLook) throws InvalidCommandException {
        ArrayList<Item> temp = this.finalRooms.get(this.index).listItems();
        itemToLook = itemToLook.toLowerCase();
        for(Item i: temp) {
            if(i.getName().equals(itemToLook)) {
                return i.getLongDescription();
            }
        }
        throw new InvalidCommandException("Item does not exist in room");
    }

    /**
     * @return String with current room's long description
     */
    public String lookAlone() {
        return this.finalRooms.get(this.index).getLongDescription();
    }

    /**
     * @param input String input of item name
     * @return Item if the input matches an item name
     */
    public Item getItemBasedOnName(String input) {
        for(Item i : finalItems) {
            if(i.getName().equalsIgnoreCase(input)) {
                return i;
            }
        }
        return null;
    }
}