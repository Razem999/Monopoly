package gameInterface;

import gameLogic.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;

public class TileSelectionMenu extends JDialog {
    private boolean closing;

    public interface TileSelectionCancelListener {
        void handleSelectionCancelation();
    }

    private static class TileCellListRenderer extends DefaultListCellRenderer {

        public TileCellListRenderer() {
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

    public TileSelectionMenu(List<GameBoard.TileAndIndex> tilePositions, Consumer<Integer> selectionListener, TileSelectionCancelListener cancelListener) {
        super();

        this.closing = false;
        DefaultListModel<GameBoard.TileAndIndex> model = new DefaultListModel<>();
        model.addAll(tilePositions);

        JList<GameBoard.TileAndIndex> list = new JList<>(model);
        list.setCellRenderer(new TileCellListRenderer());

        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridheight = 2;
        this.add(list, c);

        JButton confirmButton = new JButton("Ok");
        confirmButton.addActionListener(e -> {
            if (list.getSelectedIndex() != -1 && !this.closing) {
                this.closing = true;
                selectionListener.accept(list.getSelectedValue().index());
                this.setVisible(false);
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        this.add(confirmButton, c);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            if (!this.closing) {
                this.closing = true;
                cancelListener.handleSelectionCancelation();
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        this.add(cancelButton, c);

        this.setSize(new Dimension(500, 700));
        this.setVisible(true);
    }
}
