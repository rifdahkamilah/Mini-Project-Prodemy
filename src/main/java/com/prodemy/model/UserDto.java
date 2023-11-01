package com.prodemy.model;

import com.prodemy.entity.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    private Role role;
}
