package main.java.com.djrapitops.plan.data.handling;

import java.sql.SQLException;
import java.util.UUID;
import main.java.com.djrapitops.plan.Log;
import main.java.com.djrapitops.plan.Plan;
import main.java.com.djrapitops.plan.data.KillData;
import main.java.com.djrapitops.plan.data.UserData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Class containing static methods for processing information contained in a
 * DeathEvent when the killer is a player.
 *
 * @author Rsl1122
 * @since 3.0.0
 */
public class KillHandling {

    /**
     * Processes the information of the Event and changes UserData object
     * accordingly.
     *
     * @param data UserData of the player.
     * @param time Epoch ms the event occurred.
     * @param dead Mob or a Player the player killed.
     * @param weaponName The name of the Weapon used.
     */
    public static void processKillInfo(UserData data, long time, LivingEntity dead, String weaponName) {
        Plan plugin = Plan.getInstance();
        if (dead instanceof Player) {
            Player deadPlayer = (Player) dead;
            int victimID;
            try {
                UUID victimUUID = deadPlayer.getUniqueId();
                victimID = plugin.getDB().getUserId(victimUUID + "");
                if (victimID == -1) {
                    return;
                }
                data.addPlayerKill(new KillData(victimUUID, victimID, weaponName, time));
            } catch (SQLException e) {
                Log.toLog("main.java.com.djrapitops.plan.KillHandling", e);
            }
        } else {
            data.setMobKills(data.getMobKills() + 1);
        }
    }
}