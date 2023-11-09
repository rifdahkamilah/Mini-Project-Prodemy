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

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_PRICE")
    private Long productPrice;

   @Column(name = "PRODUCT_IMAGE", length = 256)
   private String productImage;

//    @Column(name = "main_image")
//    private String mainImage;
//
//    @Column(name = "extra_image1")
//    private String extraImage1;
//
//    @Column(name = "extra_image2")
//    private String extraImage2;
//
//    @Column(name = "extra_image3")
//    private String extraImage3;

    @OneToMany(mappedBy = "products", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Cart> carts;
}
