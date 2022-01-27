package com.specsavers.socrates.clinical.model.validation.common;

import lombok.experimental.UtilityClass;

import static java.util.Collections.nCopies;

@UtilityClass
public class TestHelpers {
    public static final String BALANCED = "BAL";
    
    public static String stringOfLength(Integer length) {
        if (length == null) {
            return null;
        }
        return String.join("", nCopies(length, "a"));
    }
}
