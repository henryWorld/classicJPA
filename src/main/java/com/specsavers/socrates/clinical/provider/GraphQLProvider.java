package com.specsavers.socrates.clinical.provider;

import java.io.IOException;

import javax.annotation.PostConstruct;

import com.specsavers.socrates.clinical.resolvers.Query;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.kickstart.tools.SchemaParser;

@Configuration
public class GraphQLProvider {

	private GraphQL graphQL;

	@Bean
	public GraphQL getGraphQL() {
		return this.graphQL;
	}

	@PostConstruct
	public void init() throws IOException {
		var graphQLSchema = SchemaParser.newParser()
							.file("graphql/schema.graphqls")
							.resolvers(new Query()/*, new Mutation()*/)
							.scalars(ExtendedScalars.DateTime)
							.build()
							.makeExecutableSchema();

    	this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}
}
