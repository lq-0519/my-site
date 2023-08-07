package cn.lq.common.domain.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liqian477
 * @date 2023/08/07 16:39
 */
@Component
public class SysConstant {
    /**
     * @see Constant.Env
     */
    public static String env;

    @Value("${spring.profiles.active}")
    public static void setEnv(String env) {
        SysConstant.env = env;
    }
}
