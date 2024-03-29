package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.mood.PostMoodParam;
import com.sipc.xxsc.pojo.dto.param.mood.PutMoodParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.mood.MoodDetailResult;
import com.sipc.xxsc.pojo.dto.result.mood.MoodSummaryResult;
import com.sipc.xxsc.service.MoodService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/tdmd")
public class MoodController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    MoodService moodService;

    @GetMapping("/moods")
    public CommonResult<List<MoodSummaryResult>> getMoods(
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ){
        return moodService.getMoods(request, response, page);
    }

    @GetMapping("/mood")
    public CommonResult<MoodDetailResult> getMood(@RequestParam(name = "id", defaultValue = "1") Integer id){
        return moodService.getMood(request, response, id);
    }

    @GetMapping("/todayMood")
    public CommonResult<MoodDetailResult> getTodayMood(){
        return moodService.getTodayMood(request, response);
    }

    @PutMapping("/mood")
    public CommonResult<NoData> putMood(@Validated @RequestBody PutMoodParam param){
        return moodService.putMood(request, response, param);
    }

    @PostMapping("/mood")
    public CommonResult<NoData> postMood(@Validated @RequestBody PostMoodParam param){
        return moodService.postMood(request, response, param);
    }
    @GetMapping("/moodPages")
    public CommonResult<Pages> getMoodPages(){
        return moodService.getMoodPages(request, response);
    }
}
