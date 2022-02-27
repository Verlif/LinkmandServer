package idea.verlif.linkmand.server;

import idea.verlif.linkmand.server.server.LinkConfig;
import idea.verlif.linkmand.server.server.LinkServer;

import java.io.IOException;

/**
 * @author Verlif
 */
public class Main {

    public static void main(String[] args) throws IOException {
        LinkConfig linkConfig = LinkConfig.loadForFile();
        linkConfig.saveToFile();
        LinkServer server = new LinkServer(linkConfig);
        server.addOnConnectedHandler(clientHandler -> {
            System.out.println("Connected with " + clientHandler.getClient().getInetAddress().getHostAddress());
        });
        server.init();
        System.out.println("Loaded commands: " + server.getParser().keys());
        server.start();
    }
}
