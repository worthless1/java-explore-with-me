package ru.practicum.explorewithme.service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}