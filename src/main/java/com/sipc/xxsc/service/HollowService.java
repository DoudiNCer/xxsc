package com.sipc.xxsc.service;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.hollow.PutCommentsParam;
import com.sipc.xxsc.pojo.dto.param.hollow.PutHollowParam;
import com.sipc.xxsc.pojo.dto.result.NoData;
import com.sipc.xxsc.pojo.dto.result.Pages;
import com.sipc.xxsc.pojo.dto.result.hollow.CommentResult;
import com.sipc.xxsc.pojo.dto.result.hollow.HollowResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HollowService {
    CommonResult<Pages> getHollowPages(HttpServletRequest request, HttpServletResponse response);

    CommonResult<NoData> putHollow(HttpServletRequest request, HttpServletResponse response, PutHollowParam param);
    
    CommonResult<NoData> putComment(HttpServletRequest request, HttpServletResponse response, PutCommentsParam param);

    CommonResult<List<HollowResult>> getHollows(HttpServletRequest request, HttpServletResponse response, Integer page);

    CommonResult<List<CommentResult>> getComments(HttpServletRequest request, HttpServletResponse response, Integer page, Integer storyId);
}
