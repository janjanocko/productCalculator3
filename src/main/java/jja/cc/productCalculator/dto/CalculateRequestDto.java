package jja.cc.productCalculator.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class CalculateRequestDto {
    @NotNull
    Long customerId;
    @NotEmpty
    List<ProductCountDto> products = new ArrayList<>();
}
