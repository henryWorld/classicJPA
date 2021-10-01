package com.specsavers.socrates.clinical.resolvers;

import java.util.List;
import java.util.stream.Collectors;

import com.specsavers.socrates.clinical.exception.NotFoundException;
import com.specsavers.socrates.clinical.legacy.repository.PrescribedRxRepository;

import com.specsavers.socrates.clinical.mapper.PrescribedRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.clinical.util.MockSightTest;

import graphql.GraphqlErrorException;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Query implements GraphQLQueryResolver {
    
    private final PrescribedRxRepository prescribedRxRepository;
    private final SightTestRepository sightTestRepository;
    private final PrescribedRxMapper mapper;
    private final SightTestMapper sightTestMapper;

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

    public List<SightTestDto> sightTests(Integer customerId) {
        if (customerId == 0) {
            throw new GraphqlErrorException.Builder()
                .message("Provide a valid customerId").build();
        }

        var sightTests = sightTestRepository.findByCustomerIdOrderByCreationDateDesc(customerId);
        
        // Mock data used while task SC-287 not done
        sightTests.add(MockSightTest.makeMockSightTest1());
        sightTests.add(MockSightTest.makeMockSightTest2());
        sightTests.add(MockSightTest.makeMockSightTest3());

        return sightTests.stream()
            .map(sightTestMapper::map)
            .collect(Collectors.toList());
    }
}
