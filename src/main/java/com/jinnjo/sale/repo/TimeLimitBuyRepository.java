package com.jinnjo.sale.repo;

import com.jinnjo.sale.domain.TimeLimitBuy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TimeLimitBuyRepository extends JpaRepository<TimeLimitBuy, Long>{
    List<TimeLimitBuy> findByGoodIdInAndCreateTimeBetween(List<Long> goodsIdList, Date startTime,Date endTime);
}
