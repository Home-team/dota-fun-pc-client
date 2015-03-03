package com.home.dotafun.bot.exception;

public class BotException extends Exception {

    public BotException(String s) {
        super(s);
    }

    public BotException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
