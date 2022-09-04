package ru.javaops.topjava2.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {

    @NotNull
    @Min(100)
    int price;

    @NotNull
    LocalDate dishDate = LocalDate.now();

    @NotNull
    int restaurantId;

    public DishTo(Integer id, String name, int price, int restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }
}
