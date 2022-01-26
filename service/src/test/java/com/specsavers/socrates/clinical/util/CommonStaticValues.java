package com.specsavers.socrates.clinical.util;

import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import com.specsavers.socrates.clinical.legacy.model.rx.Prism;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.entity.ContactLensAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessment;
import com.specsavers.socrates.clinical.model.entity.TearAssessmentEye;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CommonStaticValues {
    public static final String CREATE_SIGHT_TEST = "graphql/mutation/create_sight_test.graphql";
    public static final String CREATE_CL_ASSESSMENT = "graphql/mutation/create_cl_assessment.graphql";
    public static final String GET_PRESCRIBEDRX_BY_TRNUMBER = "graphql/query/get_prescribedRX_by_trNumber.graphql";
    public static final String GET_PRESCRIBEDRX_BY_ID = "graphql/query/get_prescribedRX_by_ID.graphql";
    public static final String CREATE_HABITUAL_RX = "graphql/mutation/create_habitual_rx.graphql";
    public static final String UPDATE_HABITUAL_RX = "graphql/mutation/update_habitual_rx.graphql";
    public static final String UPDATE_TEAR_ASSESSMENT = "graphql/mutation/update_tear_assessment.graphql";
    public static final String UPDATE_TEAR_ASSESSMENT_VALIDATION = "graphql/mutation/update_tear_assessment_validation.graphql";
    public static final String UPDATE_REFRACTED_RX = "graphql/mutation/update_refracted_rx.graphql";
    public static final String UPDATE_REFRACTED_RX_NOTE = "graphql/mutation/update_refracted_rx_note.graphql";
    public static final String UPDATE_PRESCRIBED_RX = "graphql/mutation/update_prescribed_rx.graphql";
    public static final String UPDATE_PRESCRIBED_RX_NOTE = "graphql/mutation/update_prescribed_rx_note.graphql";
    public static final String UPDATE_HISTORY_SYMPTOMS = "graphql/mutation/update_history_symptoms.graphql";
    public static final String GET_SIGHT_TEST = "graphql/query/get_sight_test.graphql";
    public static final String GET_CL_ASSESSMENT = "graphql/query/get_cl_assessment.graphql";
    public static final String GET_TEAR_ASSESSMENT = "graphql/query/get_tear_assessment.graphql";
    public static final String UNKNOWN_REQUEST = "graphql/query/unknown_request.graphql";
    public static final String UPDATE_OBJECTIVE_AND_IOP = "graphql/mutation/update_objective_and_iop.graphql";
    public static final String UPDATE_OBJECTIVE_AND_IOP_DRUG_INFO = "graphql/mutation/update_objective_and_iop_drug_info.graphql";
    public static final String UPDATE_OPTION_RECOMMENDATIONS = "graphql/mutation/update_option_recommendations.graphql";
    public static final String UPDATE_DISPENSE_NOTE = "graphql/mutation/update_dispense_note.graphql";

    public static final String VALID_STORE_ID = "8306";
    public static final String STORE_ID_HTTP_HEADER_NAME = "SOCRATES_STORE_ID";
    public static final int VALID_PRESCRIBED_RX_ID = 30;
    public static final int VALID_TR_NUMBER_ID = 123;
    public static final int VALID_CUSTOMER_ID = 5001;
    public static final int NOT_FOUND_ID = 999;
    public static final String SIGHT_TEST = "SIGHT_TEST";
    public static final UUID VALID_SIGHT_TEST_ID = UUID.fromString("17396D3B-FD1F-4454-B309-41990D705E6B");
    public static final UUID VALID_CONTACT_LENS_ID = UUID.fromString("20354d7a-e4fe-47af-8ff6-187bca92f3f9");
    public static final long VALID_SIGHT_TEST_VERSION = 1L;
    public static final long VALID_VERSION = 0L;

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

    public static final ContactLensAssessment.ContactLensAssessmentBuilder CONTACT_LENS_ASSESSMENT = ContactLensAssessment.builder()
            .id(VALID_CONTACT_LENS_ID)
            .trNumber(23)
            .version(VALID_VERSION)
            .creationDate(OffsetDateTime.now())
            .updatedDate(OffsetDateTime.now());

    public static final ContactLensAssessmentDto.ContactLensAssessmentDtoBuilder CONTACT_LENS_ASSESSMENT_DTO = ContactLensAssessmentDto.builder()
            .id(VALID_CONTACT_LENS_ID)
            .version(VALID_VERSION)
            .creationDate(OffsetDateTime.now());

    public static final TearAssessmentDto.TearAssessmentDtoBuilder TEAR_ASSESSMENT_DTO = TearAssessmentDto
            .builder()
            .leftEye(TearAssessmentEyeDto.builder().build())
            .rightEye(TearAssessmentEyeDto.builder().build());

    public static final TearAssessment.TearAssessmentBuilder TEAR_ASSESSMENT_ENTITY = TearAssessment.builder()
            .leftEye(TearAssessmentEye.builder().prism("DDDD").build());


    public static final TearAssessmentEyeDto.TearAssessmentEyeDtoBuilder TEAR_ASSESSMENT_EYE_DTO =
            TearAssessmentEyeDto.builder().prism("yyyy");

    public static final TearAssessmentEye.TearAssessmentEyeBuilder TEAR_ASSESSMENT_EYE_ENTITY =
            TearAssessmentEye.builder().prism("DDDD");

}
