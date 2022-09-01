package ru.javaops.topjava2.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @OneToMany(mappedBy = "restaurant")
//    @JsonManagedReference
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, List<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }
}