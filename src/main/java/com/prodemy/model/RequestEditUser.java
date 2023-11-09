package com.prodemy.model;

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

    @Size(max = 100)
    private String email;

    @Size(max = 256)
    private String name;

    @Size(max = 100)
    private String password;
}
