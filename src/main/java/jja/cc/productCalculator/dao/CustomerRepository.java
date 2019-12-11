package jja.cc.productCalculator.dao;

import jja.cc.productCalculator.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
