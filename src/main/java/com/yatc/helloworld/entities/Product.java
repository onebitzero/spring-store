package com.yatc.helloworld.entities;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "products")
public class Product {
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @ToString.Exclude
    @JoinColumn(name = "category_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
}
