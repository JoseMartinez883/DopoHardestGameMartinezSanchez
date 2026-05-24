package domain;

import java.io.Serializable;

public class GameTimer implements Serializable {
    private long remainingMs;
    private transient long lastTickTime;

    public GameTimer(int totalSeconds) {
        this.remainingMs = totalSeconds * 1000L;
        this.lastTickTime = System.currentTimeMillis();
    }

    public void tick() {
        long now = System.currentTimeMillis();
        if (lastTickTime <= 0) {
            lastTickTime = now;
            return;
        }
        long delta = now - lastTickTime;
        this.lastTickTime = now;
        
        if (remainingMs > 0) {
            remainingMs -= delta;
        }
    }

    public int getRemainingTime() { 
        return (int) Math.max(0, remainingMs / 1000); 
    }
    
    public boolean isTimeUp() { 
        return remainingMs <= 0; 
    }
}
