package com.wwe.superstars.repository;

import com.wwe.superstars.model.Superstar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperstarRepository extends JpaRepository<Superstar, Long> {
}
