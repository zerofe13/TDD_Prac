package com.scurity.userProj.app.user.model.dto;

import com.scurity.userProj.app.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String userId;
    private String passwd;
    private UserRole role;
}
