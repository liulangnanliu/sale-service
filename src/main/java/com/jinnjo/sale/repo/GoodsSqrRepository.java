package com.jinnjo.sale.repo;

import com.jinnjo.sale.domain.GoodsSqr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Repository
public interface GoodsSqrRepository extends JpaRepository<GoodsSqr, Long>{
    List<GoodsSqr> queryAllByIdIn(Collection<Long> ids);


    GoodsSqr findByIdAndStatus(Long id, Integer status);
}
