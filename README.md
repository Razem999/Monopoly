# sysc3110-monopoly

Authors: Callum Coughlan, Steven Pham, Razem Muhammad Shahin

Deliverables Milestone 1:
  - UML Diagram: This is a UML class diagram that shows the relationships between alls classes as well as each classes
  methods and variables. This was worked on by all group members at the start of the project to determine all needed methods
  and variables, then was edited when code was done to adjust for any changes made during the coding.
  
  - Sequence Diagrams (All sequence diagrams worked on as a group):
    - Player Rolls and Changes Turn: This diagram show the sequence of events that happen when a player rolls the dice and ends thier turn.
    - Print Player Status: This diagram shows the sequence of events that happen when a player requests that the game sidplay their status in the console.
    - Buy Property: This diagram shows the sequence of events that happen when the player succefully requests to buy an unowned property and when the purchase was successful.
    - paying Rent: This diagram shows the sequence of events that happen when a player lands on a tile that is owned by another player and has to pay rent to that player.
    - Bankruptcy: This diagram shows the sequence of events that happen when a player does not have enough money to pay an expence and the resulting bankruptcy meaning loss of           properties and removal from the game.
    
  - JAR File: This contains our projects code and documentation. Most files where made and edited by all members. See github contributions for 
    every addition from each member.
    
  - Documentation: All documentation can be found in the code above their respective functions and classes.
 
Known Issues:
  - None

Changes From Milestone 1 to Milestone 2:
The biggest change from the previous iteration of this project is a switch from a command line interface to a graphical interface. This was a smooth change as the project was architected from the start with a very low coupling between the interface and the game logic in anticipation of the milestone.

In the previous iteration, any user input was facilitated by the CommandParser class which would trigger game events using the GameActions class through the command line. Most feedback from such events would be facilitated through an implementation of the GameInterfaceI interface, which is a very generic boundary from game logic to interface. No printing (System.out.println) was allowed outside of the CommandParser and TextGameInterface classes (TextGameInterface is an implementor of GameInterfaceI) to make sure that all interface logic was encapsulated within the methods of the GameInterfaceI interface.

This strict adherence to the separation of concerns made it easy to switch to a GUI, as all the game logic would be completely untouched, and the only requirement was to implement the UI itself and connect it to the existing GameInterfaceI interface in the context of swing components. We created the class GameTextBox (which implements GameInterfaceI) as the default mediator of the game logic and the interface module, it provides text-based feedback using a JTextArea. The mode of interaction has been changed from CommandParser to the GameButtons class, which triggers GameAction methods on button clicks as opposed to command line input.

Anything in the game that is better displayed visually than textually is handled by the GameCanvas class, a JPanel that is used as a graphical canvas to draw players and tiles to the screen. The GameCanvas class is slightly more complicated architecturally as Swing’s default rendering strategy is to only redraw components when an interaction occurs within it. This contrasts with most modern games which redraw constantly for example, at 60 FPS.

Deliverables Milestone 2:
  - UML Diagram: This is a UML class diagram has been simplified and now also shows new components of the UI and their relationships to the other classes.
    
  - JAR File: This contains our projects code and Java documentation.
    
  - Documentation: All documentation can be found in the code above their respective functions and classes.

  - Engineering Decisions: Explains why the code was developed the way it was.

  - User Manual: Text file explaining how to play the game.
 
Known Issues:
  - None

Changes From Milestone 2 to Milestone 3:
The biggest changes this milestone were the additions of the AI players and the ability to buy houses and hotels. As the other tiles were added in milestone 1 there were no additional special tiles added to the board. There can be any number of AI players and there are 2 different strategies they use which is randomized upon their creating. The AI either uses the default stratagie which applies scores to tiles that determine if they are worth buying or auctioning and an aggressive stratagy that gets the AI to buy anything they can given they have the money for it. The UML diagram was updated to account for the houses, hotels, and AI.

Deliverables Milestone 3:
  - UML Diagram: Including changes made so houses, hotels, and additional tiles are accounted for.
    
  - JAR File: This contains our projects code and Java documentation.
    
  - Documentation: All documentation can be found in the code above their respective functions and classes.

  - Engineering Decisions: Explains why the code was developed the way it was.

  - User Manual: Text file explaining how to play the game.

  - Sequesnce Diagrams: 5 sequence diagrams depicting actions that are preformed during the game.
 
Known Issues:
  - None

Changes From Milestone 3 to Milestone 4:
  - N/A
