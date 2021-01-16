package adventure;

import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream; 
import java.io.ObjectInputStream; 
import java.io.InputStreamReader;

/**
 * Game class contains the main and executes the entire program,
 * initializing the player and adventure objects that further subdivide 
 * into classes Room and Item.
 */

public class Game implements java.io.Serializable{

    private static final long serialVersionUID = -3788086098781612036L;
    private JSONObject adventure_json;
    private JSONObject first;
    private ArrayList<Room> listOfFinalRooms;
    private ArrayList<Item> listofFinalItems;
    private Adventure thisAdventure;
    private Parser parser = new Parser();
    
    /**
     * Overriden toString() method
     */
    public String toString() {
        return this.thisAdventure.getPlayer().getName();
    }

    /**
     * Method loads the first JSONObject by using a json parser
     * @param filename based on file path
     * @return JSONObject "adventure"
     * @throws Exception
     */
    public JSONObject loadAdventureJson(String filename) throws Exception {
        JSONParser parser = new JSONParser();
        String inputFile = filename.trim();
    
        try (Reader reader = new FileReader(inputFile)) { 
            adventure_json = (JSONObject) parser.parse(reader);
            first = (JSONObject) adventure_json.get("adventure");
            reader.close();
        } catch (Exception e) {
            throw new Exception("Invalid file path: ");
        }
        return first;
    }

    /**
     * Method overloaded with FileInputStream as parameter instead. Calls the loadAdventureJson
     * after it finishes breaking the input stream down. 
     * @param inputStream input byte stream of data
     * @return  JSONObject "adventure"
     * @throws IOException  
     * @throws Exception
     */
    public JSONObject loadAdventureJson(FileInputStream inputStream) throws IOException, Exception {
        String inputAsString = "";
        InputStreamReader reader = new InputStreamReader(inputStream);
        while(reader.read() != -1) {
            String s = Character.toString(reader.read());
            inputAsString += s;
        }
        return loadAdventureJson(inputAsString);
    }

    /**
     * Creates a new Adventure object and it parses through 
     * the JSON object in the Adventure class constructor
     * @param first
     * @return Adventure object
     */
    public Adventure generateAdventure(JSONObject first) {
        Adventure theAdv = new Adventure(first);
        return theAdv;
    }
    
    /**
     * Print statement when input is quit
     */
    private Boolean inputExit() {
        System.out.println("THANK YOU FOR PLAYING! ADIOS!!");
        Boolean leave = true;
        return leave;
    }

    /**
     * Helper method sets up the game lists of final room and item list
     * based on the already initialized room and item list in the Adventure
     * class
     * @param adv
     */
    private void setUpLists(Adventure adv) {
        if(adv != null) {
            this.listOfFinalRooms = adv.listAllRooms();
            this.listofFinalItems = adv.listAllItems();
        }
    }
    
    /**
     * Private method calls submethods based on input
     * @param toDo Command object
     * @param play
     * @throws InvalidCommandException
     */
    private void followCommand(Command toDo, Player play) throws InvalidCommandException{
        boolean two = toDo.hasSecondWord();
        if(toDo.getActionWord().equals("GO") && two == true) {
            printMoveHero(toDo.getNoun());
        }
        else if(toDo.getActionWord().equals("LOOK")) {
            this.lookCommand(toDo);
        }
        else if(two == false && !(toDo.getActionWord().equals("INVENTORY"))) {
            printMoveHero(toDo.getActionWord());
        }
        else if(toDo.getActionWord().equals("TAKE") && two == true) {
            this.takeCommand(toDo, play);
        }
        else if(toDo.getActionWord().equals("INVENTORY") && two == false) {
            this.inventoryCommand(play);
        }
        else { 
            throw new InvalidCommandException("Error in command:");
        }
    }

    /**
     * Method prints output for user that input's look + item
     * @param cmd
     * @throws InvalidCommandException
     */
    private void lookCommand(Command cmd) throws InvalidCommandException{
        if(cmd.hasSecondWord() == true) {
            String result = this.thisAdventure.lookAtSomething(cmd.getNoun());
            System.out.println(result);
        }
        else {
            String printCommand = this.thisAdventure.lookAlone();
            System.out.println("Longer description of room");
            System.out.println(printCommand);
        }
    }

    /**
     * Method prints all items in inventory
     * @param play
     */
    private void inventoryCommand(Player play) {
        ArrayList<Item> i = play.getInventory();
        for(Item item: i) {
            System.out.println(item.getName());
        }
        if(i.size() == 0) {
            System.out.println("No items currently in inventory! Pick up items with \"take\" to add to your inventory");
        }
    }

