package idea.verlif.linkmand.server.key;

import idea.verlif.socket.command.key.KeyCommand;
import idea.verlif.socket.core.client.Client;
import idea.verlif.socket.core.server.holder.ClientHolder;

import java.io.IOException;

/**
 * @author Verlif
 */
public class CloseKey implements KeyCommand {
    @Override
    public String key() {
        return "CLOSE";
    }

    @Override
    public void receiveOnServer(ClientHolder.ClientHandler clientHandler, String s) {
        try {
            clientHandler.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void receiveOnClient(Client client, String s) {
        client.close();
    }
}
