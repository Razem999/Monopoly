import java.util.Optional;

public class CommandParser {
    GameBoard gameBoard;
    Players players;
    GameInterfaceI gameInterface;

    CommandParser(GameBoard gameBoard, Players players, GameInterfaceI gameInterface) {
        this.gameBoard = gameBoard;
        this.players = players;
        this.gameInterface = gameInterface;
    }

    private void printHelp() {
        System.out.println(
                """
                        Help----
                        status [id] - Shows player status, prints current player status if id is not supplied
                        roll - Rolls the dice for the current player
                        pass - Passes turn to next player
                        buy - Buy current tile if possible
                        owns [id] - Shows what properties the player owns"""
        );
    }

    private void printPlayerStatus(Player player) {
        Optional<String> tileDescription = gameBoard.getTileDescriptionByIndex(player.getTilePosition());
        if (tileDescription.isPresent()) {
            System.out.println("Player " +
                    player.getPlayerID() +
                    " has a balance of $" +
                    player.getBalance() +
                    " and is on tile:\n" +
                    tileDescription.get());
            if(player.isInJail() == true) {
                System.out.println("Player " +
                        player.getPlayerID() +
                        " is in Jail!\nRoll a double to get out of Jail.");
            }
        } else {
            System.out.println("Player " +
                    player.getPlayerID() +
                    " has a balance of $" +
                    player.getBalance() +
                    " and is on tile UNKNOWN");
        }
    }

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

    private void printPlayerProperties(Player player) {
        System.out.println("Player " + player.getPlayerID() + " owns:");
        for (GameTileI gameTile : this.gameBoard.getTilesOwnedByPlayer(player)) {
            System.out.println(gameTile.getName());
        }
    }

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

    private void handlePass(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'pass' command does not take arguments");
            return;
        }

        if (players.canEndCurrentTurn()) {
            this.players.nextTurn();

            Player player = this.players.getCurrentPlayer();
            System.out.println("It is now Player " + player.getPlayerID() + "'s turn.");

            this.printPlayerStatus(this.players.getCurrentPlayer());
        } else {
            System.out.println("You must roll before you can end the turn.");
        }
    }

    private void handleRoll(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'roll' command does not take arguments");
            return;
        }

        players.currentPlayerRoll(this.gameBoard);
    }

    private void handleBuy(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'buy' command does not take arguments");
            return;
        }

        players.currentPlayerBuy(gameBoard);
    }

    private void handleAuction(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'auction' command does not take arguments");
            return;
        }

        gameInterface.startAuction(0, gameBoard, players);
    }

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
