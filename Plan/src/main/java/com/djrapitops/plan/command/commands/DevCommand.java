/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package com.djrapitops.plan.command.commands;

import com.djrapitops.plan.system.info.connection.ConnectionSystem;
import com.djrapitops.plan.system.settings.locale.Locale;
import com.djrapitops.plan.system.settings.locale.Msg;
import com.djrapitops.plan.utilities.Condition;
import com.djrapitops.plugin.command.CommandNode;
import com.djrapitops.plugin.command.CommandType;
import com.djrapitops.plugin.command.ISender;

/**
 * Command used for testing functions that are too difficult to unit test.
 *
 * @author Rsl1122
 */
public class DevCommand extends CommandNode {

    public DevCommand() {
        super("dev", "plan.*", CommandType.PLAYER_OR_ARGS);
        setShortHelp("Test Plugin functions not testable with unit tests.");
        setArguments("<feature>");
    }

    @Override
    public void onCommand(ISender sender, String cmd, String[] args) {
        if (!Condition.isTrue(args.length >= 1, Locale.get(Msg.CMD_FAIL_REQ_ONE_ARG).toString(), sender)) {
            return;
        }
        String feature = args[0];
        switch (feature) {
            case "connection":
                if (!Condition.isTrue(args.length >= 2, Locale.get(Msg.CMD_FAIL_REQ_ONE_ARG).toString(), sender)) {
                    break;
                }
                sender.sendMessage("[Plan] No implementation.");
                break;
            case "web":
                ConnectionSystem connectionSystem = ConnectionSystem.getInstance();
                String accessAddress = connectionSystem.getMainAddress();
                sender.sendMessage((connectionSystem.isServerAvailable())
                        ? "Bungee: " + accessAddress : "Local: " + accessAddress);
                break;
            default:
                break;
        }
    }
}