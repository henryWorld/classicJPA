package com.specsavers.socrates.clinical.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.regex.Pattern;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RequestLoggerTest {

    private static final String KNOWN_REQUEST = "graphql/query/known_request.graphql";
    private static final String UNKNOWN_REQUEST = "graphql/query/unknown_request.graphql";

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @BeforeEach
    public void setup() {
        // Clear any headers from previous tests
        this.graphQLTestTemplate.withClearHeaders();
    }

    @Test
    void testKnownRequest() throws IOException {
        var listAppender = getListAppender();
        final ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("trNumber", 123);

        this.graphQLTestTemplate.perform(KNOWN_REQUEST, variables);

        var logList = listAppender.list;
        Assertions.assertEquals(2, logList.size());
        assertLogLine(logList.get(0), "operation prescribedRX");
        assertCompletedLog(logList.get(1));
    }

    @Test
    void testKnownRequestWithHeaders() throws IOException {
        var listAppender = getListAppender();
        final ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("trNumber", 123);

        this.graphQLTestTemplate
            .withAdditionalHeader("SOCRATES_HEADER", "header1")
            .withAdditionalHeader("SOCRATES_HEADER2", "header2")
            .perform(KNOWN_REQUEST, variables);

        var logList = listAppender.list;
        Assertions.assertEquals(2, logList.size());
        assertLogLine(logList.get(0), "operation prescribedRX, " +
                "headers socrates_header2=header2, socrates_header=header1");
        assertCompletedLog(logList.get(1));
    }

    @Test
    void testKnownRequestWithInvalidQueryInput() throws IOException {
        var listAppender = getListAppender();
        final ObjectNode variables = new ObjectMapper().createObjectNode();

        this.graphQLTestTemplate.perform(KNOWN_REQUEST, variables);

        var logList = listAppender.list;
        Assertions.assertEquals(2, logList.size());
        assertLogLine(logList.get(0), "operation prescribedRX");
        assertLogLine(logList.get(1), "Error executing: Exception while fetching data " +
            "(/prescribedRX) : Provide a valid id OR testRoomNumber");
    }

    @Test
    void testKnownRequestWithUnknownQuery() throws IOException {
        var listAppender = getListAppender();
        final ObjectNode variables = new ObjectMapper().createObjectNode();

        this.graphQLTestTemplate.perform(UNKNOWN_REQUEST, variables);

        var logList = listAppender.list;
        Assertions.assertEquals(1, logList.size());
        assertLogLine(logList.get(0), "Error executing: Validation error of type FieldUndefined: " +
            "Field 'unknownQuery' in type 'Query' is undefined @ 'unknownQuery'");
    }

    @Test
    void testKnownRequestWithUnknownTestRoom() throws IOException {
        var listAppender = getListAppender();
        final ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("trNumber", 999);

        this.graphQLTestTemplate.perform(KNOWN_REQUEST, variables);

        var logList = listAppender.list;
        Assertions.assertEquals(2, logList.size());
        assertLogLine(logList.get(0), "operation prescribedRX");
        assertLogLine(logList.get(1), "Error executing: Exception while fetching data " +
            "(/prescribedRX) : Object not found");
    }

    private ListAppender<ILoggingEvent> getListAppender() {
        var logger = (Logger) LoggerFactory.getLogger(RequestLogger.class);
        var listAppender = new ListAppender<ILoggingEvent>();
        listAppender.start();
        logger.addAppender(listAppender);

        return listAppender;
    }

    private void assertCompletedLog(ILoggingEvent event) {
        assertLogLine(event, "completed successfully");
    }

    private void assertLogLine(ILoggingEvent event, String expectedMessage) {
        var logMessage = getAfterExecutionId(event.getFormattedMessage());
        Assertions.assertEquals(expectedMessage, logMessage);
    }

    private String getAfterExecutionId(String logLine) {
        var executionPattern = "GraphQL execution .{8}-.{4}-.{4}-.{4}-.{12}: (.*)";
        var regex = Pattern.compile(executionPattern);
        var matched = regex.matcher(logLine);

        // Assert that execution id section exists
        Assertions.assertTrue(matched.find());

        return matched.group(1);
    }
}
