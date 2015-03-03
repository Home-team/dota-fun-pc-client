package com.home.dotafun.bot.command.impl;

import com.home.dotafun.bot.command.Command;
import com.home.dotafun.protocol.messaging.Request;
import com.home.dotafun.protocol.messaging.Response;
import com.home.dotafun.util.DFProcess;

import java.io.IOException;

public class KillDotaCommand implements Command {

    @Override
    public void execute(Request request, Response response) throws IOException {
        DFProcess dfProcess = new DFProcess("taskkill /f /im dota.exe");
        dfProcess.readStdout(response);
        dfProcess.readStderr(response);
    }
}
