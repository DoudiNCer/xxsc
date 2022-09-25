package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.mood.PostMoodParam;
import com.sipc.xxsc.pojo.dto.param.mood.PutMoodParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.mood.MoodDetail;
import com.sipc.xxsc.pojo.dto.result.mood.MoodSummary;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MoodService {
    CommonResult getMoods(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<MoodDetail> getMood(HttpServletRequest request, HttpServletResponse response, Integer id);

    CommonResult<NoData> putMood(HttpServletRequest request, HttpServletResponse response, PutMoodParam param);

    CommonResult<NoData> postMood(HttpServletRequest request, HttpServletResponse response, PostMoodParam param);
}
