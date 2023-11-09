package com.prodemy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

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

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_price")
    private Long productPrice;

    @Column(name = "product_image", length = 256)
    private String productImage;

    @Column(name = "extra_image1")
    private String extraImage1;

    @Column(name = "extra_image2")
    private String extraImage2;

    @Column(name = "extra_image3")
    private String extraImage3;

    @Column(name = "extra_image4")
    private String extraImage4;

    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Cart> carts;
}
