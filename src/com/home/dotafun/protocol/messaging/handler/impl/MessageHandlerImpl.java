package com.home.dotafun.protocol.messaging.handler.impl;

import com.home.dotafun.bot.exception.BotException;
import com.home.dotafun.global_variables.DF;
import com.home.dotafun.protocol.messaging.handler.MessageHandler;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MessageHandlerImpl implements MessageHandler {
    private ExecutorService executorService;

    public MessageHandlerImpl() {
        this.executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void processMessage(final Request request, final Response response) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    DF.bot.executeCommad(request, response);
                } catch (BotException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}
