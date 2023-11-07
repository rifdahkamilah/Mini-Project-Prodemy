package com.prodemy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEditRequest {

    private long id;
    private String productName;
    private String productDescription;
    private long productPrice;
    // private String product_Image;

}