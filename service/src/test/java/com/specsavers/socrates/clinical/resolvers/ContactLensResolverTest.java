package com.specsavers.socrates.clinical.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import com.specsavers.socrates.clinical.util.TestHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.CREATE_CL_ASSESSMENT;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.GET_CL_ASSESSMENT;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.GET_TEAR_ASSESSMENT;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.INVALID_VERSION;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.STORE_ID_HTTP_HEADER_NAME;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_TEAR_ASSESSMENT;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.UPDATE_TEAR_ASSESSMENT_VALIDATION;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_STORE_ID;
import static com.specsavers.socrates.clinical.util.CommonStaticValues.VALID_VERSION;
import static com.specsavers.socrates.common.util.GraphQLUtils.CORRELATION_ID_HEADER_NAME;
import static graphql.Assert.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ContactLensResolverTest {
    private static final int TR_NUMBER = 24;
    private UUID assessmentId;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;


    @BeforeEach
    void setUp() {

        this.graphQLTestTemplate
                .withClearHeaders()
                .withAdditionalHeader(CORRELATION_ID_HEADER_NAME, UUID.randomUUID().toString())
                .withAdditionalHeader(STORE_ID_HTTP_HEADER_NAME, VALID_STORE_ID);
    }

    @DisplayName("test contact lens is created")
    @Test
    void testPersistingContactLensAssessment() throws IOException {
        //when
        var response = createContactLensAssessment();

        //then
        assertTrue(response.isOk());

        response
                .assertThatField("$.data.createContactLensAssessment.id").as(UUID.class)
                .and().assertThatField("$.data.createContactLensAssessment.version").asInteger()
                .and().assertThatField("$.data.createContactLensAssessment.creationDate").as(LocalDate.class);
    }

    @DisplayName("test loading contact lens with valid id")
    @Test
    void testGetContactLensAssessmentGivenId() throws IOException {
        //when
        var clDtoResponse = loadContactLensAssessmentResponse();

        //then
        assertTrue(clDtoResponse.isOk());
        clDtoResponse
                .assertThatField("$.data.contactLensAssessment.id").as(UUID.class)
                .and().assertThatField("$.data.contactLensAssessment.version").asInteger()
                .and().assertThatField("$.data.contactLensAssessment.creationDate").as(LocalDate.class);
    }

    @DisplayName("test loading contact lens with an invalid id")
    @Test
    void testGetContactLensAssessmentGivenInvalidId() throws IOException {
        //given
        String uuid = UUID.randomUUID().toString();

        var variables = getRequestParameter("id", uuid);

        //when
        var response = graphQLTestTemplate.perform(GET_CL_ASSESSMENT, variables);

        //then
        response
                .assertThatField("$.errors[*].message")
                .asListOf(String.class)
                .contains("Exception while fetching data (/contactLensAssessment) : Object " + uuid + " not found");
    }


    @DisplayName("test creating tear assessment with a valid version")
    @Test
    void testPersistTearAssessmentGivenValidVersion() throws IOException {
        //given
        var variables = getTearAssessmentRequestParameters(VALID_VERSION);

        //when
        var clDtoResponse = graphQLTestTemplate.perform(UPDATE_TEAR_ASSESSMENT, variables);

        //then
        clDtoResponse.assertThatNoErrorsArePresent()
                .assertThatField("$.data.updateTearAssessment.version")
                .as(Long.class)
                .isEqualTo(VALID_VERSION + 1);
    }

    @DisplayName("test creating tear assessment with an invalid version")
    @Test
    void testPersistTearAssessmentGivenInvalidVersion() throws IOException {

        //given
        var variables = getTearAssessmentRequestParameters(INVALID_VERSION);

        //when
        var clDtoResponse = graphQLTestTemplate.perform(UPDATE_TEAR_ASSESSMENT, variables);

        //then
        clDtoResponse
                .assertThatField("$.errors[*].message")
                .asListOf(String.class)
                .contains("Exception while fetching data (/updateTearAssessment) : " +
                        "ContactLensAssessment with id " + assessmentId +
                        " is currently at version " + VALID_VERSION + ", but the expected version is " +
                        INVALID_VERSION);
    }


    @DisplayName("Validate left Eye tbut")
    @Test
    void testUpdateInvalidTearAssessmentGivenInvalidFieldValue() throws IOException {
        //given
        var variables = getTearAssessmentRequestParameters(VALID_VERSION)
                .put("leftTbut", " ");

        //when
        var updatedResponse = graphQLTestTemplate
                .perform(UPDATE_TEAR_ASSESSMENT_VALIDATION, variables);

        //then
        updatedResponse
                .assertThatField("$.errors[*].message")
                .asListOf(String.class)
                .contains("Left eye: tbut must not be blank");
    }

    @DisplayName("Validate left Eye tbut value length")
    @Test
    void testUpdateInvalidTearAssessmentGivenInvalidFieldLength() throws IOException {
        //given
        var invalidLeftEyeFieldLength = TestHelpers.stringOfLength(31);
        var variables = getTearAssessmentRequestParameters(VALID_VERSION)
                .put("leftTbut", "OK")
                .put("leftTbut", invalidLeftEyeFieldLength);

        //when
        var updatedResponse = graphQLTestTemplate
                .perform(UPDATE_TEAR_ASSESSMENT_VALIDATION, variables);

        //then
        updatedResponse
                .assertThatField("$.errors[*].message")
                .asListOf(String.class)
                .contains("Left eye: tbut must not be longer than 30 characters");
    }


    @DisplayName("test loading tear assessment")
    @Test
    void testGetTearAssessment() throws IOException {
        //given
        prepareTearAssessment();

        var variables = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode()
                .put("id", assessmentId.toString());


        //when
        var tearAssessmentDtoResponse = graphQLTestTemplate
                .perform(GET_TEAR_ASSESSMENT, variables);

        //then
        tearAssessmentDtoResponse
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.contactLensAssessment.tearAssessment")
                .as(TearAssessmentDto.class)
                .isEqualTo(expectedTearAssessment());
    }


    private ObjectNode getRequestParameter(String field, int value) {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode().put(field, value);
    }

    private ObjectNode getRequestParameter(String field, String value) {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode().put(field, value);
    }

    private GraphQLResponse createContactLensAssessment() throws IOException {
        var parameter = getRequestParameter("trNumber", TR_NUMBER);
        var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        response.assertThatNoErrorsArePresent();
        return response;
    }

    private GraphQLResponse loadContactLensAssessmentResponse() throws IOException {
        var response = createContactLensAssessment();

        assessmentId = response.get("$.data.createContactLensAssessment.id", UUID.class);

        var variables = getRequestParameter("id", assessmentId.toString());

        var clDtoResponse = graphQLTestTemplate
                .perform(GET_CL_ASSESSMENT, variables);

        clDtoResponse.assertThatNoErrorsArePresent();

        return clDtoResponse;
    }

    private void prepareTearAssessment() throws IOException {
        var updateTearAssessmentVariables = getTearAssessmentRequestParameters(VALID_VERSION)
                .put("leftTbut", "BM");

        graphQLTestTemplate
                .perform(UPDATE_TEAR_ASSESSMENT_VALIDATION, updateTearAssessmentVariables);
    }

    private ObjectNode getTearAssessmentRequestParameters(long version) throws IOException {
        var response = createContactLensAssessment();

        assessmentId = response.get("$.data.createContactLensAssessment.id", UUID.class);

        return new ObjectMapper().createObjectNode()
                .put("contactLensId", assessmentId.toString())
                .put("version", version);
    }

    private TearAssessmentDto expectedTearAssessment() {
        return TearAssessmentDto.builder()
                .rightEye(TearAssessmentEyeDto.builder()
                        .tbut("strings")
                        .prism("prism")
                        .scope("strings")
                        .schirmer("strings")
                        .build())
                .leftEye(TearAssessmentEyeDto.builder()
                        .tbut("BM")
                        .prism("strings")
                        .scope("strings")
                        .schirmer("strings")
                        .build())
                .observations("Strings")
                .build();
    }

}