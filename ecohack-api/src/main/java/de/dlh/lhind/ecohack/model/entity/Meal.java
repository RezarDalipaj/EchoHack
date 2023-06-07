package de.dlh.lhind.ecohack.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "meal")
@Getter
@Setter
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer totalPoints;

    @Column
    private Double price;

    @Lob
    @Column
    private String image;

    @OneToMany(
            mappedBy = "meal",
            cascade = CascadeType.ALL
    )
    private List<Nutrition> nutritions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private FoodProvider foodProvider;

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
