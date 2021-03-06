/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package com.djrapitops.plan.api.exceptions.connection;

/**
 * Thrown when Connection returns 500.
 *
 * @author Rsl1122
 */
public class InternalErrorException extends WebFailException {
    public InternalErrorException() {
        super("Internal Error occurred on receiving server");
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}