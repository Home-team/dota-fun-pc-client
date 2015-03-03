package com.home.dotafun.protocol.messaging.impl;

import com.home.dotafun.protocol.messaging.handler.MessageHandler;
import com.home.dotafun.protocol.messaging.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class XmppMessagingProtocol implements MessagingProtocol {
    private String server;
    private String domain;
    private int port;
    private String username;
    private String password;
    private XMPPConnection connection;
    private AccountManager accountManager;
    private Roster roster;

    /**
     * @param server example: webim.qip.ru.
     * @param domain example: qip.ru.
     * @param port   example: 5222
     * @throws XMPPException
     */
    public XmppMessagingProtocol(String server, String domain, int port) throws XMPPException {
        this.server = server;
        this.port = port;
        this.domain = domain;
        ConnectionConfiguration config = new ConnectionConfiguration(this.server, this.port, this.domain);
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
        connection = new XMPPConnection(config);
        accountManager = new AccountManager(connection);
        connection.connect();

        username = null;
        password = null;
        roster = null;
    }

    /**
     * @param username example: user@qip.ru
     * @param password example: secret
     * @throws XMPPException
     */
    public void login(String username, String password) throws XMPPException {
        this.username = username.split("@")[0];
        this.password = password;
        roster = connection.getRoster();
        connection.login(this.username, this.password);
        connection.sendPacket(new Presence(Presence.Type.available));
    }

    public void logout() {
        connection.sendPacket(new Presence(Presence.Type.unavailable));
        this.username = null;
        this.password = null;
    }

    public void disconnect() {
        connection.disconnect();
        this.server = null;
        this.port = -1;
        this.domain = null;
    }

    @Override
    public void sendMessage(String to, String msg) {
        if (to == null || msg == null) {
            throw new NullPointerException();
        }
        Message msgToSend = new Message(to, Message.Type.chat);
        msgToSend.setBody(msg);
        connection.sendPacket(msgToSend);
    }

    @Override
    public void addMessageHandler(final MessageHandler messageHandler) {
        PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class));
        connection.addPacketListener(new Listener(messageHandler), filter);
    }

    public void registration(String username, String password) throws XMPPException {
        accountManager.createAccount(username, password);
    }

    public void addToFriendList(String username, String nickname) throws XMPPException {
        roster.createEntry(username, nickname, null);
    }

    public void removeFromFriendList(String username) throws XMPPException {
        roster.removeEntry(roster.getEntry(username));
    }

    public boolean isFriend(String username) {
        return roster.getEntry(username) != null;
    }

    public String getServer() {
        return server;
    }

    public String getDomain() {
        return domain;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmppMessagingProtocol xmppProtocol = (XmppMessagingProtocol) o;

        if (port != xmppProtocol.port) return false;
        if (!accountManager.equals(xmppProtocol.accountManager)) return false;
        if (!connection.equals(xmppProtocol.connection)) return false;
        if (!domain.equals(xmppProtocol.domain)) return false;
        if (!username.equals(xmppProtocol.username)) return false;
        if (!password.equals(xmppProtocol.password)) return false;
        if (!roster.equals(xmppProtocol.roster)) return false;
        if (!server.equals(xmppProtocol.server)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = server.hashCode();
        result = 31 * result + domain.hashCode();
        result = 31 * result + port;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + connection.hashCode();
        result = 31 * result + accountManager.hashCode();
        result = 31 * result + roster.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Xmpp{");
        sb.append("server='").append(server).append('\'');
        sb.append(", domain='").append(domain).append('\'');
        sb.append(", port=").append(port);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }

    private class Listener implements PacketListener {
        private MessageHandler messageHandler;

        private Listener(MessageHandler messageHandler) {
            this.messageHandler = messageHandler;
        }

        @Override
        public void processPacket(Packet packet) {
            Message msg = (Message) packet;
            if(msg.getBody() != null) {
                Request request = createRequest(msg);
                Response response = createResponse();
                response.setTo(request.getFrom());
                messageHandler.processMessage(request, response);
            }
        }

        private Request createRequest(Message messageXmpp) {
            if(messageXmpp == null) {
                throw new NullPointerException();
            }
            String[] strArray = messageXmpp.getBody().trim().toLowerCase().split(" ", 2);
            String command = null;
            String[] args = null;
            if (strArray.length > 0) {
                command = strArray[0];
            }
            if (strArray.length > 1) {
                args = strArray[1].split(" ");
            }
            return new Request(messageXmpp.getFrom(), messageXmpp.getBody(), command, args);
        }

        private Response createResponse() {
            return new Response(XmppMessagingProtocol.this);
        }
    }
}
