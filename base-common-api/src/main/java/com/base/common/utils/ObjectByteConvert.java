package com.base.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author
 * @version 1.0
 * @date 2020/3/27 16:17
 */
@Slf4j
public class ObjectByteConvert {

    /**
     * 对象转 byte 数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            log.error("对象转byte数组时发生异常");
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * byte 数组转对象
     *
     * @param bytes
     * @return
     */
    public static <T> T toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (EOFException ex) {
            log.error("已从流中读取完毕");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            log.error("byte数组转对象时发生异常");
            ex.printStackTrace();
        }
        return (T) obj;
    }

}
