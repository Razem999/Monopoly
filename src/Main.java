import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        GameBoard gameBoard = new GameBoard();
        Players players = new Players();

        CommandParser commandParser = new CommandParser(gameBoard, players);

        while (true) {
            System.out.println("Please enter a command, or 'help'.");
            String command = scanner.nextLine();

            commandParser.handleCommand(command);
        }
    }

}
