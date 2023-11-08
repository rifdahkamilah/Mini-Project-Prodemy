package com.prodemy.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleEditRequest {
    private long id;

    @Size(max = 256)
    private String name;

    @Size(max = 100)
    private String password;
}
