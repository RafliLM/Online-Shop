package com.readiness.online_shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue
    private Long itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_code", unique = true, nullable = false)
    private UUID itemCode;

    @Column(name = "stock")
    @Min(value = 0, message = "Stock can't be less than 0")
    private Integer stock;

    @Column(name = "price")
    @Min(value = 0, message = "Price can't be less than 0")
    private Long price;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_re_stock")
    private Date lastReStock;

    @JsonIgnore
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @PrePersist
    protected void onCreate() {
        this.setItemCode(UUID.randomUUID());
    }
}
