package com.harvey.system.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Harvey
 * @date 2024-11-25 09:46
 **/
@Data
@Builder
public class VisitStatsVO {

    private String type;

    private String title;

    private int todayCount;

    private int totalCount;

    private BigDecimal growthRate;

    private String granularityLabel;
}
