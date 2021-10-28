package com.specsavers.socrates.clinical;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import com.specsavers.socrates.clinical.model.type.CurrentSpecsVaDto;
import com.specsavers.socrates.clinical.model.type.EyeRxDto;
import com.specsavers.socrates.clinical.model.type.PrismDto;
import com.specsavers.socrates.clinical.model.type.RefractedRxDto;
import com.specsavers.socrates.clinical.model.type.RxNotesDto;
import com.specsavers.socrates.clinical.model.type.SpecificAdditionDto;
import com.specsavers.socrates.clinical.model.type.UnaidedVisualAcuityDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.CREATE_HABITUAL_RX;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.CREATE_SIGHT_TEST;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.GET_PRESCRIBEDRX_BY_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.GET_PRESCRIBEDRX_BY_TRNUMBER;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.GET_SIGHT_TEST;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.LEFT_EYE;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.NOT_FOUND_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.RIGHT_EYE;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.SIGHT_TEST;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.STORE_ID_HTTP_HEADER_NAME;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.UPDATE_HABITUAL_RX;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.UPDATE_REFRACTED_RX;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.UPDATE_REFRACTED_RX_NOTE;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.UPDATE_HISTORY_SYMPTOMS;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_HABITUAL_RX_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_PRESCRIBEDRX_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_SIGHT_TEST_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_STORE_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;
import static java.util.Collections.nCopies;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClinicalApplicationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @BeforeEach
    public void setup() {
        // Clear any headers from previous tests and set mandatory storeID
        this.graphQLTestTemplate
                .withClearHeaders()
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
            variables.put("id", VALID_PRESCRIBEDRX_ID);

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
                    .assertThatField("$.data.createSightTest.trNumber").asInteger()
                    .isEqualTo(VALID_TR_NUMBER_ID)
                    .and()
                    .assertThatField("$.data.createSightTest.type").asString()
                    .isEqualTo(SIGHT_TEST);
        }
    }

    @Nested
    class CreateHabitualRxTest {
        @Test
        void testCreateHabitualRx() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("pairNumber", 4)
                    .put("name", "a name")
                    .put("leftCylinder", "+2.50");

            var response = graphQLTestTemplate.perform(CREATE_HABITUAL_RX, variables);

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.createHabitualRx.id").as(UUID.class).isNotNull()
                    .and()
                    .assertThatField("$.data.createHabitualRx.pairNumber").asInteger().isEqualTo(4)
                    .and()
                    .assertThatField("$.data.createHabitualRx.clinicianName").asString().isEqualTo("a name")
                    .and()
                    .assertThatField("$.data.createHabitualRx.leftEye.cylinder").asString().isEqualTo("+2.50");
        }

        @Test
        @DisplayName("Test createHabitualRx with an invalid rx")
        void testCreateHabitualRxTriggersValidation() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("pairNumber", 4)
                    .put("name", "a name")
                    .put("leftCylinder", "+30.25");

            var graphQLResponse = graphQLTestTemplate.perform(CREATE_HABITUAL_RX, variables);

            graphQLResponse
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("Left cylinder must be between -20.0 and 20.0")
                    .and()
                    .assertThatDataField()
                    .isNull();
        }
    }

    @Nested
    class UpdatedHabitualRxTest {
        @Test
        void testUpdateHabitualRx() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("id", VALID_HABITUAL_RX_ID.toString())
                    .put("clinicianName", "new name");

            var response = graphQLTestTemplate.perform(UPDATE_HABITUAL_RX, variables);

            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField("$.data.updateHabitualRx.id").as(UUID.class).isEqualTo(VALID_HABITUAL_RX_ID)
                    .and()
                    .assertThatField("$.data.updateHabitualRx.clinicianName").asString().isEqualTo("new name");
        }
    }

    @Nested
    class UpdateHistoryAndSymptomsTest {
        @Test
        void testUpdateHistoryAndSymptoms() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString())
                    .put("reason", "blurry vision");

            var response = graphQLTestTemplate.perform(UPDATE_HISTORY_SYMPTOMS, variables);

            var data = "$.data.updateHistoryAndSymptoms.";
            var lifestyle = data + "lifestyle.";
            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField(data + "reasonForVisit").asString().isEqualTo("blurry vision")
                    .and().assertThatField(data + "generalHealth").asString().isEqualTo("general")
                    .and().assertThatField(data + "medication").asString().isEqualTo("medication")
                    .and().assertThatField(data + "familyHistory").asString().isEqualTo("family")
                    .and().assertThatField(data + "ocularHistory").asString().isEqualTo("ocular")
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

            var sightTest = "$.data.sightTest.";
            response
                    .assertThatNoErrorsArePresent()
                    .assertThatField(sightTest + "id").asString().isEqualTo(id)
                    .and().assertThatField(sightTest + "type").asString().isEqualTo("SIGHT_TEST");
        }

        @Test
        void testGetSightTestWhenNotFound() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("id", UUID.randomUUID().toString());

            var response = graphQLTestTemplate.perform(GET_SIGHT_TEST, variables);

            response
                    .assertThatField("$.errors[*].message")
                    .asListOf(String.class)
                    .contains("Exception while fetching data (/sightTest) : Object not found");
        }
    }

    @Nested
    class UpdateRefractedRxTest {
        @Test
        void testUpdateRefractedRxNote() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString());
            var note = new RxNotesDto();
            note.setDate(LocalDate.now());
            note.setOptomName("Will Smith");
            note.setText("RefractedRx Notes");

            var response = graphQLTestTemplate.perform(UPDATE_REFRACTED_RX_NOTE, variables);

            response
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.updateRefractedRxNote").as(RxNotesDto.class).isEqualTo(note);
        }

        @Test
        void testUpdateRefractedRx() throws IOException {
            var variables = new ObjectMapper().createObjectNode()
                    .put("sightTestId", VALID_SIGHT_TEST_ID.toString());
            
            //Add some notes so we can validate it returns when updating RefractedRx
            graphQLTestTemplate.perform(UPDATE_REFRACTED_RX_NOTE, variables);
            var response = graphQLTestTemplate.perform(UPDATE_REFRACTED_RX, variables);

            response
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.updateRefractedRx").as(RefractedRxDto.class).isEqualTo(getRefractedRxValidResponse());
        }

        private RefractedRxDto getRefractedRxValidResponse(){
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
}
