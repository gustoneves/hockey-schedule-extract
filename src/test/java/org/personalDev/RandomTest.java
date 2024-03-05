package org.personalDev;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RandomTest {

    @Test
    public void randomTest() {

        LocalDate billingStartDate = LocalDate.parse("2020-07-31");

        long monthsBetween = ChronoUnit.MONTHS.between(billingStartDate, LocalDate.now());


        System.out.println("The date plus "+monthsBetween+" is "+ billingStartDate.plusMonths(monthsBetween-2));

    }
}
