package cn.lq.common.utils;

import cn.lq.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * tweeter的snowflake Java版:
 * <p>
 * id构成: 40位的时间前缀 + 10位的节点标识 + 4位随机数 + 10位的sequence避免并发的数字(10位不够用时强制得到新的时间前缀) 注意这里进行了小改动: snowflake是5位的datacenter加5位的机器id;
 * 这里变成使用10位的机器id,另加上5位随机数,防止后期新增实例时
 *
 * @author wangzhenguo5
 * @date 2018/01/22
 */
public class EsIdGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsIdGenerator.class);

    /**
     * 机器标识ID
     */
    private final static long WORKER_ID;

    /**
     * 时间起始标记点，作为基准，2023-07-01 00:00:00
     */
    private static final long EPOCH = 1688140800000L;

    /**
     * 机器标识位数
     */
    private static final long WORKER_ID_BITS = 10L;

    /**
     * 机器ID最大值: 1023
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 机器标识随机数（防止Hash冲突）（4L）
     */
    private static final long WORKER_RANDOM_BITS = 4L;

    /**
     * 机器标识随机数(范围[0, 32))
     */
    private static final long WORKER_RANDOM_ID = new Random().nextInt((int) 1L << WORKER_RANDOM_BITS);
    /**
     * 毫秒内自增位(10L)
     */
    private static final long SEQUENCE_BITS = 10L;
    /**
     * 左移12位
     */
    private static final long WORKER_ID_RANDOM_SHIFT = SEQUENCE_BITS;
    /**
     * 左移17位(左移14)
     */
    private static final long WORKER_ID_SHIFT = WORKER_ID_RANDOM_SHIFT + WORKER_RANDOM_BITS;
    /**
     * 左移27位（24）
     */
    private static final long TIMESTAMP_LEFT_SHIFT = WORKER_ID_SHIFT + WORKER_ID_BITS;
    /**
     * 4095,111111111111,12位（10位）
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 0，并发控制
     */
    private static long sequence = 0L;

    private static long lastTimestamp = -1L;

    static {
        try {
            // 获取实例ID值
            String deployInstanceId = System.getProperty("deploy.instance.id", "0");
            String localIp = Inet4Address.getLocalHost().getHostAddress();
            // 对deployInstanceId进行Hash映射,减小WORKER_ID冲突概率(采用hashmap中hash原理)
            WORKER_ID = calculateWorkerId(localIp, deployInstanceId);
            LOGGER.warn("实例[{}],IP[{}],随机值为:{}", deployInstanceId, localIp, WORKER_RANDOM_ID);
        } catch (Exception e) {
            LOGGER.error("EsIdGenerator 初始化失败!", e);
            throw new BusinessException("EsIdGenerator 初始化失败!");
        }
    }

    /**
     * 根据IP地址及实例ID计算workerId
     *
     * @param localIp          IP地址
     * @param deployInstanceId 实例ID
     */
    private static long calculateWorkerId(String localIp, String deployInstanceId) {
        long tmpHash = 19, mul = 1L;

        String[] arrayIp = StringUtils.split(localIp, ".");
        for (String tmpIp : arrayIp) {
            if (StringUtils.equals(tmpIp, "0")) {
                continue;
            }

            mul *= NumberUtils.toLong(tmpIp, 1L);
        }

        tmpHash ^= (mul * NumberUtils.toLong(deployInstanceId, 1L));
        tmpHash ^= (tmpHash >>> 20) ^ (tmpHash >>> 12);
        tmpHash = tmpHash ^ (tmpHash >>> 7) ^ (tmpHash >>> 4);

        return (Math.abs(tmpHash) % MAX_WORKER_ID);
    }

    /**
     * 获取下一个ID
     */
    public static synchronized long nextId() {
        return nextId(null);
    }

    /**
     * 获取下一个ID
     */
    @SuppressWarnings("SameParameterValue")
    private static synchronized long nextId(Date baseTime) {
        long timestamp = (baseTime != null ? baseTime.getTime() : timeGen());
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;
        //时间戳左移24位，机器标识id左移14位，机器标识随机数左移10位
        //40位的时间前缀 + 10位的节点标识 + 4位随机数 + 10位的sequence避免并发的数字
        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT) | (WORKER_ID << WORKER_ID_SHIFT) | (WORKER_RANDOM_ID << WORKER_ID_RANDOM_SHIFT) | sequence;
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();

            try {
                Thread.sleep(1L);
            } catch (Exception ignored) {
            }
        }

        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println("版本号为" + nextId());
        long a = WORKER_RANDOM_ID;
        long b = WORKER_RANDOM_ID << WORKER_ID_RANDOM_SHIFT;
        System.out.println("机器标识随机数(范围[0, 32))" + a);
        System.out.println("左移前二进制" + Long.toBinaryString(a));
        System.out.println("左移后二进制" + Long.toBinaryString(b));

        long c = WORKER_ID;
        long d = WORKER_ID << WORKER_ID_SHIFT;
        System.out.println("机器标识ID" + c);
        System.out.println("左移前二进制" + Long.toBinaryString(c));
        System.out.println("左移后二进制" + Long.toBinaryString(d));

        long e = timeGen() - EPOCH;
        long f = (timeGen() - EPOCH) << TIMESTAMP_LEFT_SHIFT;
        System.out.println("时间戳" + e);
        System.out.println("左移前二进制" + Long.toBinaryString(e));
        System.out.println("左移后二进制" + Long.toBinaryString(f));

        String dateTime = "2020-04-01 00:00:00";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));

        String dateTime1 = "2054-04-01 00:00:00";
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime1));
        //2的40次方是 1099511627776 2524521600000
        System.out.println("毫秒差：" + (calendar1.getTimeInMillis() - calendar.getTimeInMillis()));

        System.out.println("时间差" + (1L << 40) / (1000L * 60 * 60 * 24 * 365));
    }
}
