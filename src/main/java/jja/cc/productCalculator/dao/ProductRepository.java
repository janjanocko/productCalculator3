package jja.cc.productCalculator.dao;

import jja.cc.productCalculator.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
