package adventure;

import adventure.Adventure;
import adventure.InvalidCommandException;

/**
 * Parser class parses the user input from Game.java and creates a
 * Command object to verify the user input.
 */
public class Parser {

    /**
     * Default constructor
     */
    public Parser() {
    }

    /**
     * @Override toString() method
     */
    public String toString() {
        return "Parser object";
    }

    /**
     * Method for multiple parted input (verb + noun)
     * @param userInput The entire line of input that is seperated into verb and noun
     * @param verb  First word of input
     * @param parts Split the input into parts 
     * @return  Command object 
     * @throws InvalidCommandException
     */
    private Command verbPlusNoun(String userInput, String verb, String parts[])  throws InvalidCommandException{
        try { 
            String noun = userInput.substring(parts[0].length(), userInput.length());
            noun = noun.trim().replaceAll("\\s+", " ");
            Command cmd = new Command(verb, noun);
            return cmd;
        } catch(InvalidCommandException e) {
            throw new InvalidCommandException(e.getMessage());
        }
    }

    /**
     * 
     * @param userInput Used to set Command object
     * @return  Command object
     * @throws InvalidCommandException
     */
    private Command onlyNoun(String userInput) throws InvalidCommandException {
        try {
            Command cmd = new Command(userInput);
            return cmd;
        } catch(InvalidCommandException e) {
            throw new InvalidCommandException(e.getMessage());
        }
    }
    
    /**
     * Method parses the userInput into eith verb+noun or just verb
     * @param userInput
     * @return Command object
     * @throws InvalidCommandException
     */
    public Command parseUserInput(String userInput) throws InvalidCommandException {
        userInput = userInput.trim().toUpperCase();
        String parts[] = userInput.split("\\s+");
        String verb = parts[0];
        if(parts.length > 1) {
            Command cmd = verbPlusNoun(userInput, verb, parts);
            return cmd;
        }
        else if(parts.length == 1) {
            Command cmd = onlyNoun(verb);
            return cmd;   
        }
        else{
            throw new InvalidCommandException("Error in input: ");
        }
    }

    /**
     * Method called when user needs help with game possibilities
     * @return String of commands
     */
    public String allCommands() {
        String help = "YOU CAN USE THESE! > \n-go + direction\n-look\n-look + item\n-take + item\n-inventory";
        return help;
    }
}
