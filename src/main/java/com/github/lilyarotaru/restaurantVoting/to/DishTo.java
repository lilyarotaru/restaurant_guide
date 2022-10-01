package com.github.lilyarotaru.restaurantVoting.to;

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
    @Min(0)
    Integer price;

    @NotNull
    LocalDate dishDate;

    public DishTo(Integer id, String name, int price, LocalDate dishDate) {
        super(id, name);
        this.price = price;
        this.dishDate = dishDate;
    }
}
