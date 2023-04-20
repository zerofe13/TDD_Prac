package com.scurity.userProj.app.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class CommonEntity implements Serializable {

    @CreationTimestamp
    @Column(nullable = false,length = 20, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(length = 20,insertable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean isEnable = true;

}
