package com.specsavers.socrates.clinical.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.RefractedRxDto;
import com.specsavers.socrates.clinical.model.TearAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentEyeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.*;
import static com.specsavers.socrates.common.util.GraphQLUtils.CORRELATION_ID_HEADER_NAME;
import static graphql.Assert.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ContactLensResolverTest {
    private static final int TR_NUMBER = 24;
    private static final long INVALID_VERSION = 2L;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;


    private ObjectNode parameter;

    @BeforeEach
    void setUp() {
        //given
        parameter = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode();
        parameter.put("trNumber", TR_NUMBER);

        this.graphQLTestTemplate
                .withClearHeaders()
                .withAdditionalHeader(CORRELATION_ID_HEADER_NAME, UUID.randomUUID().toString())
                .withAdditionalHeader(STORE_ID_HTTP_HEADER_NAME, VALID_STORE_ID);
    }

    @Test
    void testPersistingContactLensAssessment() throws IOException {
        //when
        var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        //then
        assertTrue(response.isOk());

        response.assertThatNoErrorsArePresent()
                .assertThatField("$.data.createContactLensAssessment.id").as(UUID.class)
                .and().assertThatField("$.data.createContactLensAssessment.version").asInteger()
                .and().assertThatField("$.data.createContactLensAssessment.creationDate").as(OffsetDateTime.class);

    }


    @Test
    void testGetContactLensAssessmentGivenId() throws IOException {
        //when
        var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);
        assertTrue(response.isOk());

        var uuid = response.get("$.data.createContactLensAssessment.id");

        var variables = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode();

        variables.put("id", uuid);

        var clDtoResponse = graphQLTestTemplate
                .perform(GET_CL_ASSESSMENT, variables);

        //then
        assertTrue(clDtoResponse.isOk());

        clDtoResponse.assertThatNoErrorsArePresent()
                .assertThatField("$.data.contactLensAssessment.id").asString().isEqualTo(uuid)
                .and().assertThatField("$.data.contactLensAssessment.version").asInteger()
                .and().assertThatField("$.data.contactLensAssessment.creationDate").as(OffsetDateTime.class);

    }


    @Test
    void testGetContactLensAssessmentGivenInvalidId() throws IOException {
        //given
        String uuid = UUID.randomUUID().toString();
        var variables = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode()
                .put("id", uuid);

        //when
        var response = graphQLTestTemplate.perform(GET_CL_ASSESSMENT, variables);

        //then
        response
                .assertThatField("$.errors[*].message")
                .asListOf(String.class)
                .contains("Exception while fetching data (/contactLensAssessment) : Object " + uuid + " not found");
    }


    @Test
    void testPersistTearAssessmentGivenValidVersion() throws IOException {
        //when
        var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        assertTrue(response.isOk());

        var persistedClDto = response.get("$.data.createContactLensAssessment",
                ContactLensAssessmentDto.class);

        var uuid = response.get("$.data.createContactLensAssessment.id");

        var variables = new ObjectMapper().createObjectNode()
                .put("contactLensId", uuid)
                .put("version", VALID_VERSION);

        var clDtoResponse = graphQLTestTemplate.perform(UPDATE_TEAR_ASSESSMENT, variables);

        //then
        clDtoResponse.assertThatNoErrorsArePresent()
                .assertThatField("$.data.updateTearAssessment.version")
                .as(Long.class)
                .isEqualTo(persistedClDto.getVersion() + 1);
    }


    @DisplayName("Validate left Eye tbut")
    @Test
    void testUpdateInvalidTearAssessmentGivenInvalidFieldValue() throws IOException {
        //given
        var variables = variablesForTearAssessmentUpdate();
        variables.put("leftTbut", " ");

        //when
        var updatedResponse = graphQLTestTemplate
                .perform(UPDATE_TEAR_ASSESSMENT_VALIDATION, variables);

        //then
        updatedResponse
                .assertThatField("$.errors[*].message")
                .asListOf(String.class)
                .contains("Left eye: tbut must not be blank");
    }


    @Test
    void testGetTearAssessmentGivenValidId() throws IOException {
        //given
        var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        assertTrue(response.isOk());

        var persistedClDto = response
                .get("$.data.createContactLensAssessment",
                        ContactLensAssessmentDto.class);

        var updateTearAssessmentVariables = new ObjectMapper().createObjectNode()
                .put("contactLensId", persistedClDto.getId().toString())
                .put("version", persistedClDto.getVersion())
                .put("leftTbut", "BM");

        var updatedResponse = graphQLTestTemplate
                .perform(UPDATE_TEAR_ASSESSMENT_VALIDATION, updateTearAssessmentVariables);

        var variables = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode()
                .put("id", persistedClDto.getId().toString());


        //when
        var tearAssessmentDtoResponse = graphQLTestTemplate
                .perform(GET_TEAR_ASSESSMENT, variables);

        //then
        tearAssessmentDtoResponse
        .assertThatNoErrorsArePresent()
                .assertThatField("$.data.tearAssessment")
                .as(TearAssessmentDto.class)
                .isEqualTo(getTearAssessmentTestData());

        assertTrue(tearAssessmentDtoResponse.isOk());
    }


    private ObjectNode variablesForTearAssessmentUpdate() throws IOException {
        var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        assertTrue(response.isOk());

        var persistedClDto = response
                .get("$.data.createContactLensAssessment",
                        ContactLensAssessmentDto.class);

        return new ObjectMapper().createObjectNode()
                .put("contactLensId", persistedClDto.getId().toString())
                .put("version", VALID_VERSION)
                .put("leftTbut", "OK");
    }

    private TearAssessmentDto getTearAssessmentTestData() {
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