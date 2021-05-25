package com.specsavers.socrates.clinical.provider;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.specsavers.socrates.clinical.reader.ResourceReader;

import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;

@Configuration
public class GraphQLProvider {

	//private final GraphQLDataFetchersProvider graphQLDataFetchersProvider;
	private final ResourceReader resourceReader;

	private GraphQL graphQL;

	@Autowired
	public GraphQLProvider(/*GraphQLDataFetchersProvider graphQLDataFetchersProvider,*/ ResourceReader resourceReader) {
		super();

		//this.graphQLDataFetchersProvider = graphQLDataFetchersProvider;
		this.resourceReader = resourceReader;
	}

	@Bean
	public GraphQL getGraphQL() {
		return this.graphQL;
	}

	@PostConstruct
	public void init() throws IOException {
		String sdl = this.resourceReader.asString("classpath:graphql/schema.graphqls", StandardCharsets.UTF_8);
		GraphQLSchema graphQLSchema = this.buildSchema(sdl);
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	private GraphQLSchema buildSchema(String sdl) {
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = this.buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();

		return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
	}

	private RuntimeWiring buildWiring() {
		return RuntimeWiring.newRuntimeWiring()
				.scalar(ExtendedScalars.DateTime)
				//.type(this.buildQuery())
				//.type(this.buildTypes())
				.build();
	}

	// private TypeRuntimeWiring.Builder buildQuery() {
	// 	return newTypeWiring("Query")
	// 			.dataFetcher("customers", this.graphQLDataFetchersProvider.customersDataFetcher())
	// 			.dataFetcher("customer", this.graphQLDataFetchersProvider.customerDataFetcher());
	// }

	// private TypeRuntimeWiring.Builder buildTypes() {
	// 	return newTypeWiring("Customer")
	// 			.dataFetcher("gender", this.graphQLDataFetchersProvider.getCustomerGenderDataFetcher())
	// 			.dataFetcher("addresses", this.graphQLDataFetchersProvider.getCustomerAddressesDataFetcher())
	// 			.dataFetcher("contactDetails", this.graphQLDataFetchersProvider.getCustomerContactDetailsDataFetcher())
	// 			.dataFetcher("sightTests", this.graphQLDataFetchersProvider.getCustomerSightTestsDataFetcher());
	// }

}
