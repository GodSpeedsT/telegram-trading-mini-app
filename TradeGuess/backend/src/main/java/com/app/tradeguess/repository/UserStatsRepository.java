package com.app.tradeguess.repository;

import com.app.tradeguess.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatsRepository extends JpaRepository<User, Integer> {
}
