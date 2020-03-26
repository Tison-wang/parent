package com.tszk.common.api.listener;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

/**
 * Description:
 *
 * @author
 * @version 1.0
 * @date 2020/3/21 19:21
 */
public class CusZkSerializer implements ZkSerializer {

    /**
     * 序列化，将对象转化为字节数组
     */
    public byte[] serialize(Object obj) throws ZkMarshallingError {
        try {
            return String.valueOf(obj).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ZkMarshallingError(e);
        }
    }

    /**
     * 反序列化，将字节数组转化为UTF_8字符串
     */
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ZkMarshallingError(e);
        }
    }

}
