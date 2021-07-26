package com.specsavers.socrates.clinical.logging;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class RequestLogger extends SimpleInstrumentation {

    private static final String HEADER_PREFIX = "SOCRATES_";

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        var executionId = parameters.getExecutionInput().getExecutionId();
        if (executionResult != null && executionResult.getErrors() != null &&
                !executionResult.getErrors().isEmpty()) {
            executionResult.getErrors().forEach(error ->
                log.error("GraphQL execution {}: Error executing: {}", executionId, error.getMessage())
            );
        } else {
            log.info("GraphQL execution {}: completed successfully", executionId);
        }

        return super.instrumentExecutionResult(executionResult, parameters);
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(InstrumentationExecuteOperationParameters parameters) {
        var executionId = parameters.getExecutionContext().getExecutionId();
        var headers = getHeadersString(parameters);

        if (headers.isEmpty()) {
            log.info("GraphQL execution {}: operation {}",
                executionId, getOperationName(parameters));
        } else {
            log.info("GraphQL execution {}: operation {}, headers {}",
                executionId, getOperationName(parameters), getHeadersString(parameters));
        }

        return super.beginExecuteOperation(parameters);
    }

    private String getHeadersString(InstrumentationExecuteOperationParameters params) {
        var headers = getHeaders(params);

        var headerStrings = headers.entrySet().stream()
            .map(header -> String.format("%s=%s", header.getKey(), header.getValue()))
            .collect(Collectors.toList());

        return StringUtils.join(headerStrings, ", ");
    }

    private Map<String, String> getHeaders(InstrumentationExecuteOperationParameters parameters) {
        var executionInput = parameters.getExecutionContext().getExecutionInput();
        var context = (DefaultGraphQLServletContext) executionInput.getContext();

        var request = context.getHttpServletRequest();
        var headerNames = request.getHeaderNames().asIterator();

        var headers = new HashMap<String, String>();
        headerNames.forEachRemaining(headerName -> {
            if (headerName.toUpperCase().startsWith(HEADER_PREFIX)) {
                headers.put(headerName, request.getHeader(headerName));
            }
        });
        return headers;
    }

    private String getOperationName(InstrumentationExecuteOperationParameters params) {
        return getOperation(params)
            .flatMap(this::getField)
            .map(Field::getName)
            .orElse(null);
    }

    private Optional<OperationDefinition> getOperation(InstrumentationExecuteOperationParameters params) {
        var document = params.getExecutionContext().getDocument();
        var definitions = document.getDefinitionsOfType(OperationDefinition.class);
        return definitions.isEmpty() ? Optional.empty() : Optional.of(definitions.get(0));
    }

    private Optional<Field> getField(OperationDefinition operation) {
        var fields = operation.getSelectionSet().getSelectionsOfType(Field.class);
        return fields.isEmpty() ? Optional.empty() : Optional.of(fields.get(0));
    }
}

