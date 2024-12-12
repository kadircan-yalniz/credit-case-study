package com.casestudy.credit.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseCreatedColumns {
    @Column(name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @PrePersist
    public void prePersist(){
        createdAt = Calendar.getInstance().getTime();
    }
    @PreUpdate
    public void preUpdate(){
        updatedAt = Calendar.getInstance().getTime();
    }

}
