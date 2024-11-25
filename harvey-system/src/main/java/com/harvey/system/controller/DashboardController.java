package com.harvey.system.controller;

import com.harvey.system.base.RespResult;
import com.harvey.system.model.vo.VisitStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harvey
 * @date 2024-11-25 09:56
 **/
@Tag(name = "仪表板")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    @Operation(summary = "访问趋势")
    @GetMapping("/visit-trend")
    public RespResult<Map<String, Object>> visitTrend() {
        List<String> dates = Arrays.asList("2024-06-30", "2024-07-01", "2024-07-02", "2024-07-03", "2024-07-04", "2024-07-05", "2024-07-06", "2024-07-07");
        List<Integer> pvList = Arrays.asList(1751, 5168, 4882, 5301, 4721, 4885, 1901, 1003);
        List<Integer> ipList = Arrays.asList(207, 566, 565, 631, 579, 496, 222, 152);
        Map<String, Object> map = new HashMap<>();
        map.put("dates", dates);
        map.put("pvList", pvList);
        map.put("uvList", null);
        map.put("ipList", ipList);
        return RespResult.success(map);
    }

    @Operation(summary = "访问统计")
    @GetMapping("/visit-stats")
    public RespResult<List<VisitStatsVO>> visitStats() {
        VisitStatsVO visitStatsPv = VisitStatsVO.builder().type("pv").title("浏览量").todayCount(1003).totalCount(36193).growthRate(new BigDecimal("-0.35")).granularityLabel("日").build();
        VisitStatsVO visitStatsUv = VisitStatsVO.builder().type("uv").title("访客数").todayCount(100).totalCount(2000).growthRate(new BigDecimal("0")).granularityLabel("日").build();
        VisitStatsVO visitStatsIp = VisitStatsVO.builder().type("ip").title("IP数").todayCount(152).totalCount(3234).growthRate(new BigDecimal("-0.2")).granularityLabel("日").build();
        return RespResult.success(Arrays.asList(visitStatsPv, visitStatsUv, visitStatsIp));
    }
}
