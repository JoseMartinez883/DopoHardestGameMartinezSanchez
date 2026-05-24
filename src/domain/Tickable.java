package domain;

import java.io.Serializable;

public interface Tickable extends Serializable {
    void tick(Board board);
}
