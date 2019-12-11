package jja.cc.productCalculator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Discount {
    @Id
    @GeneratedValue
    Long id;
    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    Customer customer;
}
