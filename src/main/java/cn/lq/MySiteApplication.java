package cn.lq;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liqian477
 */
@SpringBootApplication
@MapperScan("cn.lq.dao.mysql")
@EnableEncryptableProperties
public class MySiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySiteApplication.class, args);
    }
}
