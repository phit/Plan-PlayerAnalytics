package com.djrapitops.plan.database;

import com.djrapitops.plan.database.DemographicsData;
import com.djrapitops.plan.database.Database;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class UserData {

    private boolean isAccessed;
    
    private UUID uuid;
    private Location location;
    private List<Location> locations;
    private Location bedLocation;
    private List<InetAddress> ips;
    private List<String> nicknames;
    private long registered;
    private long lastPlayed;
    private long playTime;
    private int loginTimes;
    private int timesKicked;
    private long lastGmSwapTime;
    private GameMode lastGamemode;
    private HashMap<GameMode, Long> gmTimes;
    private boolean isOp;
    private boolean isBanned;
    private DemographicsData demData;

    public UserData(Player player, DemographicsData demData, Database db) {
        uuid = player.getUniqueId();
        bedLocation = player.getBedSpawnLocation();
        registered = player.getFirstPlayed();
        location = player.getLocation();
        isOp = player.isOp();
        locations = new ArrayList<>();
        nicknames = new ArrayList<>();
        ips = new ArrayList<>();
        gmTimes = new HashMap<>();
        this.demData = demData;
        isBanned = player.isBanned();
    }

    public UserData(OfflinePlayer player, DemographicsData demData, Database db) {
        uuid = player.getUniqueId();
        bedLocation = player.getBedSpawnLocation();
        registered = player.getFirstPlayed();
        isOp = player.isOp();
        locations = new ArrayList<>();
        nicknames = new ArrayList<>();
        ips = new ArrayList<>();
        gmTimes = new HashMap<>();
        this.demData = demData;
        isBanned = player.isBanned();
    }    
    public UserData(OfflinePlayer player, DemographicsData demData) {
        uuid = player.getUniqueId();
        bedLocation = player.getBedSpawnLocation();
        registered = player.getFirstPlayed();
        isOp = player.isOp();
        locations = new ArrayList<>();
        nicknames = new ArrayList<>();
        ips = new ArrayList<>();
        gmTimes = new HashMap<>();
        this.demData = demData;
        isBanned = player.isBanned();
    }    

    public void addIpAddress(InetAddress ip) {
        if (!ips.contains(ip)) {
            ips.add(ip);
        }
    }

    public void addIpAddresses(Collection<InetAddress> addIps) {
        ips.addAll(addIps);
    }

    public void addLocation(Location loc) {
        locations.add(loc);
        location = locations.get(locations.size()-1);
    }

    public void addLocations(Collection<Location> addLocs) {
        locations.addAll(addLocs);
        location = locations.get(locations.size()-1);
    }

    public void addNickname(String nick) {
        if (!nicknames.contains(nick)) {
            nicknames.add(nick);
        }
    }

    public void addNicknames(Collection<String> addNicks) {
        nicknames.addAll(addNicks);
    }

    public void setGMTime(GameMode gm, long time) {
        gmTimes.put(gm, time);
    }

    public void setAllGMTimes(long survivalTime, long creativeTime, long adventureTime, long spectatorTime) {
        gmTimes.clear();
        gmTimes.put(GameMode.SURVIVAL, survivalTime);
        gmTimes.put(GameMode.CREATIVE, creativeTime);
        gmTimes.put(GameMode.ADVENTURE, adventureTime);
        gmTimes.put(GameMode.SPECTATOR, spectatorTime);
    }

    public void updateBanned(Player p) {
        isBanned = p.isBanned();
    }
    
    public boolean isAccessed() {
        return isAccessed;
    }
    
    public void setAccessing(boolean value) {
        isAccessed = value;
    } 

    // Getters -------------------------------------------------------------
    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location getBedLocation() {
        return bedLocation;
    }

    public List<InetAddress> getIps() {
        return ips;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public long getRegistered() {
        return registered;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public long getPlayTime() {
        return playTime;
    }

    public int getLoginTimes() {
        return loginTimes;
    }

    public int getTimesKicked() {
        return timesKicked;
    }

    public HashMap<GameMode, Long> getGmTimes() {
        return gmTimes;
    }

    public long getLastGmSwapTime() {
        return lastGmSwapTime;
    }

    public GameMode getLastGamemode() {
        return lastGamemode;
    }

    public boolean isOp() {
        return isOp;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public DemographicsData getDemData() {
        return demData;
    }

    // Setters -------------------------------------------------------------
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
    }

    public void setIps(List<InetAddress> ips) {
        this.ips = ips;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public void setRegistered(long registered) {
        this.registered = registered;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public void setLoginTimes(int loginTimes) {
        this.loginTimes = loginTimes;
    }

    public void setTimesKicked(int timesKicked) {
        this.timesKicked = timesKicked;
    }

    public void setGmTimes(HashMap<GameMode, Long> gmTimes) {
        this.gmTimes = gmTimes;
    }

    public void setLastGmSwapTime(long lastGmSwapTime) {
        this.lastGmSwapTime = lastGmSwapTime;
    }

    public void setLastGamemode(GameMode lastGamemode) {
        this.lastGamemode = lastGamemode;
    }

    public void setIsOp(boolean isOp) {
        this.isOp = isOp;
    }

    public void setDemData(DemographicsData demData) {
        this.demData = demData;
    }
}