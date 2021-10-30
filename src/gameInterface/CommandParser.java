package gameInterface;

import gameLogic.GameBoard;
import gameLogic.Players;
import gameLogic.GameActions;
import gameLogic.Player;

import java.util.Optional;

/**
 * The gameInterface.CommandParser is where all the commands inputted by the gameLogic.Player is parsed and
 * an action takes place, depending on the command inputted. The game consists of five
 * commands to help the gameLogic.Player to proceed with the game.
 */
public class CommandParser {
    private final GameBoard gameBoard;
    private final Players players;
    private final GameActions gameActions;
    private final GameInterfaceI gameInterface;

    /**This is the constructor of gameInterface.CommandParser with parameters
     * @param gameBoard This is the board in which the gameLogic.Players are playing on
     * @param players These are the list of gameLogic.Players playing the game
     * @param gameInterface This provides text for each action the player takes
     */
    public CommandParser(GameBoard gameBoard, Players players, GameInterfaceI gameInterface) {
        this.gameBoard = gameBoard;
        this.players = players;
        this.gameInterface = gameInterface;
        this.gameActions = new GameActions(gameBoard, players, gameInterface);
    }

    /**
     * This method provides the gameLogic.Players with a list of commands and their functionality
     * to select from.
     */
    private void printHelp() {
        System.out.println(
                """
                        Help----
                        status [id] - Shows player status, prints current player status if id is not supplied
                        roll - Rolls the dice for the current player
                        pass - Passes turn to next player
                        buy - Buy current tile if possible
                        auction - Auction current tile if possible
                        owns [id] - Shows what properties the player owns"""
        );
    }

    /**This method prints the status of a specified gameLogic.Player. Status consists
     * of the gameLogic.Player's ID, balance, tile position, and whether or not the
     * player is in Jail or not.
     * @param player This is the gameLogic.Player who will have their status printed
     */
    private void printPlayerStatus(Player player) {
        Optional<String> tileDescription = gameBoard.getTileDescriptionByIndex(player.getTilePosition());
        if (tileDescription.isPresent()) {
            System.out.println("gameLogic.Player " +
                    player.getPlayerID() +
                    " has a balance of $" +
                    player.getBalance() +
                    " and is on tile:\n" +
                    tileDescription.get());
            if(gameBoard.isPlayerInJail(player)) {
                System.out.println("gameLogic.Player " +
                        player.getPlayerID() +
                        " is in Jail!\nRoll a double to get out of Jail.");
            }
        } else {
            System.out.println("gameLogic.Player " +
                    player.getPlayerID() +
                    " has a balance of $" +
                    player.getBalance() +
                    " and is on tile UNKNOWN");
        }
    }

    /**This method is used to check if the ID provided by a gameLogic.Player is valid or not.
     * This method calls printPlayerStatus() if command is valid, otherwise prints an
     * error message
     * @param command This is the command inputted by the gameLogic.Player
     */
    private void handleStatus(String command) {
        String[] split = command.split(" ");

        Player player = null;
        if (split.length < 2) {
            player = players.getCurrentPlayer();
        } else if (split.length > 2) {
            System.out.println("'status' command takes at most one argument");
            return;
        } else {
            String id = split[1];
            try {
                int playerID = Integer.parseInt(id);

                Optional<Player> player_opt = players.getPlayerByID(playerID);
                if (player_opt.isEmpty()) {
                    System.out.println("No player with that ID");
                    return;
                } else {
                    player = player_opt.get();
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid player id");
            }
        }

        assert player != null;
        printPlayerStatus(player);
    }

    /**This method is used to print the properties a specified gameLogic.Player owns.
     * @param player This is the gameLogic.Player who will have their properties printed
     */
    private void printPlayerProperties(Player player) {
        gameInterface.displayPlayerProperties(player, this.gameBoard);
    }

    /**This method is used to check if the command provided is valid (checks if gameLogic.Player
     * ID is valid), and calls printPlayerProperties() if it is valid. Otherwise, it
     * prints an error message.
     * @param command This is the command a gameLogic.Player provides
     */
    private void handleOwns(String command) {
        String[] split = command.split(" ");

        Player player = null;
        if (split.length < 2) {
            player = players.getCurrentPlayer();
        } else if (split.length > 2) {
            System.out.println("'owns' command takes at most one argument");
            return;
        } else {
            String id = split[1];
            try {
                int playerID = Integer.parseInt(id);

                Optional<Player> player_opt = players.getPlayerByID(playerID);
                if (player_opt.isEmpty()) {
                    System.out.println("No player with that ID");
                    return;
                } else {
                    player = player_opt.get();
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid player id");
            }
        }

        assert player != null;
        printPlayerProperties(player);
    }

    /**This method is used to check if the command is valid for 'pass'. It checks
     * to see if the gameLogic.Player can pass his turn or not.
     * @param command This is the command provided by the gameLogic.Player
     */
    private void handlePass(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'pass' command does not take arguments");
            return;
        }

        if (players.canEndCurrentTurn()) {
            this.players.nextTurn();

            Player player = this.players.getCurrentPlayer();
            System.out.println("It is now gameLogic.Player " + player.getPlayerID() + "'s turn.");

            this.printPlayerStatus(this.players.getCurrentPlayer());
        } else {
            System.out.println("You must roll before you can end the turn.");
        }
    }

    /**This method is used to check if the command is valid for 'roll'. This
     * allows the gameLogic.Player to roll the dice.
     * @param command This is the command provided by the gameLogic.Player
     */
    private void handleRoll(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'roll' command does not take arguments");
            return;
        }

        this.gameActions.currentPlayerRoll();
    }

    /**This method is used to check if the command is valid for 'buy'. This allows
     * the gameLogic.Player to purchase a property
     * @param command This is the command provided by the gameLogic.Player
     */
    private void handleBuy(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'buy' command does not take arguments");
            return;
        }

        this.gameActions.currentPlayerBuy();
    }

    /**This method is used to check if the command is valid for 'auction'. This allows
     * the gameLogic.Player to start an auction for a property.
     * @param command This is the command provided by the gameLogic.Player
     */
    private void handleAuction(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'auction' command does not take arguments");
            return;
        }

        this.gameActions.currentPlayerStartAuction();
    }

    /**This method is used to check the command inputted by the gameLogic.Player, and then
     * calls the method the gameLogic.Player has inputted.
     * @param command This is the command provided by the gameLogic.Player
     */
    public void handleCommand(String command) {
        command = command.toLowerCase();
        if (command.startsWith("help")) {
            printHelp();
            return;
        } else if (command.startsWith("status")) {
            handleStatus(command);
            return;
        } else if (command.startsWith("pass")) {
            handlePass(command);
            return;
        } else if (command.startsWith("roll")) {
            handleRoll(command);
            return;
        } else if (command.startsWith("buy")) {
            handleBuy(command);
            return;
        } else if (command.startsWith("auction")) {
            handleAuction(command);
            return;
        } else if (command.startsWith("owns")) {
            handleOwns(command);
            return;
        }

        System.out.println("Not a valid command");
    }
}
