package jja.cc.productCalculator.service;

import jja.cc.productCalculator.model.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DiscountCalculatorService {
    public Double calculate(Customer customer, Map<Product, Double> products) {
        //temporary discount per product
        Map<Product, Double> discounts = new HashMap<>();

        //price per product for all products before discounts
        Map<Product, Double> pricePerProduct = new HashMap<>();
        double totalPriceBeforeDiscounts = 0.0;

        totalPriceBeforeDiscounts = countTotalsAndApplyProductAndTimeDiscounts(customer, products, discounts,
                pricePerProduct, totalPriceBeforeDiscounts);

        applyVolumeDiscount(customer, products, discounts, totalPriceBeforeDiscounts);

        calculateTotalPriceAfterDiscounts(products, discounts, pricePerProduct);

        double totalPriceAfterDiscounts = pricePerProduct.values().stream().mapToDouble(Double::doubleValue).sum();

        totalPriceAfterDiscounts = applyDiscountReduction(customer, totalPriceBeforeDiscounts, totalPriceAfterDiscounts);

        return totalPriceAfterDiscounts;
    }

    private double countTotalsAndApplyProductAndTimeDiscounts(
            Customer customer, Map<Product, Double> products, Map<Product, Double> discounts, Map<Product,
            Double> pricePerProduct, double totalPriceBeforeDiscounts) {
        for (Product product : products.keySet()) {
            discounts.put(product, 0.0);

            //each product x pieces
            pricePerProduct.put(product, products.get(product) * product.getPrice());
            totalPriceBeforeDiscounts += products.get(product) * product.getPrice();

            //apply product discount
            Optional<ProductDiscount> productDiscount = getProductDiscount(customer, product);
            productDiscount.ifPresent(discount -> discounts.put(product, discount.getDiscount()));

            //apply time discount
            Optional<TimeDiscount> timeDiscount = getCurrentTimeDiscount(customer);
            timeDiscount.ifPresent(discount ->
                    discounts.put(product, (discounts.get(product) + discount.getDiscount())));
        }
        return totalPriceBeforeDiscounts;
    }

    private double applyDiscountReduction(Customer customer, double totalPriceBeforeDiscounts, double totalPriceAfterDiscounts) {
        double totalDiscount = totalPriceAfterDiscounts / totalPriceBeforeDiscounts * 100;
        if (customer.getMaxDiscount() != null && customer.getMaxDiscount() < totalDiscount) {
            totalPriceAfterDiscounts = totalPriceBeforeDiscounts * (100 - customer.getMaxDiscount()) / 100;
        }
        return totalPriceAfterDiscounts;
    }

    private void calculateTotalPriceAfterDiscounts(Map<Product, Double> products, Map<Product, Double> discounts, Map<Product, Double> pricePerProduct) {
        for (Product product : products.keySet()) {
            Double beforeDiscount = pricePerProduct.get(product);
            Double afterDiscount = beforeDiscount * ((100 - discounts.get(product)) / 100);
            pricePerProduct.put(product, afterDiscount);
        }
    }

    private void applyVolumeDiscount(Customer customer, Map<Product, Double> products, Map<Product, Double> discounts, double totalPriceBeforeDiscounts) {
        Optional<VolumeDiscount> volumeDiscount = getVolumeDiscount(customer, totalPriceBeforeDiscounts);

        if (volumeDiscount.isPresent()) {
            for (Product product : products.keySet()) {
                discounts.put(product, (discounts.get(product) + volumeDiscount.get().getDiscount()));

            }
        }
    }

    private Optional<ProductDiscount> getProductDiscount(Customer customer, Product product) {
        for (Discount discount : customer.getDiscounts()) {
            if (discount instanceof ProductDiscount &&
                    ((ProductDiscount) discount).getProduct().equals(product)) {
                return Optional.of((ProductDiscount) discount);
            }
        }
        return Optional.empty();
    }

    private Optional<TimeDiscount> getCurrentTimeDiscount(Customer customer) {
        for (Discount discount : customer.getDiscounts()) {
            if (discount instanceof TimeDiscount &&
                    !Instant.now().isBefore(((TimeDiscount) discount).getFromDate()) &&
                    ((((TimeDiscount) discount).getToDate() == null) ||
                            !Instant.now().isAfter(((TimeDiscount) discount).getToDate()))
            ) {
                return Optional.of((TimeDiscount) discount);
            }
        }
        return Optional.empty();
    }

    private Optional<VolumeDiscount> getVolumeDiscount(Customer customer, Double totalPrice) {
        for (Discount discount : customer.getDiscounts()) {
            if (discount instanceof VolumeDiscount &&
                    totalPrice >= ((VolumeDiscount) discount).getMinAmount() &&
                    ((((VolumeDiscount) discount).getMaxAmount() == null) ||
                            totalPrice <= ((VolumeDiscount) discount).getMaxAmount())
            ) {
                return Optional.of((VolumeDiscount) discount);
            }
        }
        return Optional.empty();
    }
}
