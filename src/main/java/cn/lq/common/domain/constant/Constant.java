package cn.lq.common.domain.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author winterchen
 * @date 2018/4/29
 */
public interface Constant {

    String USER_IN_COOKIE = "S_L_ID";
    /**
     * 最大获取文章条数
     */
    int MAX_POSTS = 9999;
    /**
     * 最大页码
     */
    int MAX_PAGE = 100;
    /**
     * 文章最多可以输入的文字数
     */
    int MAX_TEXT_COUNT = 200000;
    /**
     * 文章标题最多可以输入的文字个数
     */
    int MAX_TITLE_COUNT = 200;
    /**
     * 一些网站配置
     */
    Map<String, String> INIT_CONFIG = new HashMap<>();
    /**
     * session的key
     */
    String LOGIN_SESSION_KEY = "login_user";
    /**
     * aes加密加盐
     */
    String AES_SALT = "0123456789abcdef";
    /**
     * 上传文件最大1M
     */
    Integer MAX_FILE_SIZE = 1048576;

    interface Common {
        String PARAM_IS_EMPTY = "参数为空";
    }

    interface Article {
        String TITLE_IS_TOO_LONG = "文章标题过长";
        String TITLE_CAN_NOT_EMPTY = "文章标题不能为空";
        String CONTENT_CAN_NOT_EMPTY = "文章内容不能为空";
        String CONTENT_IS_TOO_LONG = "文章字数超过限制";
    }

    interface Att {
        String DELETE_ATT_FAIL = "删除附件信息失败";
        String UPLOAD_FILE_FAIL = "上传附件失败";
    }

    interface Comment {
        String ADD_NEW_COMMENT_FAIL = "添加评论失败";
        String COMMENT_NOT_EXIST = "评论不存在";
    }

    interface Meta {
        String ADD_META_FAIL = "添加项目信息失败";
        String NOT_ONE_RESULT = "获取的项目的数量不止一个";
        String META_IS_EXIST = "该项目已经存在";
    }

    interface Auth {
        String USERNAME_PASSWORD_IS_EMPTY = "用户名和密码不可为空";
        String USERNAME_PASSWORD_ERROR = "用户名不存在或密码错误";
    }

    interface Es {
        String FIELD_ID = "id";
        String FIELD_DELETED = "deleted";
        String FIELD_MODIFIED = "modified";
        String FIELD_CREATED = "created";
    }

    /**
     * 系统环境
     */
    interface Env {
        String DEV = "dev";
        String PROD = "prod";
    }

}
