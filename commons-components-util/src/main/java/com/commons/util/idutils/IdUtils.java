package com.util.idutils;

import com.util.encrypt.oneway.Md5Util;
import com.util.encrypt.oneway.Sha1Util;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


/**
 *  id工具类
 * @author Jie.cai58@gmail.com
 * @date 2019/9/19 14:17
 */

public class IdUtils {

    private final long workerId;
    private final static long twepoch = 1361753741828L;
    private long  sequence = 0L;
    private final static long workerIdBits = 4L;
    public  final static long maxWorkerId = -1L ^ -1L << workerIdBits;
    private final static long sequenceBits = 10L;
    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;
    public  final static long sequenceMask = -1L ^ -1L << sequenceBits;
    private long  lastTimestamp = -1L;

    public IdUtils(final long workerId) {
        this.workerId = workerId;
    }

    /**
     * @param length 小于16位
     * @return
     */
    public synchronized String nextId(int length) {
        long nextId = 0;
        long timestamp = this.timeGen();

        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            nextId = 0L;
        } else {
            this.lastTimestamp = timestamp;
            nextId = ((timestamp - twepoch << timestampLeftShift))
                    | (this.workerId << workerIdShift) | (this.sequence);
        }

        String nextIdStr = nextId + "";
        String id = String.format("%08d", nextId).substring(nextIdStr.length() - length, nextIdStr.length());
        return id;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static String genTxid(String firstValue) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(firstValue)) {
            sb.append(firstValue);
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
        String mmdd = formatter.format(date);
        formatter   = new SimpleDateFormat("HHmmss");
        String hhmm = formatter.format(date);
        sb.append(mmdd);
        int workId = ThreadLocalRandom.current().nextInt(100);
        IdUtils idUtils = new IdUtils(workId);
        String randomString = idUtils.nextId(8);
        sb.append(randomString);
        sb.append(hhmm);
        return sb.toString();
    }

    /**
     * 返回时间id
     * @return
     */
    public static final String dateId(){
       DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
       return df.format(new Date());
    }

    /**
     * 订单id
     */
    public static final String activityNo(){
        try{
            return Sha1Util.sha1(Md5Util.EncoderByMd5(UUID.randomUUID().toString() + System.currentTimeMillis()));
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**以时间生成文件名称**/
    public static String generatorFileNameByTime(String originFileName){

        /**年/月/日时分秒毫秒+唯一id**/
        Calendar cal=Calendar.getInstance();
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(cal.get(Calendar.YEAR));
        stringBuffer.append("/");
        stringBuffer.append(cal.get(Calendar.MONTH)+1);
        stringBuffer.append("/");
        stringBuffer.append(cal.get(Calendar.DAY_OF_MONTH));
        stringBuffer.append(cal.get(Calendar.HOUR_OF_DAY));
        stringBuffer.append(cal.get(Calendar.MINUTE));
        stringBuffer.append(cal.get(Calendar.SECOND));
        stringBuffer.append(cal.get(Calendar.MILLISECOND));
        stringBuffer.append(GenerateIdBySnowflake.generatId());
        String fileExtName=originFileName.substring(originFileName.indexOf("."));
        stringBuffer.append(fileExtName);

        return stringBuffer.toString();
    }

}
