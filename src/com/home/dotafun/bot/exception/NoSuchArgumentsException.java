package com.home.dotafun.bot.exception;

public class NoSuchArgumentsException extends BotException {

    public NoSuchArgumentsException(String s) {
        super(s);
    }

    public NoSuchArgumentsException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
