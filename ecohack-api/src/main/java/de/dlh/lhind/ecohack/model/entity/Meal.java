package de.dlh.lhind.ecohack.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String name;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL
    )
    private List<Nutrition> nutritions;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL
    )
    private List<Tag> tags;

    @ManyToMany(mappedBy = "meals", fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Order> orders;
}
