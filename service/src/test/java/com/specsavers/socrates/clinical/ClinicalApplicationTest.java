package com.specsavers.socrates.clinical;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import com.specsavers.socrates.clinical.model.CurrentSpecsVaDto;
import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy1Dto;
import com.specsavers.socrates.clinical.model.EyeHealthDrugInfoDto;
import com.specsavers.socrates.clinical.model.EyeHealthAndOphthalmoscopy2Dto;
import com.specsavers.socrates.clinical.model.EyeIopDto;
import com.specsavers.socrates.clinical.model.EyeRxDto;
import com.specsavers.socrates.clinical.model.ObjectiveAndIopDto;
import com.specsavers.socrates.clinical.model.OptionRecommendationsDto;
import com.specsavers.socrates.clinical.model.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.PrismDto;
import com.specsavers.socrates.clinical.model.RecommendationsDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.RxNotesDto;
import com.specsavers.socrates.clinical.model.RxOptionTypeDto;
import com.specsavers.socrates.clinical.model.SpecificAdditionDto;
import com.specsavers.socrates.clinical.model.UnaidedVisualAcuityDto;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.CREATE_SIGHT_TEST;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.GET_PRESCRIBEDRX_BY_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.GET_PRESCRIBEDRX_BY_TRNUMBER;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.GET_SIGHT_TEST;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.LEFT_EYE;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.NOT_FOUND_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.RIGHT_EYE;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.SIGHT_TEST;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.STORE_ID_HTTP_HEADER_NAME;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_DISPENSE_NOTE;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_1;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_1_DRUG_INFO;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_2;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_HABITUAL_RX;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_HISTORY_SYMPTOMS;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_OBJECTIVE_AND_IOP;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_OBJECTIVE_AND_IOP_DRUG_INFO;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_OPTION_RECOMMENDATIONS;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_PRESCRIBED_RX;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_PRESCRIBED_RX_NOTE;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_REFRACTED_RX;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_REFRACTED_RX_NOTE;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_PRESCRIBED_RX_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_SIGHT_TEST_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_SIGHT_TEST_VERSION;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_STORE_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_TR_NUMBER_ID;
import static com.specsavers.socrates.clinical.util.TestHelpers.stringOfLength;
import static com.specsavers.socrates.common.util.GraphQLUtils.CORRELATION_ID_HEADER_NAME;
import static java.util.Collections.nCopies;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClinicalApplicationTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Autowired
    private SightTestRepository repository;

    @BeforeEach
    public void setup() {
        // Clear any headers from previous tests and set mandatory storeID
        this.graphQLTestTemplate
                .withClearHeaders()
                .withAdditionalHeader(CORRELATION_ID_HEADER_NAME, UUID.randomUUID().toString())
                .withAdditionalHeader(STORE_ID_HTTP_HEADER_NAME, VALID_STORE_ID);
    }

    @Nested
    class GetPrescribedRxTest {
        @Test
        void testGetPrescriptionWithValidTrNumber() throws IOException {
            var variables = new ObjectMapper().createObjectNode();
            variables.put("trNumber", VALID_TR_NUMBER_ID);

            var graphqlResponse = graphQLTestTemplate.perform(GET_PRESCRIBEDRX_BY_TRNUMBER, variables);

            validateFullResponse(graphqlResponse);
        }

        @Test
        void testGetPrescriptionWithValidID() throws IOException {
            var variables = new ObjectMapper().createObjectNode();
            variables.put("id", VALID_PRESCRIBED_RX_ID);

            var graphqlResponse = graphQLTestTemplate.perform(GET_PRESCRIBEDRX_BY_ID, variables);

            validateFullResponse(graphqlResponse);
        }

        @Test
        void testGetPrescriptionWithNotFoundTrNumber() throws IOException {
            var variables = new ObjectMapper().createObjectNode();
            variables.put("trNumber", NOT_FOUND_ID);

            var graphqlResponse = graphQLTestTemplate.perform(GET_PRESCRIBEDRX_BY_TRNUMBER, variables);

            graphqlResponse
                    .assertThatNumberOfErrors().isEqualTo(1)
                    .and()
                    .assertThatField("$.errors[0].extensions.classification").asString()
                    .isEqualTo("DataFetchingException");
        }
    }

    @Nested
    class CreateSightTestTest {
        @Test
        void testCreateSightTest() throws IOException {
            var variables = new ObjectMapper().createObjectNode();
            variables.put("trNumber", VALID_TR_NUMBER_ID);
            variables.put("type", SIGHT_TEST);

            var graphqlResponse = graphQLTestTemplate.perform(CREATE_SIGHT_TEST, variables);

            graphqlResponse
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.createSightTest.id").as(UUID.class)
                    .and()
                    .assertThatField("$.data.createSightTest.version").asInteger()
                    .isEqualTo(0)
                    .and()
                    .assertThatField("$.data.createSightTest.trNumber").asInteger()
                    .isEqualTo(VALID_TR_NUMBER_ID)
                    .and()
                    .assertThatField("$.data.createSightTest.type").asString()
                    .isEqualTo(SIGHT_TEST);
        }
    }

    @Nested
    class UpdateHabitualRxTest {
        @Test
        void testUpdateHabitualRx() throws IOException {
            var variables = variables()
                    .put("pairNumber", 1)
                    .put("leftCylinder", "+1.50")
                    .put("clinicianName", "new name");

            var response = graphQLTestTemplate.perform(UPDATE_HABITUAL_RX, variables);

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateHabitualRx.habitualRx[0].clinicianName").asString().isEqualTo("new name");
        }

        @Test
        void testUpdateInvalidHabitualRx() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("version", VALID_SIGHT_TEST_VERSION)
                    .put("pairNumber", 1)
                    .put("leftCylinder", "+30.25")
                    .put("clinicianName", "name");

            var response = graphQLTestTemplate.perform(UPDATE_HABITUAL_RX, variables);

            response
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("Left eye: Cylinder must be between -20 and 20")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }
    }

    @Nested
    class UpdateHistoryAndSymptomsTest {
        @Test
        void testUpdateHistoryAndSymptoms() throws IOException {
            var variables = variables()
                    .put("reason", "blurry vision");

            var response = graphQLTestTemplate.perform(UPDATE_HISTORY_SYMPTOMS, variables);

            final var prefix = "$.data.updateHistoryAndSymptoms.historyAndSymptoms.";
            final var lifestyle = prefix + "lifestyle.";
            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField(prefix + "reasonForVisit").asString().isEqualTo("blurry vision")
                    .and().assertThatField(prefix + "generalHealth").asString().isEqualTo("general")
                    .and().assertThatField(prefix + "medication").asString().isEqualTo("medication")
                    .and().assertThatField(prefix + "familyHistory").asString().isEqualTo("family")
                    .and().assertThatField(prefix + "ocularHistory").asString().isEqualTo("ocular")
                    .and().assertThatField(lifestyle + "driveHeavyGoods").asBoolean().isEqualTo(true)
                    .and().assertThatField(lifestyle + "drivePrivate").asBoolean().isEqualTo(true)
                    .and().assertThatField(lifestyle + "drivePublic").asBoolean().isEqualTo(false)
                    .and().assertThatField(lifestyle + "driveMotorcycle").asBoolean().isEqualTo(false)
                    .and().assertThatField(lifestyle + "vdu").asBoolean().isEqualTo(true)
                    .and().assertThatField(lifestyle + "vduHoursPerDay").asInteger().isEqualTo(7)
                    .and().assertThatField(lifestyle + "occupation").asString().isEqualTo("test case")
                    .and().assertThatField(lifestyle + "hobbies").asString().isEqualTo("executing test cases");
        }

        @Test
        void testUpdateHistoryAndSymptomsTriggersValidation() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("version", VALID_SIGHT_TEST_VERSION)
                    .put("reason", String.join("", nCopies(1001, "x")));

            var response = graphQLTestTemplate.perform(UPDATE_HISTORY_SYMPTOMS, variables);

            response
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("reasonForVisit must not be longer than 1000 characters");
        }
    }

    @Nested
    class GetSightTestTest {
        @Test
        void testGetSightTestById() throws IOException {
            var id = VALID_SIGHT_TEST_ID.toString();
            var variables = new ObjectMapper().createObjectNode()
                    .put("id", id);

            var response = graphQLTestTemplate.perform(GET_SIGHT_TEST, variables);

            final var sightTest = "$.data.sightTest.";
            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField(sightTest + "id").asString().isEqualTo(id)
                    .and().assertThatField(sightTest + "version").asInteger()
                    .and().assertThatField(sightTest + "type").asString().isEqualTo("SIGHT_TEST");
        }

        @Test
        void testGetSightTestWhenNotFound() throws IOException {
            String id = UUID.randomUUID().toString();
            var variables = new ObjectMapper().createObjectNode()
                    .put("id", id);

            var response = graphQLTestTemplate.perform(GET_SIGHT_TEST, variables);

            response
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("Exception while fetching data (/sightTest) : Object " + id + " not found");
        }
    }

    @Nested
    class UpdateRefractedRxTest {
        @Test
        void testUpdateRefractedRxNote() throws IOException {
            var variables = variables();
            var note = new RxNotesDto();
            note.setDate(LocalDate.now());
            note.setOptomName("Will Smith");
            note.setText("RefractedRx Notes");

            var response = graphQLTestTemplate.perform(UPDATE_REFRACTED_RX_NOTE, variables);

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateRefractedRxNote.refractedRx.notes").as(RxNotesDto.class).isEqualTo(note);
        }

        @Test
        void testUpdateRefractedRx() throws IOException {
            //Add some notes, so we can validate it returns when updating RefractedRx
            graphQLTestTemplate.perform(UPDATE_REFRACTED_RX_NOTE, variables());
            var response = graphQLTestTemplate.perform(UPDATE_REFRACTED_RX, variables());

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateRefractedRx.refractedRx").as(RefractedRxDto.class).isEqualTo(getRefractedRxValidResponse());
        }

        private RefractedRxDto getRefractedRxValidResponse() {
            var notes = new RxNotesDto();
            notes.setDate(LocalDate.now());
            notes.setOptomName("Will Smith");
            notes.setText("RefractedRx Notes");

            var currentSpecsVa = new CurrentSpecsVaDto();
            currentSpecsVa.setRightEye("10/9");

            var unaidedVisualAcuity = new UnaidedVisualAcuityDto("4/4", null, "24/24");

            var specificAddition = new SpecificAdditionDto();
            specificAddition.setRightEye(5.5f);
            specificAddition.setReason("Main Reason");

            var rightEyePrism = new PrismDto();
            rightEyePrism.setHorizontal("2.25 In");
            rightEyePrism.setVertical("5.5 Up");

            var rightEye = new EyeRxDto();
            rightEye.setSphere("30.25");
            rightEye.setCylinder("1.0");
            rightEye.setAxis(0f);
            rightEye.setDistancePrism(rightEyePrism);

            var leftEyePrism = new PrismDto();
            leftEyePrism.setHorizontal("3.50 In");
            leftEyePrism.setVertical("20 Down");

            var leftEye = new EyeRxDto();
            leftEye.setDistancePrism(leftEyePrism);

            var refractedRx = new RefractedRxDto();
            refractedRx.setBvd(5.5f);
            refractedRx.setDistanceBinVisualAcuity("0123456789");
            refractedRx.setCurrentSpecsVA(currentSpecsVa);
            refractedRx.setUnaidedVisualAcuity(unaidedVisualAcuity);
            refractedRx.setSpecificAddition(specificAddition);
            refractedRx.setRightEye(rightEye);
            refractedRx.setLeftEye(leftEye);
            refractedRx.setNotes(notes);

            return refractedRx;
        }
    }

    @Nested
    class UpdatePrescribedRxTest {
        @Test
        void testUpdatePrescribedRxNote() throws IOException {
            var variables = variables();
            var note = new RxNotesDto();
            note.setDate(LocalDate.now());
            note.setOptomName("Will Smith");
            note.setText("PrescribedRx Notes");

            var response = graphQLTestTemplate.perform(UPDATE_PRESCRIBED_RX_NOTE, variables);

            response
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.updatePrescribedRxNote.prescribedRx.notes").as(RxNotesDto.class).isEqualTo(note);
        }

        @Test
        void testUpdatePrescribedRx() throws IOException {
            //Add some notes, so we can validate it returns when updating PrescribedRx
            graphQLTestTemplate.perform(UPDATE_PRESCRIBED_RX_NOTE, variables());
            var response = graphQLTestTemplate.perform(UPDATE_PRESCRIBED_RX, variables());

            response
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.updatePrescribedRx.prescribedRx").as(PrescribedRxDto.class).isEqualTo(getPrescribedRxValidResponse());
        }

        private PrescribedRxDto getPrescribedRxValidResponse(){
            var notes = new RxNotesDto();
            notes.setDate(LocalDate.now());
            notes.setOptomName("Will Smith");
            notes.setText("PrescribedRx Notes");

            var unaidedVisualAcuity = new UnaidedVisualAcuityDto("4/4", null, "24/24");
            
            var rightEyePrism = new PrismDto();
            rightEyePrism.setHorizontal("2.25 In");
            rightEyePrism.setVertical("5.5 Up");

            var rightEye = new EyeRxDto();
            rightEye.setSphere("30.25");
            rightEye.setCylinder("1.0");
            rightEye.setAxis(0f);
            rightEye.setDistancePrism(rightEyePrism);

            var leftEyePrism = new PrismDto();
            leftEyePrism.setHorizontal("3.50 In");
            leftEyePrism.setVertical("20 Down");

            var leftEye = new EyeRxDto();
            leftEye.setDistancePrism(leftEyePrism);
            leftEye.setSphere("BAL");
            leftEye.setBalSphere("+2.50");

            var prescribedRx = new PrescribedRxDto();
            prescribedRx.setBvd(5.5f);
            prescribedRx.setDistanceBinVisualAcuity("0123456789");
            prescribedRx.setUnaidedVisualAcuity(unaidedVisualAcuity);
            prescribedRx.setRightEye(rightEye);
            prescribedRx.setLeftEye(leftEye);
            prescribedRx.setNotes(notes);
            prescribedRx.setRecallPeriod(24);

            return prescribedRx;
        }
    }

    @Nested
    class UpdateObjectiveAndIopTest {
        private final DateTimeFormatter expiryFormat = DateTimeFormatter.ofPattern("MM/yyyy");

        @Test
        void testUpdateObjectiveAndIopDrugInfo() throws IOException {
            var mapper = new ObjectMapper();
            var drugInfo = new DrugInfoDto();
            drugInfo.setTime("00:30");
            drugInfo.setBatchNo("AX123");
            drugInfo.setDrugUsed("drugName");
            String expiry = YearMonth.now().format(expiryFormat);
            drugInfo.setExpiryDate(expiry);

            var variables = variables();
            variables.set("input", mapper.valueToTree(drugInfo));

            var response = graphQLTestTemplate.perform(UPDATE_OBJECTIVE_AND_IOP_DRUG_INFO, variables);

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateObjectiveAndIopDrugInfo.objectiveAndIop.drugInfo").as(DrugInfoDto.class).isEqualTo(drugInfo);
        }

        @Test
        void testUpdateObjectiveAndIopDrugInfoTriggersValidation() throws IOException {
            var mapper = new ObjectMapper();
            var drugInfo = new DrugInfoDto();
            drugInfo.setExpiryDate("99/2021");

            var variables = mapper.createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("version", VALID_SIGHT_TEST_VERSION);
            variables.set("input", mapper.valueToTree(drugInfo));

            graphQLTestTemplate.perform(UPDATE_OBJECTIVE_AND_IOP_DRUG_INFO, variables)
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("ExpiryDate is not in correct format 'MM/YYYY'")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }

        @Test
        void testUpdateObjectiveAndIop() throws IOException {
            var mapper = new ObjectMapper();
            var objectiveAndIopDto = getObjectiveAndIopDto();

            ObjectNode input = mapper.valueToTree(objectiveAndIopDto);
            input.remove("drugInfo");

            var variables = variables();
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_OBJECTIVE_AND_IOP, variables)
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateObjectiveAndIop.objectiveAndIop").as(ObjectiveAndIopDto.class)
                    .isEqualTo(getObjectiveAndIopDto());
        }

        @Test
        void testUpdateObjectiveAndIopTriggersValidation() throws IOException {
            var mapper = new ObjectMapper();
            var objectiveAndIopDto = getObjectiveAndIopDto();
            objectiveAndIopDto.setTime("99:99");

            ObjectNode input = mapper.valueToTree(objectiveAndIopDto);
            input.remove("drugInfo");

            var variables = mapper.createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("version", VALID_SIGHT_TEST_VERSION);
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_OBJECTIVE_AND_IOP, variables)
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("Time is not in correct format 'HH:mm'")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }

        private ObjectiveAndIopDto getObjectiveAndIopDto(){
            var rightEye = new EyeIopDto();
            rightEye.setSphere("-2.50");
            rightEye.setCylinder(-2.50f);
            rightEye.setAxis(180f);
            rightEye.setIop1(10);
            rightEye.setIop2(20);
            rightEye.setIop3(30);
            rightEye.setIop4(40);
            rightEye.setVisualAcuity("6/6");

            var objectiveAndIop = new ObjectiveAndIopDto();
            objectiveAndIop.setTime("12:50");
            objectiveAndIop.setNotes("Some Notes");
            objectiveAndIop.setRightEye(rightEye);

            return objectiveAndIop;
        }
    }

    @Nested
    class UpdateOptionRecommendationsTests {
        @Test
        void testUpdateDispenseNotes() throws IOException {
            var input = "Some Notes";

            var variables = variables()
                .put("input", input);

            var response = graphQLTestTemplate.perform(UPDATE_DISPENSE_NOTE, variables);

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateDispenseNote.dispenseNotes").asString().isEqualTo(input);
        }

        @Test
        void testUpdateDispenseNotesTriggersValidation() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("version", VALID_SIGHT_TEST_VERSION)
                    .put("input", stringOfLength(250));

            var response = graphQLTestTemplate.perform(UPDATE_DISPENSE_NOTE, variables);

            response.assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("Text must not be longer than 220 characters")
                    .and()
                    .assertThatField("$.data")
                    .isNull();
        }

        @Test
        void testUpdateOptionRecommendations() throws IOException {
            var mapper = new ObjectMapper();
            var optionRecommendations = getOptionRecommendations();
            var variables = variables();
            variables.set("input", mapper.valueToTree(optionRecommendations));

            graphQLTestTemplate.perform(UPDATE_OPTION_RECOMMENDATIONS, variables)
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateOptionRecommendations.optionRecommendations").as(OptionRecommendationsDto.class)
                    .isEqualTo(optionRecommendations);
        }

        @Test
        void testUpdateOptionRecommendationsTriggersValidation() throws IOException {
            var mapper = new ObjectMapper();
            var optionRecommendations = getOptionRecommendations();
            optionRecommendations.setRxOptionType(RxOptionTypeDto.NO_RX_REQUIRED);

            ObjectNode input = mapper.valueToTree(optionRecommendations);
            //input.remove("drugInfo");

            var variables = mapper.createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("version", VALID_SIGHT_TEST_VERSION);
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_OPTION_RECOMMENDATIONS, variables)
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("No recommendations are allowed when RxOptionType = NO_RX_REQUIRED")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }

        private OptionRecommendationsDto getOptionRecommendations(){
            var rec = new RecommendationsDto();
            rec.setPolar(true);
            rec.setReact(true);
            rec.setTints(true);
            rec.setThinAndLight(true);
            rec.setUltraTough(true);
            rec.setVari(true);
            rec.setSvn(true);
            rec.setSvd(true);

            var optionRecommendations = new OptionRecommendationsDto();
            optionRecommendations.setRxOptionType(RxOptionTypeDto.NEW_RX);
            optionRecommendations.setReferToDoctor(true);
            optionRecommendations.setRecommendations(rec);

            return optionRecommendations;
        }
    }

    @Nested
    class UpdateEyeHealthAndOphthalmoscopy1Tests {

        @Test
        void testUpdateEyeHealthDrugInfo() throws IOException {
            var drugInfo = new DrugInfoDto();
            drugInfo.setExpiryDate("12/2050");

            var eyeHealthDrugInfo = EyeHealthDrugInfoDto.builder()
                .drugInfo(drugInfo)
                .prePressure("PrePressure")
                .postPressure("PostPressure")
                .prePressureTime("15:22")
                .postPressureTime("11:36")
                .build();

            var variables = variables();
            variables.set("input", mapper.valueToTree(eyeHealthDrugInfo));

            graphQLTestTemplate.perform(UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_1_DRUG_INFO, variables)
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.updateEyeHealthAndOphthalmoscopy1DrugInfo.eyeHealthAndOphthalmoscopy1.drugInfoEyeHealth")
                .as(EyeHealthDrugInfoDto.class)
                .isEqualTo(eyeHealthDrugInfo);
        }

        @Test
        void testUpdateEyeHealthDrugInfoTriggersValidation() throws IOException {
            var drugInfo = new EyeHealthDrugInfoDto();
            drugInfo.setPostPressure(stringOfLength(50));

            var variables = variables();
            variables.set("input", mapper.valueToTree(drugInfo));

            graphQLTestTemplate.perform(UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_1_DRUG_INFO, variables)
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("PostPressure must not be longer than 25 characters")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }

        @Test
        void testUpdateEyeHealthAndOphthalmoscopy1() throws IOException {
            var eyeHealth = new EyeHealthAndOphthalmoscopy1Dto();
            eyeHealth.setExternalEyeLeft("ExternalEyeLeft");
            eyeHealth.setVolk(true);

            ObjectNode input = mapper.valueToTree(eyeHealth);
            input.remove("drugInfoEyeHealth");

            var variables = variables();
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_1, variables)
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateEyeHealthAndOphthalmoscopy1.eyeHealthAndOphthalmoscopy1")
                    .as(EyeHealthAndOphthalmoscopy1Dto.class)
                    .isEqualTo(eyeHealth);
        }

        @Test
        void testUpdateEyeHealthAndOphthalmoscopy1TriggersValidation() throws IOException {
            var eyeHealth = new EyeHealthAndOphthalmoscopy1Dto();
            eyeHealth.setExternalEyeLeft(stringOfLength(600));

            ObjectNode input = mapper.valueToTree(eyeHealth);
            input.remove("drugInfoEyeHealth");

            var variables = variables();
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_1, variables)
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("ExternalEyeLeft must not be longer than 550 characters")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }
    }

    @Nested
    class UpdateEyeHealthAndOphthalmoscopy2Tests {
        @Test
        void testUpdateEyeHealthAndOphthalmoscopy2() throws IOException {
            var eyeHealth = EyeHealthAndOphthalmoscopy2Dto
                    .builder()
                    .lensLeft("lensLeft")
                    .lensRight("lensRight")
                    .vitreousLeft("vitreousLeft")
                    .vitreousRight("vitreousRight")
                    .build();

            ObjectNode input = mapper.valueToTree(eyeHealth);

            var variables = variables();
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_2, variables)
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateEyeHealthAndOphthalmoscopy2.eyeHealthAndOphthalmoscopy2")
                    .as(EyeHealthAndOphthalmoscopy2Dto.class)
                    .isEqualTo(eyeHealth);
        }

        @Test
        void testUpdateEyeHealthAndOphthalmoscopy2TriggersValidation() throws IOException {
            var eyeHealth = EyeHealthAndOphthalmoscopy2Dto
                    .builder()
                    .lensLeft(stringOfLength(401))
                    .lensRight("lensRight")
                    .vitreousLeft("vitreousLeft")
                    .vitreousRight("vitreousRight")
                    .build();

            ObjectNode input = mapper.valueToTree(eyeHealth);

            var variables = variables();
            variables.set("input", input);

            graphQLTestTemplate.perform(UPDATE_EYE_HEALTH_AND_OPHTHALMOSCOPY_2, variables)
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("lensLeft must not be longer than 400 characters")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }
    }

    private void validateFullResponse(GraphQLResponse graphQLResponse) {
        graphQLResponse
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.prescribedRX.id").asInteger()
                .isEqualTo(30)
                .and()
                .assertThatField("$.data.prescribedRX.recallPeriod").asInteger()
                .isEqualTo(12)
                .and()
                .assertThatField("$.data.prescribedRX.testRoomNumber").asInteger()
                .isEqualTo(VALID_TR_NUMBER_ID)
                .and()
                .assertThatField("$.data.prescribedRX.bvd").asInteger()
                .isEqualTo(12)
                .and()
                .assertThatField("$.data.prescribedRX.testDate").asString()
                .isEqualTo("2021-04-19T12:00:00Z")
                .and()
                .assertThatField("$.data.prescribedRX.distanceBinVisualAcuity").asString()
                .isEqualTo("6/5")
                .and()
                .assertThatField("$.data.prescribedRX.dispenseNotes").asString()
                .isEqualTo("Some dispense notes")
                .and()
                .assertThatField("$.data.prescribedRX.recommendations").asString()
                .isEqualTo("No RX, No Change")
                .and()
                .assertThatField("$.data.prescribedRX.clinicianName").asString()
                .isEqualTo("Joe Bloggs")
                .and()
                .assertThatField("$.data.prescribedRX.rightEye").as(EyeRX.class)
                .isEqualTo(RIGHT_EYE)
                .and()
                .assertThatField("$.data.prescribedRX.leftEye").as(EyeRX.class)
                .isEqualTo(LEFT_EYE);
    }

    private ObjectNode variables() {
        var version = repository.findById(VALID_SIGHT_TEST_ID)
                .orElseThrow()
                .getVersion();
        return new ObjectMapper().createObjectNode()
                .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                .put("version", version);
    }
}
