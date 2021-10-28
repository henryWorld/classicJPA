package com.specsavers.socrates.clinical.Utils;

import static java.util.Collections.nCopies;

public class StaticHelpers {
    private StaticHelpers() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    public static String StringOfLength(int length) {
        return String.join("", nCopies(length, "a"));
    }
}
