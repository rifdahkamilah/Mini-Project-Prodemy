package com.prodemy.entity;

import org.springframework.lang.Nullable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Nullable
    private String metodePembayaran;
    private String status;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product products;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User users;
}
