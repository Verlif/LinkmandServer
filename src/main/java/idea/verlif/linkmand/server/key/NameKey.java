package idea.verlif.linkmand.server.key;

import idea.verlif.socket.command.SocketCommand;
import idea.verlif.socket.command.key.KeyCommand;
import idea.verlif.socket.core.client.Client;
import idea.verlif.socket.core.server.holder.ClientHolder;

/**
 * 名称
 *
 * @author Verlif
 */
public class NameKey implements KeyCommand {

    private final String name;

    /**
     * 客户端调用此构造器
     *
     * @param name 客户端名称
     */
    public NameKey(String name) {
        this.name = name;
    }

    @Override
    public String key() {
        return "NAME";
    }

    @Override
    public void receiveOnServer(ClientHolder.ClientHandler clientHandler, String s) {
    }

    @Override
    public void receiveOnClient(Client client, String s) {
        client.sendMessage(this.buildKey() + SocketCommand.SPLIT + name);
    }
}
