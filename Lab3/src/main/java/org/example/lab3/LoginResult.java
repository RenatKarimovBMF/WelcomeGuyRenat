package org.example.lab3;

public class LoginResult {

    public enum Type {
        SUCCESS,            // login correct
        WRONG_CREDENTIALS,  // email exists but password is wrong
        USER_NOT_FOUND,     // email not in Users.txt
        BLOCKED             // user is blocked for t seconds
    }

    private final Type type;
    private final long remainingBlockMillis;  // only used for BLOCKED

    public LoginResult(Type type, long remainingBlockMillis) {
        this.type = type;
        this.remainingBlockMillis = remainingBlockMillis;
    }

    public Type getType() {
        return type;
    }

    public long getRemainingBlockMillis() {
        return remainingBlockMillis;
    }
}
