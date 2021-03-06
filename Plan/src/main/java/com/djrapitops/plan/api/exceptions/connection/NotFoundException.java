/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package com.djrapitops.plan.api.exceptions.connection;

/**
 * Thrown when Connection returns 404, when page is not found.
 *
 * @author Rsl1122
 */
public class NotFoundException extends WebFailException {
    public NotFoundException(String message) {
        super(message);
    }
}