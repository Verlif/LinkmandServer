package idea.verlif.linkmand.server.server;

import idea.verlif.linkmand.server.key.ChatKey;
import idea.verlif.linkmand.server.key.CloseKey;
import idea.verlif.linkmand.server.key.ConnectKey;
import idea.verlif.linkmand.server.key.NameKey;
import idea.verlif.socket.command.SocketCommand;
import idea.verlif.socket.command.server.KeyServer;
import idea.verlif.socket.command.server.handle.InputMessageHandler;
import idea.verlif.socket.core.server.holder.ClientHolder;

import java.io.IOException;
import java.util.*;

/**
 * @author Verlif
 */
public class LinkServer extends KeyServer {

    private final LinkConfig config;
    private final Map<String, ClientHolder.ClientHandler> handlerMap;

    public LinkServer(LinkConfig config) {
        super(config);

        this.config = config;
        this.handlerMap = new HashMap<>();

        parser.setMessageHandler(new InputMessageHandler() {
            @Override
            public String preHandle(ClientHolder.ClientHandler clientHandler, String s) {
                String[] ss = s.split(SocketCommand.SPLIT, 3);
                if (ss.length > 1 && config.allowedName(ss[1])) {
                    return ss[0] + SocketCommand.SPLIT + (ss.length == 3 ? ss[2] : "");
                }
                clientHandler.sendMessage("NO PERMISSION!");
                try {
                    clientHandler.close();
                } catch (IOException ignored) {
                }
                return null;
            }
        });
    }

    @Override
    public void init() throws IOException {
        NameKey nameKey = new NameKey("SERVER") {
            @Override
            public void receiveOnServer(ClientHolder.ClientHandler clientHandler, String s) {
                if (!config.allowedSocket(clientHandler.getClient())) {
                    clientHandler.sendMessage("ERROR NAME!!!");
                    try {
                        clientHandler.close();
                    } catch (IOException ignored) {
                    }
                    return;
                }
                if (s.length() == 0) {
                    clientHandler.sendMessage("ERROR NAME!!!");
                    try {
                        clientHandler.close();
                    } catch (IOException ignored) {
                    }
                } else {
                    String name = s.split(SocketCommand.SPLIT, 2)[0];
                    ClientHolder.ClientHandler handler = handlerMap.get(name);
                    if (handler != null) {
                        try {
                            handler.sendMessage("NEW CLIENT CONNECTED! THIS CONNECTION WILL BE CLOSED!");
                            handler.close();
                        } catch (IOException ignored) {
                        }
                        handlerMap.put(name, clientHandler);
                    } else if (config.allowedName(name)) {
                        handlerMap.put(name, clientHandler);
                    } else {
                        clientHandler.sendMessage("ERROR NAME!!!");
                        try {
                            clientHandler.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        };
        addKeyCommand(nameKey);
        addKeyCommand(new ConnectKey());
        addKeyCommand(new CloseKey());
        addKeyCommand(new ChatKey(this));

        super.init();
        addOnConnectedHandler(clientHandler -> clientHandler.sendMessage(nameKey.buildKey()));
    }

    /**
     * 获取客户端处理器
     *
     * @param name 客户端名称
     * @return 客户端名称对应的处理器，可能为null
     */
    public ClientHolder.ClientHandler getHandler(String name) {
        syc();
        return handlerMap.get(name);
    }

    /**
     * 获取所有的已连接的客户端连接
     *
     * @return 客户端名称列表
     */
    public Set<String> getAllClientName() {
        syc();
        return handlerMap.keySet();
    }

    /**
     * 获取所有已连接的客户端处理器
     *
     * @return 所有的已连接的客户端处理器
     */
    public Collection<ClientHolder.ClientHandler> getAllHandler() {
        syc();
        return handlerMap.values();
    }

    private void syc() {
        synchronized (handlerMap) {
            Set<String> set = new HashSet<>(handlerMap.keySet());
            for (String key : set) {
                ClientHolder.ClientHandler handler = handlerMap.get(key);
                if (handler == null || handler.getClient().isClosed()) {
                    handlerMap.remove(key);
                }
            }
        }
    }

    @Override
    public void stop() throws IOException {
        super.stop();
        config.saveToFile();
    }
}
