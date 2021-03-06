/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package utilities;

import com.djrapitops.plan.system.settings.Settings;

/**
 * Test utility for {@code @Teardown} tags.
 *
 * @author Rsl1122
 */
public class Teardown {

    public static void resetSettingsTempValues() {
        for (Settings settings : Settings.values()) {
            settings.setTemporaryValue(null);
        }
    }
}