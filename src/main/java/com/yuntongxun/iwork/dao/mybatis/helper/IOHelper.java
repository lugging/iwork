package com.yuntongxun.iwork.dao.mybatis.helper;

import java.io.Closeable;
import java.io.IOException;

/**
 * @program: iwork-dao
 * @description:
 * @author: liugang
 * @create: 2020-05-20 16:21
 **/
public class IOHelper {

    /**
     * 关闭连接
     * @param closeable
     */
    public static void close(AutoCloseable closeable){
        if ( closeable != null ){
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }
}
