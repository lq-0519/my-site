package cn.lq.common.utils;

/**
 * 单机版雪花算法ID生成
 * <p>
 * ID是严格自增的, 生成的下一个ID一定大于前面一个
 * id构成: 低12位是sequence防止并发数字, 剩余高位是时间戳
 *
 * @author liqian477
 * @date 2023-07-17 16:36:14
 */
public class EsIdGenerator {

    /**
     * 时间起始标记点，作为基准，2023-07-01 00:00:00
     */
    private static final long EPOCH = 1688140800000L;
    /**
     * 毫秒内自增位
     */
    private static final long SEQUENCE_BITS = 12L;
    /**
     * 4095,111111111111,12位
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /**
     * 0，并发控制
     */
    private static long sequence = 0L;
    /**
     * 记录上一次的时间戳
     */
    private static long lastTimestamp = -1L;

    /**
     * 获取下一个ID
     */
    public static synchronized long nextId() {
        long timestamp = timeGen();
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                //sequence队列取完了, 强制时间自增1ms
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;
        //时间戳左移12位,
        return ((timestamp - EPOCH) << SEQUENCE_BITS) | sequence;
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

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            long id = nextId();
            System.out.println("id = " + id);
        }
    }
}
