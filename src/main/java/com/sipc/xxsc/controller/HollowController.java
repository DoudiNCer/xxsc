package com.sipc.xxsc.controller;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.hollow.PutCommentsParam;
import com.sipc.xxsc.pojo.dto.param.hollow.PutHollowParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.hollow.CommentResult;
import com.sipc.xxsc.pojo.dto.result.hollow.HollowResult;
import com.sipc.xxsc.service.HollowService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 树洞 Controller
 */
@RestController
@RequestMapping("/hollow")
public class HollowController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    HollowService hollowService;

    @GetMapping("/pages")
    CommonResult<Pages> getHollowPages(){
        return hollowService.getHollowPages(request, response);
    }

    @GetMapping("/hollows")
    CommonResult<List<HollowResult>> getHollows(
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ){
        return hollowService.getHollows(request, response, page);
    }

    @PutMapping("/hollow")
    CommonResult<NoData> putHollow(@Validated @RequestBody PutHollowParam param){
        return hollowService.putHollow(request, response, param);
    }

    @GetMapping("/comment")
    CommonResult<List<CommentResult>> getComents(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "storyId", defaultValue = "1") Integer storyId
    ){
        return hollowService.getComments(request, response, page, storyId);
    }

    @PutMapping("/comment")
    CommonResult<NoData> putComment(@Validated @RequestBody PutCommentsParam param){
        return hollowService.putComment(request, response, param);
    }
}
