package com.specsavers.socrates.clinical;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.legacy.model.rx.EyeRX;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.CREATE_HABITUAL_RX;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.CREATE_SIGHT_TEST;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.GET_PRESCRIBEDRX_BY_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.GET_PRESCRIBEDRX_BY_TRNUMBER;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.LEFT_EYE;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.NOT_FOUND_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.RIGHT_EYE;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.SIGHT_TEST;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.STORE_ID_HTTP_HEADER_NAME;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.UPDATE_HABITUAL_RX;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_HABITUAL_RX_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_PRESCRIBEDRX_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_SIGHT_TEST_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_STORE_ID;
import static com.specsavers.socrates.clinical.Utils.CommonStaticValues.VALID_TR_NUMBER_ID;

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
