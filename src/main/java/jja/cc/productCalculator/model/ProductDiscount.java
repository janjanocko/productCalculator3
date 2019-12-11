package jja.cc.productCalculator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDiscount extends Discount {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    @NotNull
    Double discount;
}
