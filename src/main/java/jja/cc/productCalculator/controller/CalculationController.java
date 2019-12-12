package jja.cc.productCalculator.controller;

import jja.cc.productCalculator.dao.CustomerRepository;
import jja.cc.productCalculator.dao.ProductRepository;
import jja.cc.productCalculator.dto.CalculateRequestDto;
import jja.cc.productCalculator.dto.CalculateResponseDto;
import jja.cc.productCalculator.dto.ProductCountDto;
import jja.cc.productCalculator.model.Customer;
import jja.cc.productCalculator.model.Product;
import jja.cc.productCalculator.service.DiscountCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CalculationController {

    @Autowired
    DiscountCalculatorService discountCalculatorService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping("price")
    ResponseEntity<CalculateResponseDto> calculate(@RequestBody CalculateRequestDto requestDto) {
        Optional<Customer> customerOptional = customerRepository.findById(requestDto.getCustomerId());
        if (customerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Map<Product, Double> products = new HashMap<>();
        for (ProductCountDto productCountDto : requestDto.getProducts()) {
            Optional<Product> productOptional = productRepository.findById(productCountDto.getProductId());
            if (productOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            products.put(productOptional.get(), productCountDto.getCount());
        }
        Double result = discountCalculatorService.calculate(customerOptional.get(), products);
        CalculateResponseDto responseDto = new CalculateResponseDto();
        responseDto.setPrice(result);
        return ResponseEntity.ok(responseDto);
    }
}
