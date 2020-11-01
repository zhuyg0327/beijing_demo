package com.rent.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rent.Annotation.AuthToken;
import com.rent.Base.Md5TokenGenerator;
import com.rent.Base.organ.entity.Organ;
import com.rent.Base.organ.service.OrganService;
import com.rent.Base.user.UserConfig;
import com.rent.entity.BaseAppUser;
import com.rent.entity.User;
import com.rent.service.BaseAppUserService;
import com.rent.service.UserService;
import com.rent.util.CommonUtil;
import com.rent.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/user")
public class BaseAppUserController {
    @Autowired
    BaseAppUserService baseAppUserService;
    @Autowired
    private Md5TokenGenerator tokenGenerator;
    @Autowired
    UserService userService;
    @Autowired
    OrganService organService;

    private static final Logger logger = LoggerFactory.getLogger(BaseAppUserController.class);

    /**
     * 根据账号、密码以及用户身份登录
     *
     * @param account
     * @param password
     * @param power
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void login(String account, String password, Integer power) {
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password) || power == null) {
            Response.json("result", "账号密码不能为空！");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("password", password);
        map.put("power", power);
        BaseAppUser user = baseAppUserService.login(map);
        if (user != null) {
            String token = SetRedisData(user.getUserId(), password);
            Response.result(200, "登录成功", token);
        } else {
            Response.json("result", "登录失败");
        }
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @AuthToken
    public void test() {
        logger.info("**************测试start**************");
        User userinfo = UserConfig.getUserInfo();
        Response.json(userinfo);
    }

    /**
     * 在redis中进行数据的绑定
     *
     * @param userId
     * @param password
     * @return
     * @Title: SetRedisData
     * @Description: TODO
     * @author zyg  2020年8月30日
     */
    private String SetRedisData(String userId, String password) {
        //此处主要设置你的redis的ip和端口号，我的redis是在本地
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String token = tokenGenerator.generate(userId, password);
        jedis.set(userId, token);
        //设置key过期时间，到期会自动删除
        jedis.expire(userId, CommonUtil.TOKEN_EXPIRE_TIME);
        //将token和userid以键值对的形式存入到redis中进行双向绑定
        jedis.set(token, userId);
        jedis.expire(token, CommonUtil.TOKEN_EXPIRE_TIME);
        Long currentTime = System.currentTimeMillis();
        jedis.set(token + userId, currentTime.toString());
        //用完关闭
        jedis.close();
        return token;
    }

    /**
     * 加载人员树
     *
     * @param orgid
     * @param request
     * @return
     */
    @AuthToken
    @RequestMapping({"/tree"})
    @ResponseBody
    public Object getUserTree(String orgid, HttpServletRequest request) {
        if (StringUtils.isEmpty(orgid)) {
            orgid = UserConfig.getUserInfo().getDeptId();
        }
        if (StringUtils.isNotEmpty(orgid)) {
            JSONObject jSONObject1 = getUserTree(orgid);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("opened", Boolean.valueOf(true));
            jSONObject1.put("state", jSONObject2);
            return jSONObject1;
        }
        JSONObject list = getUserTree("root");
        JSONObject json = new JSONObject();
        json.put("opened", Boolean.valueOf(true));
        list.put("state", json);
        return list;
    }

    public JSONObject getUserTree(String id) {
        JSONObject result = new JSONObject();
        JSONArray jsons = new JSONArray();
        Organ dept = organService.queryObject(id);
        result.put("id", dept.getId());
        result.put("text", dept.getName());
        result.put("type", "0");
        List<BaseAppUser> sysUsers = this.baseAppUserService.findByOrganid(id);
        for (BaseAppUser sysUser : sysUsers) {
            if (!StringUtils.contains("admin,sysadmin,secadmin,audadmin", sysUser.getAccount())) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", sysUser.getUserId());
                jsonUser.put("text", sysUser.getUserName());
                jsonUser.put("type", "1");
                jsonUser.put("deptid", sysUser.getDeptId());
                jsonUser.put("sex", sysUser.getSex());
                jsons.add(jsonUser);
            }
        }
        List<Organ> organs = organService.findByParentId(id);
        for (Organ organ : organs) {
            JSONObject json = new JSONObject();
            json.put("id", organ.getId());
            json.put("text", organ.getName());
            json.put("type", "0");
            jsons.add(getUserTree(organ.getId()));
        }
        if (jsons.size() > 0)
            result.put("children", jsons);
        return result;
    }

    @RequestMapping({"/insertBatch"})
    @ResponseBody
    public void insertBatch() {
        List<BaseAppUser> user1 = baseAppUserService.queryAll();
        List<User> user2 = new ArrayList<>();
        List<User> users = userService.queryAll();
        if (users.size() > 0 && users != null) {
            userService.deleteAll();
        }

        if (user1.size() > 0) {
            for (BaseAppUser baseUser : user1) {
                User user =new User();
                user.setId(baseUser.getId());
                user.setAccount(baseUser.getAccount());
                user.setDeptId(baseUser.getDeptId());
                user.setDeptName(baseUser.getDeptName());
                user.setUserId(baseUser.getUserId());
                user.setUserName(baseUser.getUserName());
                user.setPower(baseUser.getPower());
                user.setSex(baseUser.getSex());
                user.setStatus(baseUser.getStatus());
                user.setSort(baseUser.getSort());
                user.setTelephone(baseUser.getTelephone());
                user.setUserPassword(baseUser.getUserPassword());
                user2.add(user);
            }
            userService.insertBatch(user2);
            Response.json("result","success");
        }
    }
}
