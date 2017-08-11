package com.learn.redis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dufy on 2017/3/28.
 *
 * cmd /c dir 是执行完dir命令后关闭命令窗口。<br/>
 * cmd /k dir 是执行完dir命令后不关闭命令窗口.<br/>
 * cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。<br/>
 * cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。<br/>
 * redis-cli.exe -h 127.0.0.1 -p 端口<br/>
 * info replication -- 查看主从复制<br/>
 * info sentinel-- 查看哨兵情况<br/>
 *
 * window本地搭redis的哨兵模式：http://blog.csdn.net/liuchuanhong1/article/details/53206028<br/><br/>
 *
 * 启动服务工具类
 */
public class StartRedisServer {
    private final static String redisRootPath = "F:/nosql_learn/Redis3.2";

    public static void main(String[] args) {
        List<String> cmds = new ArrayList<String>();
        String cmdRedis6379 = "cmd /k start redis-server.exe redis.conf ";//redis-server.exe redis.conf
        String cmdRedis6380 = "cmd /k start redis-server.exe redis6380.conf ";//redis-server.exe redis.conf
        String cmdRedis6381 = "cmd /k start redis-server.exe redis6381.conf ";//redis-server.exe redis.conf

        cmds.add(cmdRedis6379);
        cmds.add(cmdRedis6380);
        cmds.add(cmdRedis6381);

        String cmdRedis26379 = "cmd /k start redis-server.exe sentinel.conf --sentinel";//redis-server.exe sentinel26479.conf --sentinel
        String cmdRedis26479 = "cmd /k start redis-server.exe sentinel26479.conf --sentinel";//redis-server.exe sentinel26479.conf --sentinel
        String cmdRedis26579 = "cmd /k start redis-server.exe sentinel26579.conf --sentinel";//redis-server.exe sentinel26479.conf --sentinel

        cmds.add(cmdRedis26379);
        cmds.add(cmdRedis26479);
        cmds.add(cmdRedis26579);

        initRedisServer(cmds);
    }

    public static void initRedisServer(List<String> cmdStr){
        if(cmdStr != null && cmdStr.size() > 0){
            for (String cmd:cmdStr
                 ) {
                try {
                    Process exec = Runtime.getRuntime().exec(cmd, null, new File(redisRootPath));
                    Thread.sleep(1*1000);
                }catch (InterruptedException e) {
                    System.out.println("线程中断异常" + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("cmd command error" + e.getMessage());
                    e.printStackTrace();
                }

            }
        }

    }
}
