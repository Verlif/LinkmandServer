package idea.verlif.linkmand.server.client;

import idea.verlif.linkmand.server.key.ChatKey;
import idea.verlif.linkmand.server.key.CloseKey;
import idea.verlif.linkmand.server.key.ConnectKey;
import idea.verlif.linkmand.server.key.NameKey;
import idea.verlif.socket.command.SocketCommand;
import idea.verlif.socket.command.client.KeyClient;
import idea.verlif.socket.core.client.ClientConfig;

/**
 * @author Verlif
 */
public class LinkClient extends KeyClient {

    private final String name;
    private final ChatKey chatKey;

    public LinkClient(String name, ClientConfig config) {
        super(config);

        this.name = name;
        this.chatKey = new ChatKey(name);
    }

    @Override
    public boolean connect() {
        addInnerCommand(new NameKey(name));
        addInnerCommand(new ConnectKey());
        addInnerCommand(new CloseKey());
        addInnerCommand(chatKey);
        return super.connect();
    }

    /**
     * 设定聊天信息处理器
     *
     * @param handler 聊天信息处理器
     */
    public void setMessageHandler(ChatKey.MessageHandler handler) {
        chatKey.setMessageHandler(handler);
    }

    /**
     * 向目标客户端发送消息
     *
     * @param to      目标客户端名称
     * @param message 消息内容
     */
    public void chat(String to, String message) {
        chatKey.sendMessage(this, to, message);
    }

    @Override
    public void sendMessage(String message) {
        String[] ss = message.split(SocketCommand.SPLIT, 2);
        super.sendMessage(ss[0] + SocketCommand.SPLIT + name + SocketCommand.SPLIT + (ss.length == 2 ? ss[1] : ""));
    }

    public String getName() {
        return name;
    }
}
