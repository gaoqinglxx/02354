package com.usermgmt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Min(value = 0, message = "性别值无效")
    @Max(value = 1, message = "性别值无效")
    private Integer gender;

    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;

    @Size(max = 50, message = "职业长度不能超过50")
    private String profession;

    @Size(max = 200, message = "住址长度不能超过200")
    private String address;
}
