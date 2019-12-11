package jja.cc.productCalculator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class VolumeDiscount extends Discount {
    @NotNull
    Double minAmount;
    Double maxAmount;
    @NotNull
    Double discount;
}
