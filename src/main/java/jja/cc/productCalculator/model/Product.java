package jja.cc.productCalculator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@ToString(exclude = "discounts")
@EqualsAndHashCode(exclude = "discounts")
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String productId;
    @NotBlank
    private String name;
    @NotNull
    private Double price;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProductDiscount> discounts = new LinkedHashSet<>();
}
