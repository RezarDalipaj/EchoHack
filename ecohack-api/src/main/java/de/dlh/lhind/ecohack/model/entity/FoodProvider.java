package de.dlh.lhind.ecohack.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "food_provider")
public class FoodProvider{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "nipt")
    private String nipt;

    @Column
    private Long latitude;

    @Column
    private Long longtitude;

    @OneToMany(
            mappedBy = "foodProvider",
            cascade = CascadeType.ALL
    )
    private List<Order> orderList;


}
