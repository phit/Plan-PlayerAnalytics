package com.djrapitops.plan.system.tasks.server;

import com.djrapitops.plan.Plan;
import com.djrapitops.plan.data.container.TPS;
import com.djrapitops.plan.system.tasks.TPSCountTimer;
import com.djrapitops.plan.utilities.analysis.MathUtils;
import com.djrapitops.plugin.api.TimeAmount;
import com.djrapitops.plugin.api.utility.log.Log;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class BukkitTPSCountTimer extends TPSCountTimer<Plan> {

    private long lastCheckNano;

    public BukkitTPSCountTimer(Plan plugin) {
        super(plugin);
        lastCheckNano = -1;
    }

    @Override
    public void addNewTPSEntry(long nanoTime, long now) {
        long diff = nanoTime - lastCheckNano;

        lastCheckNano = nanoTime;

        if (diff > nanoTime) { // First run's diff = nanoTime + 1, no calc possible.
            Log.debug("First run of TPSCountTimer Task.");
            return;
        }

        history.add(calculateTPS(diff, now));
    }

    /**
     * Calculates the TPS
     *
     * @param diff The time difference between the last run and the new run
     * @param now  The time right now
     * @return the TPS
     */
    private TPS calculateTPS(long diff, long now) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
        double averageCPUUsage = MathUtils.round(operatingSystemMXBean.getSystemLoadAverage() / availableProcessors * 100.0);

        if (averageCPUUsage < 0) { // If unavailable, getSystemLoadAverage() returns -1
            averageCPUUsage = -1;
        }

        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long usedMemory = (totalMemory - runtime.freeMemory()) / 1000000;

        int playersOnline = plugin.getServer().getOnlinePlayers().size();
        latestPlayersOnline = playersOnline;
        int loadedChunks = getLoadedChunks();
        int entityCount;

        entityCount = getEntityCount();

        return getTPS(diff, now, averageCPUUsage, usedMemory, entityCount, loadedChunks, playersOnline);
    }

    /**
     * Gets the TPS for Spigot / Bukkit
     *
     * @param diff          The difference between the last run and this run
     * @param now           The time right now
     * @param cpuUsage      The usage of the CPU
     * @param playersOnline The amount of players that are online
     * @return the TPS
     */
    protected TPS getTPS(long diff, long now, double cpuUsage, long usedMemory, int entityCount, int chunksLoaded, int playersOnline) {
        long difference = diff;
        if (difference < TimeAmount.SECOND.ns()) { // No tick count above 20
            difference = TimeAmount.SECOND.ns();
        }

        long twentySeconds = 20L * TimeAmount.SECOND.ns();
        while (difference > twentySeconds) {
            // Add 0 TPS since more than 20 ticks has passed.
            history.add(new TPS(now, 0, playersOnline, cpuUsage, usedMemory, entityCount, chunksLoaded));
            difference -= twentySeconds;
        }

        double tpsN = twentySeconds * 1.0 / difference;

        return new TPS(now, tpsN, playersOnline, cpuUsage, usedMemory, entityCount, chunksLoaded);
    }

    /**
     * Gets the amount of loaded chunks
     *
     * @return amount of loaded chunks
     */
    private int getLoadedChunks() {
        return plugin.getServer().getWorlds().stream().mapToInt(world -> world.getLoadedChunks().length).sum();
    }

    /**
     * Gets the amount of entities on the server for Bukkit / Spigot
     *
     * @return amount of entities
     */
    protected int getEntityCount() {
        return plugin.getServer().getWorlds().stream().mapToInt(world -> world.getEntities().size()).sum();
    }
}
