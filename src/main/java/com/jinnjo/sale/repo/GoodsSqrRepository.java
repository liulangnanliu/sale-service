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

    @Query(value = "select id from jz_goods_info_sqr where id in (?1)", nativeQuery = true)
    List<BigInteger> queryAllByIdNotIn(Collection<Long> ids);
}
