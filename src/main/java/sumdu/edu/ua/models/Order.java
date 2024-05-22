package sumdu.edu.ua.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    @JsonProperty()
    private Integer orderId;
    @JsonProperty()
    private Integer orderUserId;
    @JsonProperty()
    private Integer orderProductId;
    @JsonProperty()
    private Integer orderQuantity;
    @JsonProperty()
    private Double orderProductPrice;
    @JsonProperty()
    private Double orderTotalPrice;
    @JsonProperty()
    private String orderEmail;
    @JsonProperty()
    private String orderDeliveryVariant;
    @JsonProperty()
    private String orderPaymentVariant;
    @JsonProperty()
    private String orderStatus;

    public Order(Integer orderId, Integer orderUserId, Integer orderProductId, Integer orderQuantity, Double orderProductPrice, Double orderTotalPrice, String orderEmail, String orderDeliveryVariant, String orderPaymentVariant, String orderStatus) {
        this.orderId = orderId;
        this.orderUserId = orderUserId;
        this.orderProductId = orderProductId;
        this.orderQuantity = orderQuantity;
        this.orderProductPrice = orderProductPrice;
        this.orderTotalPrice = orderTotalPrice;
        this.orderEmail = orderEmail;
        this.orderDeliveryVariant = orderDeliveryVariant;
        this.orderPaymentVariant = orderPaymentVariant;
        this.orderStatus = orderStatus;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(Integer orderUserId) {
        this.orderUserId = orderUserId;
    }

    public Integer getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Integer orderProductId) {
        this.orderProductId = orderProductId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Double getOrderProductPrice() {
        return orderProductPrice;
    }

    public void setOrderProductPrice(Double orderProductPrice) {
        this.orderProductPrice = orderProductPrice;
    }

    public Double getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Double orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }

    public String getOrderDeliveryVariant() {
        return orderDeliveryVariant;
    }

    public void setOrderDeliveryVariant(String orderDeliveryVariant) {
        this.orderDeliveryVariant = orderDeliveryVariant;
    }

    public String getOrderPaymentVariant() {
        return orderPaymentVariant;
    }

    public void setOrderPaymentVariant(String orderPaymentVariant) {
        this.orderPaymentVariant = orderPaymentVariant;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
