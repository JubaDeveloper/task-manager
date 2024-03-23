package io.github.jubadeveloper.core.repository;

import io.github.jubadeveloper.core.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.user.email = ?1")
    List<Task> findByUserEmail (String email);
    Task findByIdAndUserEmail (Long id, String email);
}
