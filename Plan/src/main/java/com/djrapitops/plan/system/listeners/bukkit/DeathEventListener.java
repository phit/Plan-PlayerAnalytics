package com.djrapitops.plan.system.listeners.bukkit;

import com.djrapitops.plan.data.container.Session;
import com.djrapitops.plan.system.cache.SessionCache;
import com.djrapitops.plan.system.processing.Processing;
import com.djrapitops.plan.system.processing.processors.player.KillProcessor;
import com.djrapitops.plan.utilities.MiscUtils;
import com.djrapitops.plugin.api.utility.log.Log;
import com.djrapitops.plugin.utilities.Format;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Event Listener for EntityDeathEvents.
 *
 * @author Rsl1122
 */
public class DeathEventListener implements Listener {

    /**
     * Command use listener.
     *
     * @param event Fired event.
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent event) {
        long time = MiscUtils.getTime();
        LivingEntity dead = event.getEntity();

        if (dead instanceof Player) {
            // Process Death
            Processing.submitCritical(() -> SessionCache.getCachedSession(dead.getUniqueId()).ifPresent(Session::died));
        }

        try {
            EntityDamageEvent entityDamageEvent = dead.getLastDamageCause();
            if (!(entityDamageEvent instanceof EntityDamageByEntityEvent)) {
                return;
            }

            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entityDamageEvent;
            Entity killerEntity = entityDamageByEntityEvent.getDamager();

            handleKill(time, dead, killerEntity);
        } catch (Exception e) {
            Log.toLog(this.getClass(), e);
        }
    }

    private void handleKill(long time, LivingEntity dead, Entity killerEntity) {
        KillProcessor processor = null;
        if (killerEntity instanceof Player) {
            processor = handlePlayerKill(time, dead, (Player) killerEntity);
        } else if (killerEntity instanceof Wolf) {
            processor = handleWolfKill(time, dead, (Wolf) killerEntity);
        } else if (killerEntity instanceof Arrow) {
            processor = handleArrowKill(time, dead, (Arrow) killerEntity);
        }
        if (processor != null) {
            Processing.submit(processor);
        }
    }

    private KillProcessor handlePlayerKill(long time, LivingEntity dead, Player killer) {
        Material itemInHand;
        try {
            itemInHand = killer.getInventory().getItemInMainHand().getType();
        } catch (NoSuchMethodError e) {
            try {
                itemInHand = killer.getInventory().getItemInHand().getType(); // Support for non dual wielding versions.
            } catch (Exception | NoSuchMethodError | NoSuchFieldError e2) {
                itemInHand = Material.AIR;
            }
        }

        return new KillProcessor(killer.getUniqueId(), time, dead, normalizeMaterialName(itemInHand));
    }

    private KillProcessor handleWolfKill(long time, LivingEntity dead, Wolf wolf) {
        if (!wolf.isTamed()) {
            return null;
        }

        AnimalTamer owner = wolf.getOwner();
        if (!(owner instanceof Player)) {
            return null;
        }

        return new KillProcessor(owner.getUniqueId(), time, dead, "Wolf");
    }

    private KillProcessor handleArrowKill(long time, LivingEntity dead, Arrow arrow) {
        ProjectileSource source = arrow.getShooter();
        if (!(source instanceof Player)) {
            return null;
        }

        Player player = (Player) source;

        return new KillProcessor(player.getUniqueId(), time, dead, "Bow");
    }

    /**
     * Normalizes a material name
     *
     * @param material The material
     * @return The normalized material name
     */
    private String normalizeMaterialName(Material material) {
        String[] parts = material.name().split("_");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = new Format(parts[i]).capitalize().toString();
            builder.append(part);
            if (i < parts.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }
}

