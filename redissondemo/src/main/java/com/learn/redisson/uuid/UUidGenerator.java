package com.learn.redisson.uuid;

import com.learn.redisson.manager.RedisPoolAPIManager;
import com.learn.redisson.util.DistributedLockUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dufy on 2017/3/28.
 */
public class UUidGenerator {

        private int preKeyLen;
        private int defaultTotalNum;
        private int defaultCacheNum;

        /*redis db */
        private static final String REDIS_DB= "reids_db";
        /*redis cach */
        private static final String REDIS_CACH = "reids_cach";

        public UUidGenerator() {

            this.preKeyLen = 10;

            this.defaultTotalNum = 20;

            this.defaultCacheNum = 1000;
        }

        public void setPreKeyLen(int preKeyLen) {
            this.preKeyLen = preKeyLen;
        }

        public void setDefaultCacheNum(int defaultCacheNum) {
            this.defaultCacheNum = defaultCacheNum;
        }

        public void setDefaultTotalNum(int defaultTotalNum) {
            this.defaultTotalNum = defaultTotalNum;
        }

        public String getNextUuid(String preKey, int cacheNum) {
            return getNextUuid(preKey, this.defaultTotalNum - this.preKeyLen, "0",
                    substractPreKey(preKey) + "#", true, cacheNum);
        }

        public String getNextUuid(String preKey) {
            return getNextUuid(preKey, this.defaultCacheNum);
        }

        public String getNextUuid(String preKey, int length, String fillChar,
                                  String formatStr, boolean needFormat, int cacheNum) {
            String uuidStr = "";

            preKey = substractPreKey(preKey);
            int nextUuid = getNextIntUuid(preKey, cacheNum);

            if (needFormat) {
                String formatUuidStr = getFormatUuid(nextUuid, length, fillChar);

                uuidStr = formatStr.replace("#", formatUuidStr);
            } else {
                uuidStr = "" + nextUuid;
            }

            return uuidStr;
        }


        public int getNextIntUuid(String preKey, int cacheNum) {

            Integer nowUuid = null ;
            try {
                DistributedLockUtil.lock(preKey);
                Jedis jedis = null;
                JedisSentinelPool pool = null;
                try {

                    pool = RedisPoolAPIManager.getPool();
                    jedis = pool.getResource();

                    String nowStr = jedis.get(REDIS_CACH+preKey);
                    if(nowStr == null || "".equals(nowStr)){
                        nowStr = "0";
                    }
                    nowUuid = Integer.valueOf(nowStr);

                    if ((nowUuid == null) || (nowUuid.intValue() == 0)) {
                        nowUuid = Integer.valueOf(1);

                        jedis.set(REDIS_DB+preKey, Integer.toString(cacheNum));
                        jedis.set(REDIS_CACH+preKey, Integer.toString(1));

                    } else {
                        nowUuid = Integer.valueOf(nowUuid.intValue() + 1);

                        if (Integer.valueOf(jedis.get(REDIS_DB+preKey)) <= Integer.valueOf(jedis.get(REDIS_CACH+preKey))) {

                            int num = Integer.valueOf(jedis.get(REDIS_DB+preKey)).intValue();
                            jedis.set(REDIS_DB+preKey, Integer.toString(num + cacheNum));
                            jedis.set(REDIS_CACH+preKey, Integer.toString(nowUuid));

                        }else {
                            jedis.set(REDIS_CACH+preKey, Integer.toString(nowUuid));
                        }
                    }

                } catch (Exception e) {
                    System.out.println("线程 redis :"+Thread.currentThread().getId()+" exception :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    getNextIntUuid(preKey, cacheNum);
                    e.printStackTrace();
                }finally{
                    if(jedis != null){
                        pool.returnResource(jedis);
                    }
                }

            } catch (Exception e) {
                System.out.println("线程锁 :"+Thread.currentThread().getId()+" exception :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                e.printStackTrace();
            }finally {
                DistributedLockUtil.unlock(preKey);
            }


            return nowUuid.intValue();
        }

        public int getNextIntUuid(String preKey) {
            return getNextIntUuid(preKey, this.defaultCacheNum);
        }

        private String substractPreKey(String preKey) {
            String str = preKey.trim();
            if (str.length() > this.preKeyLen) {
                str = str.substring(str.length() - this.preKeyLen);
            }
            return str;
        }

        private String getFormatUuid(int nextUuid, int length, String fillChar) {
            StringBuffer buffer = new StringBuffer("");

            int len = length - (""+nextUuid).length();
            for (int i = 0; i < len; ++i) {
                buffer.append(fillChar);
            }

            buffer.append("" + nextUuid);
            return buffer.toString();

        }


}
