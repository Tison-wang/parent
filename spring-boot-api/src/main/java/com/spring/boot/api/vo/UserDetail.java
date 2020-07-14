package com.spring.boot.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @version 1.0
 * @date 2020/7/9 13:46
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDetail {

    private Integer id;

    private String name;

    private String idCardNumber;

    private Integer fkUserId;

    private User user;

}
