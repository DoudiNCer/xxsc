package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.mood.PostMoodParam;
import com.sipc.xxsc.pojo.dto.param.mood.PutMoodParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.mood.MoodDetailResult;
import com.sipc.xxsc.pojo.dto.result.mood.MoodSummaryResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MoodService {
    CommonResult<List<MoodSummaryResult>> getMoods(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<MoodDetailResult> getMood(HttpServletRequest request, HttpServletResponse response, Integer id);

    CommonResult<NoData> putMood(HttpServletRequest request, HttpServletResponse response, PutMoodParam param);

    CommonResult<NoData> postMood(HttpServletRequest request, HttpServletResponse response, PostMoodParam param);

    CommonResult<Pages> getMoodPages(HttpServletRequest request, HttpServletResponse response);

    CommonResult<MoodDetailResult> getTodayMood(HttpServletRequest request, HttpServletResponse response);
}
