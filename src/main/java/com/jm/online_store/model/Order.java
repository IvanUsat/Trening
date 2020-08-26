package com.jm.online_store.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    private Double orderPrice;

    @NonNull
    private LocalDateTime dateTime;

    @NonNull
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JsonManagedReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonBackReference
    private List<ProductInOrder> productInOrders;

    // Список статусов заказа
    public enum Status {
        COMPLETED, CANCELED, INCARTS;
    }

    public Order(@NonNull LocalDateTime dateTime, @NonNull Status status) {
        this.dateTime = dateTime;
        this.status = status;
    }
}