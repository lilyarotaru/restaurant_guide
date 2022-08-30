package ru.javaops.topjava2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = "restaurant")
public class Dish extends NamedEntity {

    @Column(name = "dish_date", columnDefinition = "date default now()")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dishDate = LocalDate.now();

    @Column(name = "price")
    @NotNull
    @Min(10)
    private int price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey
            (foreignKeyDefinition = "foreign key (restaurant_id) references restaurant on delete set null"))
    //when delete restaurant want to use it's menu for other restaurants
    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Restaurant restaurant;
}
