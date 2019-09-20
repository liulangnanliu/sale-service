package com.jinnjo.sale.repo;

import com.jinnjo.sale.domain.TimeLimitRemind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TimeLimitRemindRepository extends JpaRepository<TimeLimitRemind, Long>{
    TimeLimitRemind findByUserIdAndActivityTimeAndGoodsIdAndStatus(Long userId, Date activityTime,Long goodsId,Integer status);
    List<TimeLimitRemind> findByStatusAndActivityTimeLessThanEqual(Integer status,Date now);
    List<TimeLimitRemind> findByUserId(Long userId);
}
