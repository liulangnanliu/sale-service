package com.jinnjo.sale.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Date;


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Identifiable<Long> {
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "主键", name = "id")
    private Long id;

    @ApiModelProperty(value = "状态", name = "status")
    private Integer status;

    @Version
    private int version;

    @CreatedDate
    @Column(name = "create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

