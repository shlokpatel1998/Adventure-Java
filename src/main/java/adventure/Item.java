package adventure;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Item class holds the information for each Item and
 * the containing room.
 */
public class Item implements java.io.Serializable {
   
    private static final long serialVersionUID = -3788086098781612036L;
    private String nameItem;
    private String nameItemLongDes;
    private Long itemID;
    private Room containingRoom;

    /**
     * Default constructor
     */
    public Item() {
    }
    
    /**
     * Constructor decomposes JSONObject for item and initializes the 
     * private fields
     */ 
    public Item(JSONObject item_current_object) {
        this.itemID = (Long) item_current_object.get("id");
        this.nameItem = (String) item_current_object.get("name");
        this.nameItemLongDes = (String) item_current_object.get("desc");
    }

    /**
     * Overrides the toString() method
     */
    public String toString() {
        return this.nameItem;
    }

    /**
     * @return String name of item
     */
    public String getName() {           
        return nameItem;
    }

    /**
     * Mutator sets the item name
     */
    public void setName(String item){     
        nameItem = item;
    }

    /**
     * @return long description of item
     */
    public String getLongDescription(){    
        return nameItemLongDes;
    }

    /**
     * mutator set long description
     */
    public void setLongDescription(String itemLongDes){     
        nameItemLongDes = itemLongDes;
    }

    /**
     * Mutator set Item id
     */
    public void setItemID(Long id) {
        itemID = id;
    }
 
    /**
     * @return Long item id
     */
    public Long getItemID() {
        return itemID;
    }

    /**
     * returns containing Room object
     */
    public Room getContainingRoom(){
        return containingRoom;
    }

    /**
     * Sets Room object to the Item
     */
    public void setContainingRoom(Room room) {
        containingRoom = room;
    }
}