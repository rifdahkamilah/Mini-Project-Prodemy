package com.prodemy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_PRICE")
    private Long productPrice;

    @Column(name = "PRODUCT_IMAGE", length = 256)
    private String productImage;

    @Column(name = "EXTRA_IMAGE1")
    private String extraImage1;

    @Column(name = "EXTRA_IMAGE2")
    private String extraImage2;

    @Column(name = "EXTRA_IMAGE3")
    private String extraImage3;

    @Column(name = "EXTRA_IMAGE4")
    private String extraImage4;

    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts;
}
