package jja.cc.productCalculator.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductCountDto {
    @NotNull
    Long productId;
    @NotNull
    @Min(1)
    Double count;
}
