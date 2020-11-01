package com.rent.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.rent.Annotation.AuthToken;
import com.rent.entity.BaseAppUser;
import com.rent.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 拦截器
 * zyg  2020年8月30日
 */
public class TokenInterceptor implements HandlerInterceptor {
    Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    public static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    //存放鉴权信息的Header名称，默认是Authorization
    private String Authorization = "Authorization";
    //  private String Token = "token";
    //鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;
    /**
     * 存放用户名称和对应的key
     */
    public static final String USER_KEY = "USER_KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //验证token
        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            String token = request.getParameter(Authorization);
            //用于获取token信息 放入集合，方便以后随时获取
            requestThreadLocal.set(request);
            tokenThreadLocal.set(token);

            log.info("获取到的token为: {} ", token);
            //此处主要设置你的redis的ip和端口号，我的redis是在本地
            Jedis jedis = new Jedis("127.0.0.1", 6379);
            String userId = null;
            if (token != null && token.length() != 0) {
                //从redis中根据键token来获取绑定的username
                userId = jedis.get(token);
                log.info("从redis中获取的用户id为: {}", userId);
            }
            //判断username不为空的时候
            if (userId != null && !userId.trim().equals("")) {
                String startBirthTime = jedis.get(token + userId);
                log.info("生成token的时间为: {}", startBirthTime);
                Long time = System.currentTimeMillis() - Long.valueOf(startBirthTime);
                log.info("token存在时间为 : {} ms", time);
                //重新设置Redis中的token过期时间
                if (time > CommonUtil.TOKEN_RESET_TIME) {
                    jedis.expire(userId, CommonUtil.TOKEN_EXPIRE_TIME);
                    jedis.expire(token, CommonUtil.TOKEN_EXPIRE_TIME);
                    log.info("重置成功!");
                    Long newBirthTime = System.currentTimeMillis();
                    jedis.set(token + userId, newBirthTime.toString());
                }

                //关闭资源
                jedis.close();
                request.setAttribute(USER_KEY, userId);
                return true;
            } else {
                JSONObject jsonObject = new JSONObject();
                PrintWriter out = null;
                log.info("token 校验异常！");
                try {
                    response.setStatus(unauthorizedErrorCode);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    jsonObject.put("code", ((HttpServletResponse) response).getStatus());
                    //鉴权失败后返回的错误信息，默认为401 unauthorized
                    jsonObject.put("reason", HttpStatus.UNAUTHORIZED);
                    jsonObject.put("message","token---->undefined！");
                    out = response.getWriter();
                    out.println(jsonObject);

                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                }

            }

        }
        request.setAttribute(USER_KEY, null);
        return true;
    }

    /**
     * 获取token信息
     * 用于从token中获取用户信息
     * 朱阳刚  2020.09.01
     *
     * @return
     */
    public static String getToken() {
        return tokenThreadLocal.get();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
