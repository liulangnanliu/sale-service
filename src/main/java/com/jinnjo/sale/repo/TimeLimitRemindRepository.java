package com.jinnjo.sale.repo;

import com.jinnjo.sale.domain.TimeLimitRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeLimitRemindRepository extends JpaRepository<TimeLimitRemind, Long>{
}
