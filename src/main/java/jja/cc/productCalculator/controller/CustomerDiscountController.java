package jja.cc.productCalculator.controller;

import jja.cc.productCalculator.dao.CustomerRepository;
import jja.cc.productCalculator.dao.DiscountRepository;
import jja.cc.productCalculator.dao.ProductRepository;
import jja.cc.productCalculator.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerDiscountController {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/customers/{customerId}/discounts")
    List<Discount> all(@PathVariable Long customerId) {
        return discountRepository.findAllByCustomerId(customerId);
    }


    @GetMapping("/customers/{customerId}/discounts/{discountId}")
    ResponseEntity<? extends Discount> get(@PathVariable Long customerId, @PathVariable Long discountId) {
        return ResponseEntity.of(discountRepository.findById(discountId));
    }

    @DeleteMapping("/customers/{customerId}/discounts/{discountId}")
    ResponseEntity<Void> deleteDiscount(@PathVariable Long customerId, @PathVariable Long discountId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Discount> discount = discountRepository.findById(discountId);
        if (customer.isEmpty() || discount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        discountRepository.delete(discount.get());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/customers/{customerId}/volumeDiscounts")
    ResponseEntity<VolumeDiscount> addVolumeDiscount(@PathVariable Long customerId,
                                                     @RequestBody VolumeDiscount volumeDiscount) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        volumeDiscount.setCustomer(customer);
        customer.getDiscounts().add(volumeDiscount);
        discountRepository.save(volumeDiscount);
        return ResponseEntity.ok(volumeDiscount);
    }

    @PostMapping("/customers/{customerId}/timeDiscounts")
    ResponseEntity<TimeDiscount> addTimeDiscount(@PathVariable Long customerId,
                                                 @RequestBody TimeDiscount timeDiscount) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        timeDiscount.setCustomer(customer);
        customer.getDiscounts().add(timeDiscount);
        discountRepository.save(timeDiscount);
        return ResponseEntity.ok(timeDiscount);
    }

    @PostMapping("/customers/{customerId}/productDiscounts")
    ResponseEntity<ProductDiscount> addProductDiscount(@PathVariable Long customerId,
                                                       @RequestBody ProductDiscount productDiscount) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Optional<Product> productOptional = productRepository.findById(productDiscount.getProduct().getId());
        if (customerOptional.isEmpty() || productOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Customer customer = customerOptional.get();
        Product product = productOptional.get();
        productDiscount.setCustomer(customer);
        productDiscount.setProduct(product);
        product.getDiscounts().add(productDiscount);
        customer.getDiscounts().add(productDiscount);
        discountRepository.save(productDiscount);
        return ResponseEntity.ok(productDiscount);
    }
}
