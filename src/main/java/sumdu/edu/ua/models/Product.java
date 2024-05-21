package sumdu.edu.ua.models;

import java.util.List;

public class Product {
    private Integer id;
    private String name;
    private String type;
    private Double price;
    private String paymentVariants;
    private String deliveryVariants;
    private int quantity;
    private boolean isWishlisted;

    public Product(Integer id, String name, String type, Double price, String paymentVariants, String deliveryVariants, int quantity, boolean isWishlisted) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.paymentVariants = paymentVariants;
        this.deliveryVariants = deliveryVariants;
        this.quantity = quantity;
        this.isWishlisted = isWishlisted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPaymentVariants() {
        return paymentVariants;
    }

    public void setPaymentVariants(String paymentVariants) {
        this.paymentVariants = paymentVariants;
    }

    public String getDeliveryVariants() {
        return deliveryVariants;
    }

    public void setDeliveryVariants(String deliveryVariants) {
        this.deliveryVariants = deliveryVariants;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean getIsWishlisted() {
        return isWishlisted;
    }

    public void setIsWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }
}
