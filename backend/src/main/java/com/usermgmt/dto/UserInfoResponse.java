package com.usermgmt.dto;

import com.usermgmt.entity.User;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserInfoResponse {
    private Long id;
    private String username;
    private Integer gender;
    private Integer age;
    private String profession;
    private String address;
    private LocalDateTime createTime;

    public static UserInfoResponse fromEntity(User user) {
        UserInfoResponse resp = new UserInfoResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setGender(user.getGender());
        resp.setAge(user.getAge());
        resp.setProfession(user.getProfession());
        resp.setAddress(user.getAddress());
        resp.setCreateTime(user.getCreateTime());
        return resp;
    }
}
