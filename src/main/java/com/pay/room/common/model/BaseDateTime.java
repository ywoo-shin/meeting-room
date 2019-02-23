package com.pay.room.common.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseDateTime {
    @Temporal(TemporalType.TIMESTAMP) private Date createdAt = new Date();
    @Temporal(TemporalType.TIMESTAMP) private Date modifiedAt = new Date();
}
