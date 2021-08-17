package com.specsavers.socrates.clinical.Utils;

import com.specsavers.socrates.clinical.model.rx.EyeRX;
import com.specsavers.socrates.clinical.model.rx.Prism;

public class CommonStaticValues {
    public static final String GET_PRESCRIBEDRX_BY_TRNUMBER = "graphql/query/get_prescribedRX_by_trNumber.graphql";
    public static final String GET_PRESCRIBEDRX_BY_ID = "graphql/query/get_prescribedRX_by_ID.graphql";
    public static final String UNKNOWN_REQUEST = "graphql/query/unknown_request.graphql";

    public static final int VALID_PRESCRIBEDRX_ID = 30;
    public static final int VALID_TR_NUMBER_ID = 123;
    public static final int NOT_FOUND_ID = 999;
    
    public static final EyeRX RIGHT_EYE = EyeRX.builder()
        .axis(50)
        .cylinder("-1.00")
        .distancePrism(Prism.builder().horizontal("3.00 Out").vertical("7.00 Down").build())
        .distanceVisualAcuity("6/6")
        .interAddtion(5f)
        .nearAddition(2.5f)
        .nearPrism(Prism.builder().horizontal("2.00 In").vertical("7.00 Down").build())
        .nearVisualAcuity("N5")
        .pupillaryDistance(29f)
        .sphere("-3.50")
        .build();
    public static final EyeRX LEFT_EYE = EyeRX.builder()
        .axis(115)
        .cylinder("-0.50")
        .distancePrism(Prism.builder().horizontal("4.00 Out").vertical("7.00 Up").build())
        .distanceVisualAcuity("6/6")
        .interAddtion(8f)
        .nearAddition(2.25f)
        .nearPrism(Prism.builder().horizontal("2.50 In").vertical("6.00 Down").build())
        .nearVisualAcuity("N5")
        .pupillaryDistance(29f)
        .sphere("-3.25")
        .build();
}
