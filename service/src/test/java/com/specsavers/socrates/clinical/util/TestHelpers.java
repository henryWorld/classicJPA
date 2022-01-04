package com.specsavers.socrates.clinical.util;

import lombok.experimental.UtilityClass;

import static java.util.Collections.nCopies;

@UtilityClass
public class TestHelpers {
    public static String stringOfLength(int length) {
        return String.join("", nCopies(length, "a"));
    }
}
