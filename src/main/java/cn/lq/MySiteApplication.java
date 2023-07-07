package cn.lq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author liqian477
 */
@SpringBootApplication
@MapperScan("cn.lq.dao.mysql")
@EnableCaching
public class MySiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySiteApplication.class, args);
    }
}
