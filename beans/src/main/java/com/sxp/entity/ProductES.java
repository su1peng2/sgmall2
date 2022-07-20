package com.sxp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 粟小蓬
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductES {
    private String productId;
    private String productName;
    private Integer soldNum;
    private String skuImg;
    private String skuName;
    private Integer sellPrice;
}
