package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;
import com.home.dotafun.util.DFProcess;

import java.io.IOException;

public class ShutdownCommand implements Command {

    @Override
    public void execute(Request request, Response response) throws IOException {
        DFProcess dfProcess = new DFProcess("shutdown /s /t 0");
        dfProcess.readStdout(response);
        dfProcess.readStderr(response);
    }
}
