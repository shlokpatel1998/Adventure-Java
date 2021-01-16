package adventure;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This method takes input in the form of string and creates
 * an object Command if the input is valid
 */
public class Command implements java.io.Serializable {
    
    private static final long serialVersionUID = -3788086098781612036L;
    private String action;
    private String noun;
    private static ArrayList<String> COMMAND_NOUN = new ArrayList<>(Arrays.asList("S", "N", "E", "W", "UP", "DOWN"));
    private static ArrayList<String> COMMAND_VERB = new ArrayList<>(Arrays.asList("LOOK", "S", "N", "E", "W", "GO", "TAKE", "INVENTORY"));
    
    /**
     * Default constructor
     * @throws InvalidCommandException
     */
    public Command() throws InvalidCommandException {
        this(null, null);
    }

    /**
     * Creates a command object given only an action. this.noun is set to null
     * @param command The first word of the command. 
     */
    public Command(String command) throws InvalidCommandException {
        this.action = command;
        this.noun = null;

        if(this.isValidVerb(command) == false) {
            throw new InvalidCommandException("Verb is not valid");
        }
        if(command.equalsIgnoreCase("GO")) {
            throw new InvalidCommandException("Cannot input GO by itself, follow it by a direction");
        }
        if(command.equalsIgnoreCase("TAKE")) {
            throw new InvalidCommandException("Cannot input TAKE by itself, follow it with a valid item");
        }
    }

    /**
     * Creates a command object given both an action and a noun
     *
     * @param command The first word of the command. 
     * @param what      The second word of the command.
     */
    public Command(String command, String what) throws InvalidCommandException{
        this.action = command;
        this.noun = what;
        if(this.isValidVerb(this.action) == false) {
            throw new InvalidCommandException("Verb is not valid");
        }
        if(this.isValidNoun(this.noun) == false) {
            throw new InvalidCommandException("Noun is not valid");
        }
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     *
     * @return The command word.
     */
    public String getActionWord() {
        return this.action;
    }

    /**
     * @Override toString() method
     * @return String containing action word
     */
    public String toString() {
        return this.getActionWord();
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getNoun() {
        return this.noun;
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord() {
        return (noun != null);
    }

    /**
     * Method validates if the first word is valid
     * @param command represents first word
     * @return Boolean based on if it is valid 
     */
    public Boolean isValidNoun(String command) {
        for(String s: COMMAND_NOUN) {
            if(s.equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method validates if the second word is valid
     * @param command represents second word
     * @return Boolean based on if it is valid 
     */
    public Boolean isValidVerb(String command) {
        for(String s: COMMAND_VERB) {
            if(s.equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to add the item list of names to the 
     * current list of valid nouns (second words)
     * @param items final list of Item objects
     */
    public static void setCommandComparison(ArrayList<Item> items) {
        ArrayList<String> totalitems = new ArrayList<String>();
        for(Item i: items) {
            String eachItemName = i.getName();
            totalitems.add(eachItemName);
        }
        COMMAND_NOUN.addAll(totalitems);
    }
}