    /**
     * Method to print the String returned by moveHero in Adventure class
     */
    private void printMoveHero(String inp) throws InvalidCommandException {
        Room room = this.thisAdventure.moveHero(inp);
        Integer currentIndex = this.thisAdventure.getIndex();
        System.out.println(room.getName() + "\n");
        for(Item item: room.listItems()) {
            System.out.println(item.getName());
        } 
    }

    /**
     * Method if user inputs take + item 
     * Adds the item to inventory
     * @param cmd Command object
     * @param play
     * @return Returns a call to a print method
     */
    private Boolean takeCommand(Command cmd, Player play) {
        for(Item i : this.thisAdventure.getCurrentRoom().listItems()) {
            if(i.getName().equalsIgnoreCase(cmd.getNoun())) {
                Item item = this.thisAdventure.getItemBasedOnName(cmd.getNoun());
                play.setInventory(item);
                System.out.println("The following item was added to inventory: " + item.getName());
                return true;
            }
        }
        return printInvalidTake();
    }
    
    /**
     * @return Boolean
     * Prints invalid "take" command
     */
    private Boolean printInvalidTake() {
        System.out.println("Sorry the entered item does not exist in this room!");
        return false;
    }
   
    /**
     * prints the menu
     */
    private static void printMenu() {
        System.out.println("\n\nWELCOME TO ADVENTURE! THIS IS THE STARTING POINT FOR THE GAME MODE CHOSEN");
        System.out.println("1. ENTER LOOK TO SEE THE CONTENTS OF THE ROOM");
        System.out.println("2. ENTER LOOK ALONG WITH THE NAME OF THE ITEM FOR A LONGER DESCRIPTION");
        System.out.println("3. TYPE TAKE ALONG WITH THE ITEM TO ADD ITEMS TO YOUR INVENTORY");
        System.out.println("4. TYPE HELP FOR CLUES");
        System.out.println("5. TYPE INVENTORY TO GET LIST OF ITEMS IN YOUR INVENTORY");
        System.out.println("6. TYPE GO ALONG WITH THE DIRECTION OF CHOICE(N,E,S,W, up, down) TO MOVE THROUGH ROOMS");
        System.out.println("7. TYPE QUIT TO SAVE GAME\n\n");
    }
    
    /**
     * Method calls all methods to create adventure from default
     * @param inputJSON
     * @throws Exception
     */
    private void userDefaultAdventure(String inputJSON) throws Exception {
        JSONObject defaultGameObject = this.loadAdventureJson(inputJSON);
        this.thisAdventure = this.generateAdventure(defaultGameObject); 
        this.setUpLists(this.thisAdventure);
        System.out.println(this.thisAdventure.setStartRoom());
        this.thisAdventure.setPlayer(new Player());
        Command.setCommandComparison(this.thisAdventure.listAllItems());
    }

    /**
     * Method to parse input and return the Command object if valid
     * @param userInput
     * @return
     * @throws InvalidCommandException
     */
    public Command parse(String userInput) throws InvalidCommandException {
        return parser.parseUserInput(userInput);
    }

    /**
     * Method to parse the command line arguements to either load a file or load a saved game
     * @param settings user command line arguements
     * @param scnr Scanner object
     * @param play
     * @throws Exception
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void parseArgs(String settings[], Scanner scnr, Player play) throws Exception, IOException, ClassNotFoundException {
        if(settings[0].equals("-a") && settings.length > 1) {
            parseAlphaCommand(scnr, play, settings[1]);
        }
        else if(settings[0].equals("-l") && settings.length > 1) {
            this.loadSavedAdventure(settings[1]);
            System.out.println("Welcome Back, I TOLD YOU THIS GAME WAS ADDICTING" +  "  " + this.thisAdventure.getPlayer().getName());
            System.out.println(this.thisAdventure.getCurrentRoomDescription());
            this.parseCommands(scnr, play);
        }
        else if(settings.length == 1) {
            parseAlphaCommand(scnr, play, "target/classes/example_adventure.json");
        }
        else {
        }
    }

    /**
     * Helper method to parse -a, by loading default adventure and calling the
     * parsing command method
     * @param scnr
     * @param play
     * @param setDefault
     * @throws Exception
     */
    private void parseAlphaCommand(Scanner scnr, Player play, String setDefault) throws Exception {
        this.userDefaultAdventure(setDefault);
        this.parseCommands(scnr, play);
    }

    /**
     * Method prints all viable commands for user
     */
    public void printHelp() {
        System.out.println(parser.allCommands());
    }   

