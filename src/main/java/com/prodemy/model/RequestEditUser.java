package com.prodemy.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestEditUser {
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String password;
}
