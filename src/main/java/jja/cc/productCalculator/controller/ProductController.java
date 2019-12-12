package jja.cc.productCalculator.controller;

import jja.cc.productCalculator.dao.ProductRepository;
import jja.cc.productCalculator.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    List<Product> all() {
        return productRepository.findAll();
    }

    @PostMapping("/products")
    Product post(@RequestBody Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    @GetMapping("/products/{id}")
    ResponseEntity<Product> get(@PathVariable Long id) {
        return ResponseEntity.of(productRepository.findById(id));
    }

    @PutMapping("/products/{id}")
    ResponseEntity<Product> put(@RequestBody Product newProduct, @PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setPrice(newProduct.getPrice());
            product.setName(newProduct.getName());
            productRepository.save(product);
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/products/{id}")
    void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
