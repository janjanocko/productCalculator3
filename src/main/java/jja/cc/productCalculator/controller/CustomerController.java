package jja.cc.productCalculator.controller;

import jja.cc.productCalculator.dao.CustomerRepository;
import jja.cc.productCalculator.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/customers")
    List<Customer> all() {
        return customerRepository.findAll();
    }

    @PostMapping("/customers")
    Customer post(@RequestBody Customer customer) {
        customer.setId(null);
        return customerRepository.save(customer);
    }

    @GetMapping("/customers/{id}")
    ResponseEntity<Customer> get(@PathVariable Long id) {
        return ResponseEntity.of(customerRepository.findById(id));
    }

    @PutMapping("/customers/{id}")
    ResponseEntity<Customer> put(@RequestBody Customer newCustomer, @PathVariable Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setDiscounts(newCustomer.getDiscounts());
            customer.setName(newCustomer.getName());
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/customers/{id}")
    void delete(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }
}
