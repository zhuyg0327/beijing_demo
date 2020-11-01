package com.rent.controller;

import com.rent.Annotation.AuthToken;
import com.rent.entity.Leader;
import com.rent.service.LeaderService;
import com.rent.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AuthToken
@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/api/leader")
public class LeaderController {
    @Autowired
    LeaderService leaderService;
    private static final Logger logger = LoggerFactory.getLogger(LeaderController.class);

    /**
     * 查询领导type值是1和type值是2的数量
     *
     * @throws ParseException
     */
    @RequestMapping("/queryCounts")
    public void queryCounts() {
        List<Leader> leaders = leaderService.queryAll();
        int overType1 = 0;
        int overType2 = 0;
        if (leaders.size() > 0 && leaders != null) {
            for (Leader leader : leaders) {
                if (leader.getType() == 1) {
                    overType1++;
                } else {
                    overType2++;
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("overType1", overType1);
        map.put("overType2", overType2);
        Response.json(map);
    }

}
