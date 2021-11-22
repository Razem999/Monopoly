package gameInterface;

import gameLogic.GameBoard;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.List;

public class TileSelectionMenu extends JDialog {
    private final TileSelectionListener selectionListener;
    private final TileSelectionCancelListener cancelListener;

    public interface TileSelectionListener {
        void handleTileSelection(int tilePosition);
    }

    public interface TileSelectionCancelListener {
        void handleSelectionCancelation();
    }

    private static class TileCellListRenderer extends DefaultListCellRenderer {
        private final GameBoard gameBoard;

        public TileCellListRenderer(GameBoard gameBoard) {
            this.gameBoard = gameBoard;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof GameBoard.TileAndIndex tileAndIndex) {
                setText(tileAndIndex.tile().getName());
            }

            return this;
        }
    }

    public TileSelectionMenu(List<GameBoard.TileAndIndex> tilePositions, GameBoard gameBoard, TileSelectionListener selectionListener, TileSelectionCancelListener cancelListener) {
        super();

        this.selectionListener = selectionListener;
        this.cancelListener = cancelListener;
        DefaultListModel<GameBoard.TileAndIndex> model = new DefaultListModel<>();
        model.addAll(tilePositions);

        JList<GameBoard.TileAndIndex> list = new JList<>();
        list.setCellRenderer(new TileCellListRenderer(gameBoard));

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridheight = 2;
        layout.addLayoutComponent(list, c);

        JButton confirmButton = new JButton("Ok");
        confirmButton.addActionListener(e -> {
            if (list.getSelectedIndex() != -1) {
                this.selectionListener.handleTileSelection(list.getSelectedValue().index());
                this.setVisible(false);
                this.dispose();
            }
        });
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        layout.addLayoutComponent(confirmButton, c);

        JButton cancelButton = new JButton("Cancel");
        confirmButton.addActionListener(e -> {
            this.cancelListener.handleSelectionCancelation();
            this.setVisible(false);
            this.dispose();
        });
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        layout.addLayoutComponent(cancelButton, c);
    }
}
