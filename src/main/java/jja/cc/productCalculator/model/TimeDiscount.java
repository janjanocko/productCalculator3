package jja.cc.productCalculator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TimeDiscount extends Discount {
    @NotNull
    Instant fromDate;
    Instant toDate;
    @NotNull
    Double discount;
}
