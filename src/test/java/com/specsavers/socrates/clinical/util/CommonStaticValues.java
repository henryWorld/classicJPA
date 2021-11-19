package com.specsavers.socrates.clinical.util;

import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import com.specsavers.socrates.clinical.legacy.model.rx.Prism;

import java.util.UUID;

public class CommonStaticValues {
    public static final String CREATE_SIGHT_TEST = "graphql/mutation/create_sight_test.graphql";
    public static final String GET_PRESCRIBEDRX_BY_TRNUMBER = "graphql/query/get_prescribedRX_by_trNumber.graphql";
    public static final String GET_PRESCRIBEDRX_BY_ID = "graphql/query/get_prescribedRX_by_ID.graphql";
    public static final String CREATE_HABITUAL_RX = "graphql/mutation/create_habitual_rx.graphql";
    public static final String UPDATE_HABITUAL_RX = "graphql/mutation/update_habitual_rx.graphql";
    public static final String UPDATE_REFRACTED_RX = "graphql/mutation/update_refracted_rx.graphql";
    public static final String UPDATE_REFRACTED_RX_NOTE = "graphql/mutation/update_refracted_rx _note.graphql";
    public static final String UPDATE_PRESCRIBED_RX = "graphql/mutation/update_prescribed_rx.graphql";
    public static final String UPDATE_PRESCRIBED_RX_NOTE = "graphql/mutation/update_prescribed_rx _note.graphql";
    public static final String UPDATE_HISTORY_SYMPTOMS = "graphql/mutation/update_history_symptoms.graphql";
    public static final String GET_SIGHT_TEST = "graphql/query/get_sight_test.graphql";
    public static final String UNKNOWN_REQUEST = "graphql/query/unknown_request.graphql";
    public static final String UPDATE_OBJECTIVE_AND_IOP = "graphql/mutation/update_objective_and_iop.graphql";
    public static final String UPDATE_OBJECTIVE_AND_IOP_DRUG_INFO = "graphql/mutation/update_objective_and_iop_drug_info.graphql";

    public static final String VALID_STORE_ID = "8306";
    public static final String STORE_ID_HTTP_HEADER_NAME = "SOCRATES_STORE_ID";
    public static final int VALID_PRESCRIBED_RX_ID = 30;
    public static final int VALID_TR_NUMBER_ID = 123;
    public static final int VALID_CUSTOMER_ID = 5001;
    public static final int NOT_FOUND_ID = 999;
    public static final String SIGHT_TEST = "SIGHT_TEST";
    public static final String BALANCED = "BAL";
    public static final UUID VALID_SIGHT_TEST_ID = UUID.fromString("17396D3B-FD1F-4454-B309-41990D705E6B");
    public static final UUID VALID_HABITUAL_RX_ID = UUID.fromString("9A759CFA-E8D8-4DD8-932D-84F9DE23956C");

    public static final EyeRX RIGHT_EYE = EyeRX.builder()
            .axis(50f)
            .cylinder("-1.00")
            .distancePrism(Prism.builder().horizontal("3.00 Out").vertical("7.00 Down").build())
            .distanceVisualAcuity("6/6")
            .interAddition(5f)
            .nearAddition(2.5f)
            .nearPrism(Prism.builder().horizontal("2.00 In").vertical("7.00 Down").build())
            .nearVisualAcuity("N5")
            .pupillaryDistance(29f)
            .sphere("-3.50")
            .build();
    public static final EyeRX LEFT_EYE = EyeRX.builder()
            .axis(115f)
            .cylinder("-0.50")
            .distancePrism(Prism.builder().horizontal("4.00 Out").vertical("7.00 Up").build())
            .distanceVisualAcuity("6/6")
            .interAddition(8f)
            .nearAddition(2.25f)
            .nearPrism(Prism.builder().horizontal("2.50 In").vertical("6.00 Down").build())
            .nearVisualAcuity("N5")
            .pupillaryDistance(29f)
            .sphere("-3.25")
            .build();
}
