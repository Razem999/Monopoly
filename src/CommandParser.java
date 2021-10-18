import java.util.Optional;

public class CommandParser {
    GameBoard gameBoard;
    Players players;

    CommandParser(GameBoard gameBoard, Players players) {
        this.gameBoard = gameBoard;
        this.players = players;
    }

    private void printHelp() {
        System.out.println(
                "Help----\n" +
                "status [id] - Shows player status, prints current player status if id is not supplied\n" +
                "pass - Passes turn to next player"
        );
    }

    private void printPlayerStatus(Player player) {
        Optional<String> tileDescription = gameBoard.getTileDescriptionByIndex(player.getTilePosition());
        if (tileDescription.isPresent()) {
            System.out.println("Player " +
                    player.getPlayerID() +
                    " has a balance of $" +
                    player.getBalance() +
                    " and is on tile '" +
                    tileDescription.get() + "'");
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

    private void handlePass(String command) {
        if (command.split(" ").length > 1) {
            System.out.println("'pass' command does not take arguments");
            return;
        }

        this.players.nextTurn();

        Player player = this.players.getCurrentPlayer();
        System.out.println("It is now Player " + player.getPlayerID() + "'s turn.");
    }

    public void handleCommand(String command) {
        if (command.equals("help")) {
            printHelp();
            return;
        } else if (command.startsWith("status")) {
            handleStatus(command);
            return;
        } else if (command.startsWith("pass")) {
            handlePass(command);
            return;
        }

        System.out.println("Not a valid command");
    }
}
