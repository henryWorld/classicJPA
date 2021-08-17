package com.specsavers.socrates.clinical.resolvers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import com.specsavers.socrates.clinical.model.OptionRecommendation;
import com.specsavers.socrates.clinical.model.rx.RX;

import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLResolver;

@Component
public class PrescribedRXResolver implements GraphQLResolver<RX> {

    public int getId(RX rx){
        return rx.getPrescribedRX().getId();
    }

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
        var dbDate = rx.getPrescribedRX().getSightTest().getRecord().getCustomerArrivalTime();
        return OffsetDateTime.of(dbDate, ZoneOffset.UTC);
    }

    public Integer getRecallPeriod(RX rx) {
        return rx.getPrescribedRX().getRecallPeriod();
    }
}
