/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package com.djrapitops.plan.api;

import com.djrapitops.plan.api.exceptions.database.DBException;
import com.djrapitops.plan.utilities.uuid.UUIDUtility;
import com.djrapitops.plugin.api.utility.log.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PlanAPI extension for all implementations.
 *
 * @author Rsl1122
 */
public abstract class CommonAPI implements PlanAPI {

    @Override
    public String getPlayerInspectPageLink(UUID uuid) {
        return getPlayerInspectPageLink(getPlayerName(uuid));
    }

    @Override
    public String getPlayerInspectPageLink(String playerName) {
        return "../player/" + playerName;
    }

    @Override
    public UUID playerNameToUUID(String playerName) {
        return UUIDUtility.getUUIDOf(playerName);
    }

    @Override
    public Map<UUID, String> getKnownPlayerNames() {
        try {
            return fetchFromPlanDB().getPlayerNames();
        } catch (DBException e) {
            Log.toLog(this.getClass(), e);
            return new HashMap<>();
        }
    }

}