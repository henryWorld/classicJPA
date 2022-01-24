package com.specsavers.socrates.clinical.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.specsavers.socrates.clinical.model.ContactLensAssessmentDto;
import com.specsavers.socrates.clinical.model.TearAssessmentInputDto;
import com.specsavers.socrates.clinical.service.ContactLensAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

import static com.specsavers.socrates.clinical.util.CommonStaticValues.*;
import static com.specsavers.socrates.common.util.GraphQLUtils.CORRELATION_ID_HEADER_NAME;
import static graphql.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ContactLensResolverTest {
    private static final int TR_NUMBER = 24;

    @MockBean
    ContactLensAssessmentService contactLensAssessmentService;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    private ContactLensAssessmentDto contactLensAssessmentDto;
    private ObjectNode parameter;

    @BeforeEach
    void setUp() {
        parameter = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode();
        parameter.put("trNumber", TR_NUMBER);

        this.graphQLTestTemplate
                .withClearHeaders()
                .withAdditionalHeader(CORRELATION_ID_HEADER_NAME, UUID.randomUUID().toString())
                .withAdditionalHeader(STORE_ID_HTTP_HEADER_NAME, VALID_STORE_ID);

        contactLensAssessmentDto = CONTACT_LENS_ASSESSMENT_DTO
                .build();

        lenient().when(contactLensAssessmentService.save(any())).thenReturn(contactLensAssessmentDto);
        lenient().when(contactLensAssessmentService.getContactLensAssessment(any())).thenReturn(contactLensAssessmentDto);
        lenient().when(contactLensAssessmentService.update(any(), anyLong(), any(TearAssessmentInputDto.class)))
                .thenReturn(contactLensAssessmentDto);
    }

    @Test
    void testPersistingContactLensAssessment() throws IOException {
        final var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        assertTrue(response.isOk());

        response.assertThatNoErrorsArePresent()
                .assertThatField("$.data.createContactLensAssessment.id").as(UUID.class)
                .and().assertThatField("$.data.createContactLensAssessment.version").asInteger()
                .and().assertThatField("$.data.createContactLensAssessment.creationDate").as(OffsetDateTime.class);

        verify(contactLensAssessmentService).save(any());
    }


    @Test
    void testGetContactLensAssessmentGivenId() throws IOException {

        final var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);
        assertTrue(response.isOk());

        final var persistedClDto = response.get("$.data.createContactLensAssessment",
                ContactLensAssessmentDto.class);

        final var uuid = response.get("$.data.createContactLensAssessment.id");

        final var variables = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .createObjectNode();

        variables.put("id", uuid);

        final var clDtoResponse = graphQLTestTemplate
                .perform(GET_CL_ASSESSMENT, variables);

        assertTrue(clDtoResponse.isOk());

        final var retrievedClDto = clDtoResponse.get("$.data.contactLensAssessment",
                ContactLensAssessmentDto.class);

        assertEquals(persistedClDto, retrievedClDto);

        verify(contactLensAssessmentService).getContactLensAssessment(any());
    }


    @Test
    void testPersistTearAssessment() throws IOException {
        final var response = graphQLTestTemplate
                .perform(CREATE_CL_ASSESSMENT, parameter);

        assertTrue(response.isOk());

        final var persistedClDto = response.get("$.data.createContactLensAssessment",
                ContactLensAssessmentDto.class);

        final var uuid = response.get("$.data.createContactLensAssessment.id");

        final var variables = new ObjectMapper().createObjectNode()
                .put("contactLensId", uuid)
                .put("version", 20L);

        final var clDtoResponse = graphQLTestTemplate.perform(UPDATE_TEAR_ASSESSMENT, variables);

        clDtoResponse.assertThatNoErrorsArePresent()
                .assertThatField("$.data.updateTearAssessment.version")
                .as(Long.class)
                .isEqualTo(persistedClDto.getVersion());

        verify(contactLensAssessmentService).update(any(), anyLong(), any(TearAssessmentInputDto.class));
    }
}