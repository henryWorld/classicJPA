package com.specsavers.socrates.clinical.model.validation.common;

import com.specsavers.socrates.clinical.model.DrugInfoDto;
import com.specsavers.socrates.common.exception.ValidationException;
import com.specsavers.socrates.common.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@Component
@AllArgsConstructor
@NoArgsConstructor
@ConditionalOnProperty(name = "region", havingValue = "common")
public class DrugInfoValidator extends Validator<DrugInfoDto> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");
    private Clock clock = Clock.systemUTC();

    @Override
    public void validate(DrugInfoDto item) {
        checkTime(item.getTime());

        var drugCheck = check("DrugUsed", item.getDrugUsed());
        drugCheck.notBlank();
        drugCheck.maxLength(25);

        var batchCheck = check("batchNo", item.getBatchNo());
        batchCheck.notBlank();
        batchCheck.maxLength(25);

        checkExpiryDate(item.getExpiryDate());
        checkMandatoryExpiryDate(item);
    }

    private void checkMandatoryExpiryDate(DrugInfoDto info) {
        if (info.getExpiryDate() == null &&  (info.getDrugUsed() != null || info.getTime() != null || info.getBatchNo() != null)){
                throw new ValidationException("ExpiryDate is mandatory when any other field has value");
        }
    }

    private void checkTime(String time) {
        if (time != null) {
            try {
                TIME_FORMATTER.parse(time);
            } catch (DateTimeParseException e) {
                throw new ValidationException("Time is not in correct format 'HH:mm'");
            }
        }
    }

    private void checkExpiryDate(String dateString) {
        if (dateString != null) {
            try {

                var expiryDate = YearMonth.parse(dateString, DATE_FORMATTER);
                var currentDate = YearMonth.now(clock);
                if (expiryDate.isBefore(currentDate)) {
                    throw new ValidationException("ExpiryDate must be equal or greater than current date.");
                }

            } catch (DateTimeParseException e) {
                throw new ValidationException("ExpiryDate is not in correct format 'MM/YYYY'");
            }
        }
    }
}
