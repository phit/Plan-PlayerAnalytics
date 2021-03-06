/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djrapitops.plan.utilities.comparators;

import com.djrapitops.plan.data.PlayerProfile;

import java.util.Comparator;

/**
 * Comparator for PlayerProfile so that most recently seen is first.
 *
 * @author Rsl1122
 */
public class PlayerProfileLastPlayedComparator implements Comparator<PlayerProfile> {

    @Override
    public int compare(PlayerProfile u1, PlayerProfile u2) {
        return Long.compare(u2.getLastSeen(), u1.getLastSeen());
    }
}
