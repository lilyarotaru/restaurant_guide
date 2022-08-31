package ru.javaops.topjava2.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava2.model.Dish;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
}