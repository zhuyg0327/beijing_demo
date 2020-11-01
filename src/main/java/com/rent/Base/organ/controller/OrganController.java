package com.rent.Base.organ.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rent.Annotation.AuthToken;
import com.rent.Base.organ.entity.Organ;
import com.rent.Base.organ.service.OrganService;
import com.rent.Base.user.UserConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AuthToken
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/organ")
public class OrganController {
    @Autowired
    OrganService organService;

    /**
     * 加载组织机构树
     *
     * @param request
     * @return
     */
    @RequestMapping({"/tree"})
    @ResponseBody
    public Object getDeptTree(HttpServletRequest request) {
        String orgid = UserConfig.getUserInfo().getDeptId();
        if (StringUtils.isNotEmpty(orgid)) {
            JSONObject jSONObject1 = getDeptTree(orgid, false);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("opened", Boolean.valueOf(true));
            jSONObject1.put("state", jSONObject2);
            return jSONObject1;
        }
        JSONObject list = getDeptTree("root", false);
        JSONObject json = new JSONObject();
        json.put("opened", Boolean.valueOf(true));
        list.put("state", json);
        return list;
    }


    public JSONObject getDeptTree(String id, boolean isRootOnly) {
        JSONObject result = new JSONObject();
        JSONArray jsons = new JSONArray();
        Organ department = organService.queryObject(id);
        if (department != null) {
            result.put("id", department.getId());
            result.put("text", department.getName());
            result.put("type", "0");
        }
        List<Organ> depts = organService.findByParentId(id);
        if (depts != null && depts.size() > 0)
            for (Organ dept : depts) {
                JSONObject json = new JSONObject();
                json.put("id", dept.getId());
                json.put("text", dept.getName());
                json.put("type", "0");
                if (!isRootOnly) {
                    jsons.add(getDeptTree(dept.getId(), false));
                    continue;
                }
                jsons.add(json);
            }
        if (jsons.size() > 0)
            result.put("children", jsons);
        return result;
    }
}
