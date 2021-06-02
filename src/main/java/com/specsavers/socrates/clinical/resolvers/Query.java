package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.repository.PrescribedRxRepository;
import com.specsavers.socrates.clinical.types.PrescribedRX;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
public class Query implements GraphQLQueryResolver {
    
    @Autowired
    private PrescribedRxRepository prescribedRxRepository;
    
    // TODO: Create code to handle testRoomNumber and errors
    public PrescribedRX prescribedRX(String id, Integer testRoomNumber) {     
        if(id != null && !id.trim().isEmpty())   
            return prescribedRxRepository.findById(id).get();
        
        //TODO: Create proper error handler
        return null;
    }
}
