package com.readiness.online_shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private Long orderId;

    @Column(name = "order_code", unique = true, nullable = false)
    private UUID orderCode = UUID.randomUUID();

    @Column(name = "order_date")
    @CreatedDate
    private Date orderDate;

    @Column(name = "total_price")
    @Min(value = 0, message = "TotalPrice tidak boleh kurang dari 0")
    private Long totalPrice;

    @Column(name = "quantity")
    @Min(value = 1, message = "Quantity tidak boleh kurang dari 1")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @PrePersist
    protected void onCreate() {
        this.setOrderCode(UUID.randomUUID());
    }
}
