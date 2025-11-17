package org.example.lab3;

public class LoginAttemptInfo {
    private int failedAttempts;
    private long lastFailTimeMillis;
    private boolean blocked;

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public long getLastFailTimeMillis() {
        return lastFailTimeMillis;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void registerFail(long now, int maxAttempts) {
        failedAttempts++;
        lastFailTimeMillis = now;
        if (failedAttempts >= maxAttempts) {
            blocked = true;
        }
    }

    public void reset() {
        failedAttempts = 0;
        blocked = false;
        lastFailTimeMillis = 0;
    }
}
