package com.specsavers.socrates.clinical.resolvers;

import com.specsavers.socrates.clinical.legacy.repository.PrescribedRxRepository;
import com.specsavers.socrates.clinical.mapper.PrescribedRxMapper;
import com.specsavers.socrates.clinical.mapper.SightTestMapper;
import com.specsavers.socrates.clinical.model.type.PrescribedRxDto;
import com.specsavers.socrates.clinical.model.type.SightTestDto;
import com.specsavers.socrates.clinical.repository.SightTestRepository;
import com.specsavers.socrates.clinical.util.MockSightTest;
import com.specsavers.socrates.common.exception.NotFoundException;
import com.specsavers.socrates.common.exception.UnexpectedSocratesException;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class QueryResolver implements GraphQLQueryResolver {

    private final PrescribedRxRepository prescribedRxRepository;
    private final SightTestRepository sightTestRepository;
    private final PrescribedRxMapper mapper;
    private final SightTestMapper sightTestMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public PrescribedRxDto prescribedRX(int id, int testRoomNumber) {
        var hasId = id > 0;
        var hasTrNumber = testRoomNumber > 0;

        if (hasId && hasTrNumber) {
            throw new UnexpectedSocratesException("Cannot search by id and testRoomNumber at same time");
        }

        if (hasId) {
            return mapper.fromEntity(prescribedRxRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(id)));
        }

        if (hasTrNumber) {
            return mapper.fromEntity(prescribedRxRepository.findByTestRoomNumber(testRoomNumber)
                    .orElseThrow(() -> new NotFoundException(testRoomNumber)));
        }

        throw new UnexpectedSocratesException("Provide a valid id OR testRoomNumber");
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public SightTestDto sightTest(UUID id) {
        return sightTestRepository.findById(id)
                .map(sightTestMapper::map)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<SightTestDto> sightTests(Integer customerId) {
        if (customerId == 0) {
            throw new UnexpectedSocratesException("Provide a valid customerId");
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
