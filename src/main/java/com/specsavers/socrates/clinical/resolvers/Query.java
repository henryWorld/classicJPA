package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.legacy.repository.PrescribedRxRepository;
import com.specsavers.socrates.clinical.mapper.PrescribedRxMapper;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import graphql.GraphqlErrorException;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {
    
    private final PrescribedRxRepository prescribedRxRepository;
    private final PrescribedRxMapper mapper;

    public PrescribedRxDto prescribedRX(int id, int testRoomNumber) {
        var hasId = id > 0;
        var hasTrNumber = testRoomNumber > 0;

        if (hasId && hasTrNumber)
            throw new GraphqlErrorException.Builder()
            .message("Cannot search by id and testRoomNumber at same time").build();
       
        if(hasId)
            return mapper.fromEntity(prescribedRxRepository.findById(id)
            .orElseThrow(NotFoundException::new));

        if(hasTrNumber)
            return mapper.fromEntity(prescribedRxRepository.findByTestRoomNumber(testRoomNumber)
                    .orElseThrow(NotFoundException::new));

        throw new GraphqlErrorException.Builder()
        .message("Provide a valid id OR testRoomNumber").build();
    }
}
