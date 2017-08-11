package com.learn.redisson.test;

import com.learn.redisson.util.ThreadUtil;
import com.learn.redisson.uuid.UUidGenerator;

/**
 * Created by dufy on 2017/3/28.
 * 测试类
 */
public class UUidGeneratorLockTest {

    public  static void main(String[] args) {
        UUidGenerator uuid = new UUidGenerator();
         String perKey = "1788dufy";
         int  num = 100;


        for (int i = 1; i <= 10; i++) {
            ThreadUtil tu = new ThreadUtil(uuid,perKey,num);
            tu.start();
        }

    }
}
