package com.full_party.heart.repository;

import com.full_party.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
}
