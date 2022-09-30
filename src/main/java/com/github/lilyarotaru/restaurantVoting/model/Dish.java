package com.github.lilyarotaru.restaurantVoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = @UniqueConstraint(name = "uk_date_dish", columnNames = {"dish_date", "name", "restaurant_id"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = "restaurant")
public class Dish extends NamedEntity {

    @Column(name = "dish_date", columnDefinition = "date default now()")
    @NotNull
    @JsonIgnore
    private LocalDate dishDate = LocalDate.now();

    @Column(name = "price")
    @NotNull
    @Min(100)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    private Restaurant restaurant;

    public Dish(Integer id, String name, int price, Restaurant restaurant) {
        this(id, name, LocalDate.now(), price, restaurant);
    }

    public Dish(Integer id, String name, LocalDate dishDate, int price, Restaurant restaurant) {
        super(id, name);
        this.dishDate = dishDate;
        this.price = price;
        this.restaurant = restaurant;
    }
}
