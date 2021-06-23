package com.specsavers.socrates.clinical.exception;

import java.util.List;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.language.SourceLocation;

public class NotFoundException extends GraphQLException implements GraphQLError {

    public NotFoundException() {
        super("Object not found");
    }
    @Override
    public ErrorClassification getErrorType() {
        return ErrorType.DataFetchingException;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return List.of();
    }
    
}
