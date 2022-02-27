package idea.verlif.linkmand.server.key;

import idea.verlif.linkmand.server.client.LinkClient;
import idea.verlif.linkmand.server.server.LinkServer;
import idea.verlif.socket.command.SocketCommand;
import idea.verlif.socket.command.key.KeyCommand;
import idea.verlif.socket.core.client.Client;
import idea.verlif.socket.core.server.holder.ClientHolder;

/**
 * @author Verlif
 */
public class ChatKey implements KeyCommand {

    private final String name;
    private final LinkServer linkServer;
    private MessageHandler messageHandler;

    public ChatKey(String name) {
        this.name = name;
        this.linkServer = null;
    }

    public ChatKey(LinkServer linkServer) {
        this.name = null;
        this.linkServer = linkServer;
    }

    @Override
    public String key() {
        return "CHAT";
    }

    @Override
    public void receiveOnServer(ClientHolder.ClientHandler clientHandler, String s) {
        String[] ss = s.split(SocketCommand.SPLIT, 3);
        if (ss.length == 3) {
            if (linkServer != null && linkServer.getHandler(ss[0]) != null) {
                ClientHolder.ClientHandler handler = linkServer.getHandler(ss[1]);
                if (handler == null) {
                    clientHandler.sendMessage("CLIENT " + ss[1] + " IS NOT ONLINE!");
                } else {
                    handler.sendMessage(buildKey() + SocketCommand.SPLIT + ss[0] + SocketCommand.SPLIT + ss[2]);
                }
            } else {
                clientHandler.sendMessage("UNREACHED!");
            }
        } else {
            clientHandler.sendMessage("LACK OF PARAMS!");
        }
    }

    @Override
    public void receiveOnClient(Client client, String s) {
        String[] ss = s.split(SocketCommand.SPLIT, 2);
        if (messageHandler == null) {
            client.sendMessage(buildKey() + SocketCommand.SPLIT + name + SocketCommand.SPLIT + ss[0] + SocketCommand.SPLIT + name + " HAS NO MessageHandler!");
        } else {
            if (ss.length == 2) {
                messageHandler.receiveMessage(ss[0], ss[1]);
            } else {
                messageHandler.receiveMessage(null, ss[0]);
            }
        }
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void sendMessage(LinkClient client, String to, String message) {
        client.sendMessage(buildKey() + SocketCommand.SPLIT + name + SocketCommand.SPLIT + to + SocketCommand.SPLIT + message);
    }

    public interface MessageHandler {

        /**
         * 客户端接收其他客户端的信息。
         *
         * @param from    信息来源客户端名称
         * @param message 信息内容
         */
        void receiveMessage(String from, String message);
    }
}