    /**
     * Method parses the input after parsing or loading game
     * Do while loop executes until quit entered
     * @param scnr
     * @param play
     * @throws Exception
     */
    public void parseCommands(Scanner scnr, Player play) throws Exception {
        boolean exit = false;
        do {
            try{
                System.out.print("> ");
                String inp = scnr.nextLine(); 
                if(inp.equalsIgnoreCase("quit")) {
                    inputIsQuit(scnr, play);
                    return;
                }
                else if(inp.equalsIgnoreCase("help")) {
                    this.printHelp();
                }
                else if(this.thisAdventure.getPlayer() != null) {
                    Command c = this.parse(inp);
                    this.followCommand(c, this.thisAdventure.getPlayer());
                }
                else{
                    Command c = this.parse(inp);
                    this.followCommand(c, play);
                }
            } catch(InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        } while (exit == false);
    }

    /**
     * Method to save file if user inputs quit
     * If game is already saved, it automatically saves it 
     * under the same name. If not it prompts the user for
     * a save file name and a name for the player.
     * @param scnr
     * @param play
     * @throws Exception
     */
    private void inputIsQuit(Scanner scnr, Player play) throws Exception{
        if(this.thisAdventure.getPlayer().getIfAlreadySaved()) {
            String temp = this.thisAdventure.getPlayer().getSaveGameName();
            System.out.println("Thank you for playing, your file has been saved!");
            this.saveExistingAdventure(temp);
        }
        else {
            this.saveAdventure(scnr, play);
        }
    }

    /**
     * Method either saves the file under a name or exits 
     * @param scan
     * @param play
     * @throws Exception
     */
    public void saveAdventure(Scanner scan, Player play) throws Exception {
        if(askUserForSave(scan).equalsIgnoreCase("y")) {
            scan.nextLine();
            String nameOfFile = askForFileName(scan);
            String nameOfPlayer = askForPlayerName(scan);
            this.setPlayerObject(play, nameOfFile, nameOfPlayer);
            this.generateSaveFile(nameOfFile);
            return;
        }
        else {
            System.out.println("Thank you for playing!");
            return;
        }
    }

    /**
     * Helper method to set the Player object and set it to Adventure
     * Therefore one can save the Adventure object and it will include 
     * the Player object (to get Inventory after loading a saved game)
     * @param person
     * @param save
     * @param namePlayer
     */
    private void setPlayerObject(Player person, String save, String namePlayer){
        person.setCurrentRoom(this.thisAdventure.getCurrentRoom());
        person.setName(namePlayer);
        person.setSaveGameName(save);
        person.setIfAlreadySaved();
        this.thisAdventure.setPlayer(person);
    }

    /**
     * Method to save an existing file
     * @param existingname
     * @throws Exception
     */
    private void saveExistingAdventure(String existingname) throws Exception {
        this.generateSaveFile(existingname);
    }
    
    /**
     * Method to ask for file name from user
     * @param scan
     * @return
     */
    private String askForFileName(Scanner scan) {
        System.out.println("Name your save file please: ");
        String name = scan.nextLine();
        return name;
    }

    /**
     * Method to ask user for player name
     * @param scan
     * @return
     */
    private String askForPlayerName(Scanner scan) {
        System.out.println("Enter your player name: ");
        String name = scan.nextLine();
        return name;
    }

    /**
     * Method to ask user if they would like to save progress
     * @param scan
     * @return
     */
    private String askUserForSave(Scanner scan) {
        System.out.println("Would you like to save your progress? (y/n)");
        String answer = scan.next().toLowerCase();
        return answer;
    }

    /**
     * Method to deserialize the saved file
     */
    public void loadSavedAdventure(String fileName) throws Exception{
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)); ) { 
            this.thisAdventure = (Adventure) in.readObject(); 
            Command.setCommandComparison(this.thisAdventure.listAllItems()); 
            System.out.println("File has been loaded!"); 
        } catch(IOException io) {
            throw io;
        } catch(ClassNotFoundException ex) {
            throw ex;
        } catch(Exception e) {
            throw e;
        }
    }

    /**
     * Method to generate a save file
     * @param nameOfSave
     * @throws Exception
     */
    public void generateSaveFile(String nameOfSave) throws Exception {
        try {
            FileOutputStream outPutStream = new FileOutputStream(nameOfSave); 
            ObjectOutputStream outPutDest = new ObjectOutputStream(outPutStream); 
            outPutDest.writeObject(this.thisAdventure);
            outPutDest.close();
            outPutStream.close();
            System.out.println("The file has been saved successfully");
            return;
        } catch(Exception ex) {
            throw ex;
        }
    }

    public static void main(String args[]){
        Game theGame = new Game();
        Parser par = new Parser();
        Player player = new Player();
        Command nextCommand = null;
        Boolean validInput = false;
        Scanner scnr = new Scanner(System.in);
        printMenu();
        do {
            System.out.print("> ");
            try {
                theGame.parseArgs(args, scnr, player);
                validInput = true;
            }
            catch(InvalidCommandException ice) {
                System.out.println(ice.getMessage());
            }
            catch (Exception e) {
                System.out.println("Exception occured:");
                System.out.println(e.getMessage() + " Please rerun the JAR");
                return;
            }
        } while(validInput == false);
        scnr.close();
    }
}