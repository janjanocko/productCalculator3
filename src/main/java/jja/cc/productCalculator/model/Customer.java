package jja.cc.productCalculator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@ToString(exclude = "discounts")
@EqualsAndHashCode(exclude = "discounts")
@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Long registrationNumber;
    private Double maxDiscount;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Discount> discounts = new LinkedHashSet<>();


}
