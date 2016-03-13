package de.admir.util;

import java.util.Comparator;


/**
 * Author:  Admir Memic
 * Date:    11.03.2016
 * E-Mail:  admir.memic@dmc.de
 */
public enum ParentComparator implements Comparator<Long> {
    INSTANCE;

    @Override
    public int compare(Long p1, Long p2) {
        if (p1 == null && p2 == null)
            return 0;
        if (p1 == null)
            return 1;
        if (p2 == null)
            return -1;
        return p1.compareTo(p2);
    }
}
