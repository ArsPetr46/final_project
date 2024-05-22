package sumdu.edu.ua.models;

import java.util.List;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private String type;
    private String typeDescription;
    private Double price;
    private int quantity;
    private boolean isWishlisted;

    public Product(Integer id, String name, String description, String type, String typeDescription, Double price, int quantity, boolean isWishlisted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.typeDescription = typeDescription;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
