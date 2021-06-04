package com.specsavers.socrates.clinical.resolvers;

import java.time.OffsetDateTime;

import com.specsavers.socrates.clinical.types.PrescribedRX;

import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLResolver;

@Component
public class PrescribedRXResolver implements GraphQLResolver<PrescribedRX> {
    
    public String getDispenseNotes(PrescribedRX prescribedRX) {
        return prescribedRX.getSightTest().getDispenseNotes();
    }

    // TODO: Check why graphql exception handler is not working
    // When a excption occurs in a field, only this field should return null
    public String getClinicianName(PrescribedRX prescribedRX) {
        var staffName = prescribedRX.getSightTest().getStaff().getName();
        return staffName.getFirstName() + " " + staffName.getLastName();
    }

    public Integer getTestRoomNumber(PrescribedRX prescribedRX) {
        return prescribedRX.getSightTest().getTrNumber();
    }

    public OffsetDateTime getTestDate(PrescribedRX prescribedRX) {
        return prescribedRX.getSightTest().getRecord().getCustomerArrivalTime();
    }
}
