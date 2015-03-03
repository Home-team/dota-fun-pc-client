package com.home.dotafun.bot.exception;

public class NoSuchCommandException extends BotException {

    public NoSuchCommandException(String s) {
        super(s);
    }

    public NoSuchCommandException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
