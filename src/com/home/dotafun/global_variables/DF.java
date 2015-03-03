package com.home.dotafun.global_variables;

import com.home.dotafun.bot.Bot;
import com.home.dotafun.protocol.messaging.impl.HttpMessagingProtocol;
import com.home.dotafun.protocol.messaging.impl.XmppMessagingProtocol;
import com.home.dotafun.util.DFRobot;
import com.home.dotafun.protocol.file.impl.FtpFileProtocol;

public final class DF {
    public static Bot bot = null;
    public static DFRobot dfRobot = null;
    public static XmppMessagingProtocol xmppMessagingProtocol = null;
    public static HttpMessagingProtocol httpMessagingProtocol = null;
    public static FtpFileProtocol ftpFileProtocol = null;

    private DF() {

    }
}
