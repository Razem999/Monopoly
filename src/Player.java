import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerID;
    private int balance;
    private int tilePosition;

    private List<PlayerBalanceChangeListener> playerBalanceChangeListeners;

    Player(int id, int startingBalance) {
        this.playerID = id;
        this.balance = startingBalance;
        this.tilePosition = 0;

        this.playerBalanceChangeListeners = new ArrayList<>();
    }

    Player(int id) {
        this(id, 0);
    }

    public int getTilePosition() {
        return this.tilePosition;
    }

    public void setTilePosition(int tilePosition) {
        this.tilePosition = tilePosition;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getPlayerID() {
        return this.playerID;
    }

    public boolean hasID(int id) {
        return this.playerID == id;
    }

    @Override
    public boolean equals(Object p) {
        if (p instanceof Player) {
            return ((Player) p).hasID(this.playerID);
        }

        return false;
    }

    public void registerPlayerBalanceChangeListener(PlayerBalanceChangeListener listener) {
        this.playerBalanceChangeListeners.add(listener);
    }

    public void changeBalance(int diff) {
        int oldBalance = this.balance;
        this.balance += diff;

        for (PlayerBalanceChangeListener listener : playerBalanceChangeListeners) {
            listener.onBalanceChange(this, oldBalance, this.balance);
        }
    }
}
