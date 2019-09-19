package com.jinnjo.sale.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "jz_time_limit_remind")
@EntityListeners(AuditingEntityListener.class)
public class TimeLimitRemind implements Identifiable<Long> {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "主键", name = "id")
    private Long id;

    @ApiModelProperty(value = "提醒状态0 取消 1 正常", name = "status")
    private Integer status;

    @Version
    private int version;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty(value = "用户ID", name = "userId")
    private Long userId;

    @ApiModelProperty(value = "商品ID", name = "goodsId")
    private Long goodsId;

    @ApiModelProperty(value = "活动时间", name = "activityTime")
    private LocalDate activityTime;
}
