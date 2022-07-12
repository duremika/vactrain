package ru.duremika.vactrain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.duremika.vactrain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
