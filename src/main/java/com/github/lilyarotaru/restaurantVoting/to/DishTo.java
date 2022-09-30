package com.github.lilyarotaru.restaurantVoting.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {

    @NotNull
    @Min(100)
    Integer price;

    public DishTo(Integer id, String name, int price) {
        super(id, name);
        this.price = price;
    }
}
