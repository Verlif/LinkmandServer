package idea.verlif.linkmand.server.key;

import idea.verlif.socket.command.key.KeyCommand;
import idea.verlif.socket.core.client.Client;
import idea.verlif.socket.core.server.holder.ClientHolder;

/**
 * @author Verlif
 */
public class ConnectKey implements KeyCommand {

    @Override
    public String key() {
        return "C";
    }

    @Override
    public void receiveOnServer(ClientHolder.ClientHandler clientHandler, String s) {
    }

    @Override
    public void receiveOnClient(Client client, String s) {
    }
}
