
/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling Version: 1.1 Date: March 2000
 * 
 * This class is the main class of the "Zork" application. Zork is a very
 * simple, text based adventure game. Users can walk around some scenery. That's
 * all. It should really be extended to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates the commands that
 * the parser returns.
 */

import java.util.Scanner;

class Game
{
	private Parser parser;
	private Room currentRoom;
	private Inventory inv = new Inventory(15);
	private Inventory armor = new Inventory (3);
	private Inventory weapons = new Inventory(2);

	/**
	 * Create the game and initialize its internal map.
	 */
	public Game()
	{
		createRooms();
		parser = new Parser();
	}

	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms()
	{
		Room outside, lab, tavern, gblock, office;

		// create the rooms
		outside = new Room("outside G block on Peninsula campus");
		lab = new Room("a lecture theatre in A block");
		tavern = new Room("the Seahorse Tavern (the campus pub)");
		gblock = new Room("the G building");
		office = new Room("the computing admin office");

		// Initialize room exits
		outside.setExits(null, lab, gblock, tavern);
		lab.setExits(null, null, null, outside);
		tavern.setExits(null, outside, null, null);
		gblock.setExits(outside, office, null, null);
		office.setExits(null, null, null, gblock);

		// Set Items to inventory
//		outside.getInv().add(new Item("Gate Key", 0, 0, 7));
		Inventory disInv = outside.getInv();
		Item dis = new Item("Gate Key", 0, 0, 7);
		disInv.add(dis);
		
		
		currentRoom = outside; // start game outside
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play()
	{
		printWelcome();

		// Enter the main command loop. Here we repeatedly read commands
		// and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished)
		{
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Thank you for playing.  Good bye.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome()
	{
		System.out.println();
		System.out.println("Welcome to Zork!");
		System.out.println("Zork is a new, incredibly boring adventure game.");
		System.out.println("Type 'help' if you need help.");
		System.out.println();
		System.out.println(currentRoom.longDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command. If this command
	 * ends the game, true is returned, otherwise false is returned.
	 * 
	 * @return true or false, whether the game is finished
	 * @param command
	 *            A command
	 */
	private boolean processCommand(Command command)
	{
		if (command.isUnknown())
		{
			System.out.println("I don't know what you mean...");
			return false;
		}

		String commandWord = command.getCommandWord();
		if (commandWord.equals("help")){
			printHelp();
			pickUpUI();
		}
		else if (commandWord.equals("go"))
		{
			inv.add(new Item("Gate Key", 0, 0, 7));
			goRoom(command);
		}
		else if (commandWord.equals("quit"))
		{
			if (command.hasSecondWord())
				System.out.println("Quit what?");
			else
				return true; // signal that we want to quit
		}
		return false;
	}

	// implementations of user commands:

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void printHelp()
	{
		System.out.println("You are lost. You are alone. You wander");
		System.out.println("around at Monash Uni, Peninsula Campus.");
		System.out.println();
		System.out.println("Your command words are:");
		parser.showCommands();
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 */
	private void goRoom(Command command)
	{
		if (!command.hasSecondWord())
		{
			// if there is no second word, we don't know where to
			// go...
			System.out.println("Go where?");
			return;
		}

		String direction = command.getSecondWord();

		// Try to leave current room.
		Room nextRoom = currentRoom.nextRoom(direction);

		if (nextRoom == null)
			System.out.println("There is no door!");
		else
		{
			currentRoom = nextRoom;
			System.out.println(currentRoom.longDescription());
		}
	}

	/**
	 * Picks up an item from the current Room (with UI)
	 */
	public void pickUpUI()
	{
		Scanner input = new Scanner(System.in);
		int index = -1;
		do
		{
			System.out.println("Which item would you like to pick up? (-1 if you're done)");
			currentRoom.getInv().display();
			int itemIndex = input.nextInt();
			if (itemIndex >0 && itemIndex < currentRoom.getInv().getSize())
				inv.pickUp(currentRoom.getInv(), itemIndex);
		} while (index >= 0);
	}
}
