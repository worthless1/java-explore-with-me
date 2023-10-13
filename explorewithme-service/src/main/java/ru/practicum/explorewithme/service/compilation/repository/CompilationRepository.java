package ru.practicum.explorewithme.service.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.service.compilation.model.Compilation;
import ru.practicum.explorewithme.service.util.Pagination;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation c WHERE (:pinned is null or c.pinned = :pinned)")
    Page<Compilation> findAllByPinnedAndPagination(@Param("pinned") Boolean pinned, Pagination paginationSetup);

}



