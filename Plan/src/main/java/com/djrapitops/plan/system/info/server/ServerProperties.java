package com.djrapitops.plan.system.info.server;

import com.djrapitops.plan.system.settings.Settings;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Server;

/**
 * Class responsible for holding server variable values that do not change
 * without a reload.
 *
 * @author Rsl1122
 * @since 3.4.1
 */
public class ServerProperties {

    private final String id;
    private final String name;
    private final int port;
    private final String version;
    private final String implVersion;
    private final String ip;
    private final int maxPlayers;

    public ServerProperties(Server server) {
        id = server.getServerId();
        ip = server.getIp();
        name = server.getName();
        port = server.getPort();
        version = server.getVersion();
        implVersion = server.getBukkitVersion();

        maxPlayers = server.getMaxPlayers();
    }

    /**
     * Constructor, grabs the variables.
     *
     * @param server instance the plugin is running on.
     */
    public ServerProperties(ProxyServer server) {
        id = server.getServers().toString();
        ip = Settings.BUNGEE_IP.toString();
        name = "BungeeCord";
        port = -1;
        version = server.getVersion();
        implVersion = server.getVersion();

        maxPlayers = server.getConfig().getPlayerLimit();
    }

    /**
     * Ip string in server.properties.
     *
     * @return the ip.
     */
    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public String getVersion() {
        return version;
    }

    public String getImplVersion() {
        return implVersion;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getServerId() {
        return id;
    }
}