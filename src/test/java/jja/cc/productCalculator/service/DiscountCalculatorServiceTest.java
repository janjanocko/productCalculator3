package jja.cc.productCalculator.service;

import jja.cc.productCalculator.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

class DiscountCalculatorServiceTest {

    @Test
    void calculateSimple() {
        Customer customer = new Customer();
        VolumeDiscount volumeDiscount = new VolumeDiscount();
        volumeDiscount.setCustomer(customer);
        volumeDiscount.setMinAmount(50.0);
        volumeDiscount.setMaxAmount(500.0);
        volumeDiscount.setDiscount(10.0);

        TimeDiscount timeDiscount = new TimeDiscount();
        timeDiscount.setCustomer(customer);
        timeDiscount.setDiscount(5.0);
        timeDiscount.setFromDate(Instant.now().minus(1, ChronoUnit.DAYS));
        timeDiscount.setToDate(Instant.now().plus(1, ChronoUnit.DAYS));


        Product product1 = new Product();
        product1.setPrice(100.0);

        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setCustomer(customer);
        productDiscount.setProduct(product1);
        productDiscount.setDiscount(10.0);


        customer.getDiscounts().add(volumeDiscount);
        customer.getDiscounts().add(timeDiscount);
        customer.getDiscounts().add(productDiscount);

        DiscountCalculatorService discountCalculatorService = new DiscountCalculatorService();
        Map<Product, Double> products = new HashMap<>();
        products.put(product1, 2.0);
        Double result = discountCalculatorService.calculate(customer, products);

        //discount 25%, before discounts 200, after 150
        Assertions.assertEquals(150.0, result);
    }

    @Test
    void calculateNoDiscounts() {
        Customer customer = new Customer();
        VolumeDiscount volumeDiscount = new VolumeDiscount();
        volumeDiscount.setCustomer(customer);
        volumeDiscount.setMinAmount(600.0);
        volumeDiscount.setMaxAmount(5000.0);
        volumeDiscount.setDiscount(10.0);

        TimeDiscount timeDiscount = new TimeDiscount();
        timeDiscount.setCustomer(customer);
        timeDiscount.setDiscount(5.0);
        timeDiscount.setFromDate(Instant.now().minus(100, ChronoUnit.DAYS));
        timeDiscount.setToDate(Instant.now().minus(10, ChronoUnit.DAYS));


        Product productWithNoDiscount = new Product();
        productWithNoDiscount.setPrice(100.0);

        Product productWithDiscount = new Product();
        productWithDiscount.setPrice(50.0);

        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setCustomer(customer);
        productDiscount.setProduct(productWithDiscount);
        productDiscount.setDiscount(10.0);

        customer.getDiscounts().add(volumeDiscount);
        customer.getDiscounts().add(timeDiscount);
        customer.getDiscounts().add(productDiscount);

        DiscountCalculatorService discountCalculatorService = new DiscountCalculatorService();
        Map<Product, Double> products = new HashMap<>();
        products.put(productWithNoDiscount, 2.0);
        Double result = discountCalculatorService.calculate(customer, products);

        Assertions.assertEquals(200.0, result);
    }

    @Test
    void calculateOriginalExample() {
        Customer customer = new Customer();
        VolumeDiscount volumeDiscount = new VolumeDiscount();
        volumeDiscount.setCustomer(customer);
        volumeDiscount.setMinAmount(15_000.0);
        volumeDiscount.setMaxAmount(21_999.0);
        volumeDiscount.setDiscount(15.0);

        VolumeDiscount volumeDiscount2 = new VolumeDiscount();
        volumeDiscount2.setCustomer(customer);
        volumeDiscount2.setMinAmount(22_000.0);
        volumeDiscount2.setMaxAmount(null);
        volumeDiscount2.setDiscount(20.0);

        TimeDiscount timeDiscount = new TimeDiscount();
        timeDiscount.setCustomer(customer);
        timeDiscount.setDiscount(10.0);
        timeDiscount.setFromDate(Instant.now().minus(100, ChronoUnit.DAYS));
        timeDiscount.setToDate(Instant.now().plus(10, ChronoUnit.DAYS));

        Product productA = new Product();
        productA.setPrice(35.0);
        Product productB = new Product();
        productB.setPrice(67.0);
        Product productC = new Product();
        productC.setPrice(12.0);

        ProductDiscount productDiscountA = new ProductDiscount();
        productDiscountA.setCustomer(customer);
        productDiscountA.setProduct(productA);
        productDiscountA.setDiscount(5.5);

        ProductDiscount productDiscountB = new ProductDiscount();
        productDiscountB.setCustomer(customer);
        productDiscountB.setProduct(productA);
        productDiscountB.setDiscount(8.5);

        customer.getDiscounts().add(volumeDiscount);
        customer.getDiscounts().add(volumeDiscount2);
        customer.getDiscounts().add(timeDiscount);
        customer.getDiscounts().add(productDiscountA);
        customer.getDiscounts().add(productDiscountB);

        DiscountCalculatorService discountCalculatorService = new DiscountCalculatorService();
        Map<Product, Double> products = new HashMap<>();
        products.put(productA, 150.0);
        products.put(productC, 1_450.0);
        Double result = discountCalculatorService.calculate(customer, products);

        Assertions.assertEquals(15_566.25, result);
    }

    @Test
    void reduceMaxDiscount() {
        Customer customer = new Customer();
        customer.setMaxDiscount(10.0);
        Product product1 = new Product();
        product1.setPrice(100.0);

        TimeDiscount timeDiscount = new TimeDiscount();
        timeDiscount.setCustomer(customer);
        timeDiscount.setDiscount(20.0);
        timeDiscount.setFromDate(Instant.now().minus(1, ChronoUnit.DAYS));
        timeDiscount.setToDate(Instant.now().plus(1, ChronoUnit.DAYS));
        customer.getDiscounts().add(timeDiscount);

        DiscountCalculatorService discountCalculatorService = new DiscountCalculatorService();
        Map<Product, Double> products = new HashMap<>();
        products.put(product1, 1.0);
        Double result = discountCalculatorService.calculate(customer, products);

        Assertions.assertEquals(90.0, result);
    }


}