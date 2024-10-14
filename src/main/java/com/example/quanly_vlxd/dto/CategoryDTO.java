package com.example.quanly_vlxd.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    @NotBlank(message = "ID is required")
    private String Id;
    @NotBlank(message = "Name is required")
    private String Name;
    private String Description;
}
