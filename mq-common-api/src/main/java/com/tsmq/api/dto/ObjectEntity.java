package com.tsmq.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 *
 * @author
 * @version 1.0
 * @date 2020/3/27 13:42
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ObjectEntity implements Serializable {

    private static final long serialVersionUID = 6215131238120280803L;

    Integer age;

    String userName;

}
