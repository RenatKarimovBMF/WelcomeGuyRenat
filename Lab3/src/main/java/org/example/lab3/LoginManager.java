package org.example.lab3;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginManager
{

    private final List<User> validUsers;
    private final int maxAttempts;
    private final long blockDurationMillis;

    // per-email info (attempts, last fail time, blocked state)
    private final Map<String, LoginAttemptInfo> attemptsMap =
            new ConcurrentHashMap<>();

    private final Thread watcherThread;
    private volatile boolean running = true;

    public LoginManager(List<User> validUsers, int maxAttempts, int blockSeconds) {
        this.validUsers = validUsers;
        this.maxAttempts = maxAttempts;
        this.blockDurationMillis = blockSeconds * 1000L;  // ✅ seconds → millis

        // background thread to unblock users when block time passes
        watcherThread = new Thread(this::watchLoop, "LoginWatchdog");
        watcherThread.setDaemon(true); // JVM can exit even if this thread is running
        watcherThread.start();
    }

    private void watchLoop() {
        try {
            while (running) {
                long now = System.currentTimeMillis();
                for (LoginAttemptInfo info : attemptsMap.values()) {
                    if (info.isBlocked()) {
                        long elapsed = now - info.getLastFailTimeMillis();
                        if (elapsed >= blockDurationMillis) {
                            info.reset();
                        }
                    }
                }
                Thread.sleep(1000); // check every 1 second
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        running = false;
        watcherThread.interrupt();
    }

    public LoginResult tryLogin(String email, String password) {
        long now = System.currentTimeMillis();

        // get or create attempt-info for this email
        LoginAttemptInfo info =
                attemptsMap.computeIfAbsent(email, e -> new LoginAttemptInfo());

        // 1. check if user is currently blocked
        if (info.isBlocked()) {
            long elapsed = now - info.getLastFailTimeMillis();
            long remaining = blockDurationMillis - elapsed;
            if (remaining > 0) {
                return new LoginResult(LoginResult.Type.BLOCKED, remaining);
            } else {
                // block expired, reset state
                info.reset();
            }
        }

        // 2. find user by email in validUsers
        User found = null;
        for (User u : validUsers) {
            if (u.getName().equals(email)) {  // note: getName() returns email
                found = u;
                break;
            }
        }

        if (found == null) {
            // email not in system at all
            info.registerFail(now, maxAttempts);
            if (info.isBlocked()) {
                return new LoginResult(LoginResult.Type.BLOCKED, blockDurationMillis);
            }
            return new LoginResult(LoginResult.Type.USER_NOT_FOUND, 0);
        }

        // 3. check password
        if (!found.getPassword().equals(password)) {
            info.registerFail(now, maxAttempts);
            if (info.isBlocked()) {
                return new LoginResult(LoginResult.Type.BLOCKED, blockDurationMillis);
            }
            return new LoginResult(LoginResult.Type.WRONG_CREDENTIALS, 0);
        }

        // 4. success
        info.reset();
        return new LoginResult(LoginResult.Type.SUCCESS, 0);
    }
}

