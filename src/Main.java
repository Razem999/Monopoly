import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        TextGameInterface textGameInterface = new TextGameInterface();
        GameBoard gameBoard = new GameBoard(textGameInterface);
        Players players = new Players(textGameInterface);

        CommandParser commandParser = new CommandParser(gameBoard, players);

        while (true) {
            System.out.print("\nPlease enter a command, or 'help'.\n>> ");
            String command = scanner.nextLine();

            commandParser.handleCommand(command);
        }
    }

}
