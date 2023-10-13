package ru.practicum.explorewithme.service.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.user.model.User;
import ru.practicum.explorewithme.service.util.Pagination;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByIdIn(List<Long> ids, Pagination pagination);
}