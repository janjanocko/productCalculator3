package jja.cc.productCalculator.dao;

import jja.cc.productCalculator.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT d FROM Discount d WHERE d.customer.id = ?1")
    List<Discount> findAllByCustomerId(Long customerId);
}
