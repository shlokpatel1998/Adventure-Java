package adventure;

import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.HashMap;

/**
 * Room class holds all the information a room contains including
 * identifiers and the list of Item objects in the specific room
 */
public class Room implements java.io.Serializable{
    
    private static final long serialVersionUID = -3788086098781612036L;
    private String roomName;
    private String shortDescription;
    private String longDescription;
    private Long roomID;
    private JSONObject roomObject;
    private HashMap<String, Room> hash = new HashMap<String, Room>();
    private ArrayList<JSONObject> jsonEntranceList;
    private ArrayList<Room> roomEntranceList;
    private ArrayList<Item> listOfItems = new ArrayList<Item>();
    private Long startID;

    /**
     * Default constructor for Room object
     */
    public Room() {        
    }

    /**
     * Room constructor that deconstructs JSONObject for each room
     * @param room JSONObject for room
     */
    public Room(JSONObject room) {
        this.roomObject = room;
        this.roomID = (Long) room.get("id");
        this.roomName = (String) room.get("name");
        this.shortDescription = (String) room.get("short_description");
        this.longDescription = (String) room.get("long_description");
    }

    /**
     * @Override toString() method
     */
    public String toString() {
        return this.roomName;
    }

    /**
     * @return the start ID if this is the starter room
     */
    public Long getStartID() {
        return startID;
    }

    /**
     * Sets the startID if the room object has "start"
     * @param startingLocation
     */
    public void setStartID(Long startingLocation) {
        startID = startingLocation;
    }

    /**
     * @return list of Item objects in the room
     */
    public ArrayList<Item> listItems() {
        return listOfItems;
    }

    /**
     * Sets the list of Item objects in room
     */
    public void setListItems(ArrayList<Item> list) {
        this.listOfItems = list;
    }

    /**
     * @return String of the room name
     */
    public String getName() {          
        return roomName;
    }

    /**
     * @return Long of the room id
     */
    public Long getID() {
        return roomID;
    }

    /**
     * @return String of the room short description
     */
    public String getShortDescription() {       
        return shortDescription;
    }

    /**
     * @return String of the room long description
     */
    public String getLongDescription() {         
        return longDescription;    
    }

    /**
     * Method parsrs through each Room object's "entrance" array and
     * uses it to setHashMapConnection with the direction that is connected
     * and the room associated with "id".
     * @param list of Room object
     */
    public void parseEntrance(ArrayList<Room> rooms) {
        JSONArray elist = ((JSONArray) this.roomObject.get("entrance"));
        for(Object i: elist) {
            JSONObject obj = (JSONObject) i;
            String dir = (String) obj.get("dir");
            Long id = (Long) obj.get("id");
            for(Room r: rooms) {
                if(r.getID().equals(id)) {
                    setHashMapConnection(r, dir);
                }
            }  
        }
    }

    /**
     * Helper method to populate the HashMap with direction and Room object
     * @param roomInConnection
     * @param direction
     */
    private void setHashMapConnection(Room roomInConnection, String direction) {
        this.hash.put(direction, roomInConnection);
    }

    /**
     * 
     * @param direction String with movement
     * @return Room connected to the movement 
     * @throws InvalidCommandException
     */    
    public Room getConnectedRoom(String direction) throws InvalidCommandException {
        if(hash.containsKey(direction)) {
            return hash.get(direction);
        }
        else {
            throw new InvalidCommandException("No room in that direction: try again");
        }
    }
}