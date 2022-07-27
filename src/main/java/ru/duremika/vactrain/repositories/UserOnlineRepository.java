package ru.duremika.vactrain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.duremika.vactrain.entities.UserOnline;


@Repository
public interface UserOnlineRepository extends JpaRepository<UserOnline, Long> {
    void deleteByUsername(String username);
}
