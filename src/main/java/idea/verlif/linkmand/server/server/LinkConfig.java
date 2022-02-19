package idea.verlif.linkmand.server.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import idea.verlif.socket.command.server.CommandConfig;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Verlif
 */
@JsonIgnoreProperties(value = {"handler", "connectedListener", "closedListener"})
public class LinkConfig extends CommandConfig implements Serializable {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    private static final String CONFIG_PATH = "config.json";
    private final Set<String> allowedNames;
    private final Set<String> blockedNames;
    private final Set<String> allowedIps;
    private final Set<String> blockedIps;

    public LinkConfig() {
        allowedNames = new HashSet<>();
        blockedNames = new HashSet<>();
        allowedIps = new HashSet<>();
        blockedIps = new HashSet<>();
    }

    public static LinkConfig loadForFile() throws IOException {
        File file = new File(CONFIG_PATH);
        if (file.exists()) {
            try {
                return OBJECT_MAPPER.readValue(file, LinkConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LinkConfig config = new LinkConfig();
        config.saveToFile();
        return config;
    }

    public void saveToFile() throws IOException {
        File file = new File(CONFIG_PATH);
        OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, this);
    }

    public Set<String> getAllowedNames() {
        return allowedNames;
    }

    public Set<String> getBlockedNames() {
        return blockedNames;
    }

    public Set<String> getAllowedIps() {
        return allowedIps;
    }

    public Set<String> getBlockedIps() {
        return blockedIps;
    }

    /**
     * 是否允许客户端
     *
     * @param name 客户端名
     * @return 客户端是否可连接
     */
    public boolean allowedName(String name) {
        if (name == null) {
            return false;
        }
        return allowedNames.size() == 0 && !blockedNames.contains(name) || allowedNames.contains(name);
    }

    /**
     * 是否允许套接字
     *
     * @param socket 客户端套接字
     * @return 套接字是否可连接
     */
    public boolean allowedSocket(Socket socket) {
        String ip = socket.getInetAddress().getHostAddress();
        return allowedIps.size() == 0 && !blockedIps.contains(ip) || allowedIps.contains(ip);
    }
}
