package com.specsavers.socrates.clinical.util;

import static java.util.Collections.nCopies;

public class StaticHelpers {
    private StaticHelpers() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static String stringOfLength(int length) {
        return String.join("", nCopies(length, "a"));
    }
}
