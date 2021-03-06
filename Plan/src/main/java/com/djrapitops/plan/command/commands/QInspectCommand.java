package com.djrapitops.plan.command.commands;

import com.djrapitops.plan.PlanPlugin;
import com.djrapitops.plan.api.exceptions.database.DBException;
import com.djrapitops.plan.data.PlayerProfile;
import com.djrapitops.plan.data.calculation.ActivityIndex;
import com.djrapitops.plan.system.database.databases.Database;
import com.djrapitops.plan.system.settings.Permissions;
import com.djrapitops.plan.system.settings.locale.Locale;
import com.djrapitops.plan.system.settings.locale.Msg;
import com.djrapitops.plan.utilities.Condition;
import com.djrapitops.plan.utilities.FormatUtils;
import com.djrapitops.plan.utilities.MiscUtils;
import com.djrapitops.plan.utilities.uuid.UUIDUtility;
import com.djrapitops.plugin.api.utility.log.Log;
import com.djrapitops.plugin.command.CommandNode;
import com.djrapitops.plugin.command.CommandType;
import com.djrapitops.plugin.command.ISender;
import com.djrapitops.plugin.settings.ColorScheme;
import com.djrapitops.plugin.settings.DefaultMessages;
import com.djrapitops.plugin.task.AbsRunnable;
import com.djrapitops.plugin.task.RunnableFactory;
import com.djrapitops.plugin.utilities.Verify;

import java.util.UUID;

/**
 * This command is used to cache UserInfo to InspectCache and display the link.
 *
 * @author Rsl1122
 * @since 1.0.0
 */
public class QInspectCommand extends CommandNode {

    private final PlanPlugin plugin;

    /**
     * Class Constructor.
     *
     * @param plugin Current instance of Plan
     */
    public QInspectCommand(PlanPlugin plugin) {
        super("qinspect", Permissions.QUICK_INSPECT.getPermission(), CommandType.PLAYER_OR_ARGS);
        setArguments("<player>");
        setShortHelp(Locale.get(Msg.CMD_USG_QINSPECT).toString());
        setInDepthHelp(Locale.get(Msg.CMD_HELP_QINSPECT).toArray());
        this.plugin = plugin;

    }

    @Override
    public void onCommand(ISender sender, String commandLabel, String[] args) {
        String playerName = MiscUtils.getPlayerName(args, sender, Permissions.QUICK_INSPECT_OTHER);

        runInspectTask(playerName, sender);
    }

    private void runInspectTask(String playerName, ISender sender) {
        RunnableFactory.createNew(new AbsRunnable("InspectTask") {
            @Override
            public void run() {
                try {
                    UUID uuid = UUIDUtility.getUUIDOf(playerName);
                    if (!Condition.isTrue(Verify.notNull(uuid), Locale.get(Msg.CMD_FAIL_USERNAME_NOT_VALID).toString(), sender)) {
                        return;
                    }
                    Database database = Database.getActive();
                    if (!Condition.isTrue(database.check().isPlayerRegistered(uuid), Locale.get(Msg.CMD_FAIL_USERNAME_NOT_KNOWN).toString(), sender)) {
                        return;
                    }

                    PlayerProfile playerProfile = database.fetch().getPlayerProfile(uuid);

                    sendMsgs(sender, playerProfile);

                } catch (DBException ex) {
                    Log.toLog(this.getClass(), ex);
                } finally {
                    this.cancel();
                }
            }
        }).runTaskAsynchronously();
    }

    private void sendMsgs(ISender sender, PlayerProfile profile) {
        long now = MiscUtils.getTime();

        ColorScheme colorScheme = plugin.getColorScheme();

        String colM = colorScheme.getMainColor();
        String colS = colorScheme.getSecondaryColor();
        String colT = colorScheme.getTertiaryColor();

        String ball = DefaultMessages.BALL.parse();

        sender.sendMessage(Locale.get(Msg.CMD_HEADER_INSPECT).toString() + ": " + colT + profile.getName());

        ActivityIndex activityIndex = profile.getActivityIndex(now);

        sender.sendMessage(colT + ball + " " + colM + " Activity Index: " + colS + activityIndex.getFormattedValue() + " | " + activityIndex.getColor());
        sender.sendMessage(colT + ball + " " + colM + " Registered: " + colS + FormatUtils.formatTimeStampYear(profile.getRegistered()));
        sender.sendMessage(colT + ball + " " + colM + " Last Seen: " + colS + FormatUtils.formatTimeStampYear(profile.getLastSeen()));
        sender.sendMessage(colT + ball + " " + colM + " Logged in from: " + colS + profile.getMostRecentGeoInfo().getGeolocation());
        sender.sendMessage(colT + ball + " " + colM + " Playtime: " + colS + FormatUtils.formatTimeAmount(profile.getTotalPlaytime()));
        sender.sendMessage(colT + ball + " " + colM + " Longest Session: " + colS + FormatUtils.formatTimeAmount(profile.getLongestSession(0, now)));
        sender.sendMessage(colT + ball + " " + colM + " Times Kicked: " + colS + profile.getTimesKicked());
        sender.sendMessage("");
        sender.sendMessage(colT + ball + " " + colM + " Player Kills : " + colS + profile.getPlayerKills().count());
        sender.sendMessage(colT + ball + " " + colM + " Mob Kills : " + colS + profile.getMobKillCount());
        sender.sendMessage(colT + ball + " " + colM + " Deaths : " + colS + profile.getDeathCount());

        sender.sendMessage(Locale.get(Msg.CMD_CONSTANT_FOOTER).toString());
    }
}