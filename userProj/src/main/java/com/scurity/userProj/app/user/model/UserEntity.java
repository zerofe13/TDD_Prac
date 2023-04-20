package com.scurity.userProj.app.user.model;

import com.scurity.userProj.app.common.model.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,length = 50)
    private String userId;
    @Column(nullable = false,length = 20)
    private String passwd;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

}
