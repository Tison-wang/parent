package com.spring.boot.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 * @version 1.0
 * @date 2020/7/8 16:07
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserVo {

    private Integer id;

    private String account;

    private UserDetailVo userDetailVo;

}
