package gameInterface;

import gameLogic.GameActions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameButtonPanel extends JPanel {

    public GameButtonPanel(GameActions gameActions) {
        super();

        this.setLayout(new GridLayout(2, 2));

        JButton rollButton = new JButton("Roll");
        rollButton.addActionListener(e -> gameActions.currentPlayerRoll());

        JButton passButton = new JButton("Pass");
        passButton.addActionListener(e -> gameActions.currentPlayerPass());

        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> gameActions.currentPlayerBuy());

        JButton auctionButton = new JButton("Auction");
        auctionButton.addActionListener(e -> gameActions.currentPlayerStartAuction());

        this.add(rollButton);
        this.add(passButton);
        this.add(buyButton);
        this.add(auctionButton);

        this.setBorder(new EmptyBorder(70, 50, 70, 50));
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();
        return new Dimension(parent.getWidth() / 2, parent.getHeight());
    }
}
