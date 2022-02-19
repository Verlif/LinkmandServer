package idea.verlif.linkmand.server.client;

import idea.verlif.linkmand.server.key.CloseKey;
import idea.verlif.linkmand.server.key.ConnectKey;
import idea.verlif.linkmand.server.key.NameKey;
import idea.verlif.socket.command.client.KeyClient;
import idea.verlif.socket.core.client.ClientConfig;

/**
 * @author Verlif
 */
public class LinkClient extends KeyClient {

    private final String name;

    public LinkClient(String name, ClientConfig config) {
        super(config);

        this.name = name;
    }

    @Override
    public boolean connect() {
        addInnerCommand(new NameKey(name));
        addInnerCommand(new ConnectKey());
        addInnerCommand(new CloseKey());
        return super.connect();
    }

    @Override
    public void sendMessage(String message) {
        String[] ss = message.split(" ", 2);
        super.sendMessage(ss[0] + " " + name + " " + (ss.length == 2 ? ss[1] : ""));
    }
}
