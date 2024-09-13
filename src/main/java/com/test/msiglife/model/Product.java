package com.test.msiglife.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;


@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name tidak boleh kosong")
    private String name;
    @Positive(message = "Price harus bernilai positif")
    private Long price;
    @Column(name = "`description`")
    private String description;
    @Column(name = "`status`")
    private String status;
    private Integer productId;
    @Column(name = "`action`")
    private String action;
}
