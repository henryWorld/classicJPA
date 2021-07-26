package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.model.rx.RX;
import com.specsavers.socrates.clinical.repository.PrescribedRxRepository;
import graphql.GraphqlErrorException;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {
    
    @Autowired
    private PrescribedRxRepository prescribedRxRepository;

    public RX prescribedRX(int id, int testRoomNumber) {
        var hasId = id > 0;
        var hasTrNumber = testRoomNumber > 0;

        if(hasId && hasTrNumber)
            throw new GraphqlErrorException.Builder()
            .message("Cannot search by id and testRoomNumber at same time").build();
       
        if(hasId)   
            return prescribedRxRepository.findById(id)
            .orElseThrow(NotFoundException::new).getRx();

        if(hasTrNumber)
            return prescribedRxRepository.findByTestRoomNumber(testRoomNumber)
            .orElseThrow(NotFoundException::new).getRx();
        
        throw new GraphqlErrorException.Builder()
        .message("Provide a valid id OR testRoomNumber").build();
    }
}
