package com.rent.Base.user;

import com.rent.entity.User;
import com.rent.interceptor.TokenInterceptor;
import com.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
/**
 * 根据token获取用户信息
 */
@Component
public class UserConfig {
    private static HashMap<String, Object> userInfos = new HashMap<>();
    @Autowired
    private UserService userService;
    private static UserConfig userConfig;

    public void setUserInfoService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 利用构造器初始化此类
     * 因为传统的@Autowired无法正常注入，需要此方法来初始化
     */
    @PostConstruct
    public void init() {
        userConfig = this;
        userConfig.userService = this.userService;
    }

    /**
     * 从token中获取用户id
     * 朱阳刚 2020.09.01
     *
     * @return
     */
    public static String getUserId() {
        String token = TokenInterceptor.getToken();
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String userId = jedis.get(token);
        return userId;
    }

    public static User getUserInfo() {
        String userId = getUserId();
        Map<String, Object> map = new HashMap<>();
        map.clear();
        if (!StringUtils.isEmpty(userId)) {
            map.put("userId", userId);
        }
        User user = userConfig.userService.queryById(map);
        userInfos.put(userId, user);
        return user;
    }

    /**
     * 将用户信息放入缓存里面
     *
     * @param userId
     * @return
     */
    public static Map<String, Object> setUserInfo(String userId) {
        Map<String, Object> map = (Map<String, Object>) userInfos.get(userId);
        if (map != null) {
            return map;
        }
        map.clear();
        map.put("userId", userId);
        Map<String, Object> userInfo = (Map<String, Object>) userConfig.userService.queryById(map);
        userInfos.put(userId, userInfo);
        return userInfo;
    }

    /**
     * 删除缓存的用户信息
     *
     * @param userIds
     */
    public static void removeUserInfo(String userIds) {
        if (!StringUtils.isEmpty(userIds)) {
            String[] split = userIds.split(",");
            for (String userId : split) {
                userInfos.remove(userId);
            }
        }
    }
}
