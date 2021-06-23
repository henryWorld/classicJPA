package com.specsavers.socrates.clinical.resolvers;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import com.specsavers.socrates.clinical.model.OptionRecommendation;
import com.specsavers.socrates.clinical.model.rx.RX;

import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLResolver;

@Component
public class PrescribedRXResolver implements GraphQLResolver<RX> {

    public String getDispenseNotes(RX rx) {
        return rx.getPrescribedRX().getSightTest().getDispenseNotes();
    }

    public String getRecommendations(RX rx) {
        return rx.getPrescribedRX().getSightTest().getOptionRecommendations().stream()
            .map(OptionRecommendation::getText)
            .collect(Collectors.joining(", "));
    }

    public String getClinicianName(RX rx) {
        var staffName = rx.getPrescribedRX().getSightTest().getStaff().getName();
        return staffName.getFirstName() + " " + staffName.getLastName();
    }

    public Integer getTestRoomNumber(RX rx) {
        return rx.getPrescribedRX().getSightTest().getTrNumber();
    }

    public OffsetDateTime getTestDate(RX rx) {
        return rx.getPrescribedRX().getSightTest().getRecord().getCustomerArrivalTime();
    }

    public Integer getRecallPeriod(RX rx) {
        return rx.getPrescribedRX().getRecallPeriod();
    }
}
