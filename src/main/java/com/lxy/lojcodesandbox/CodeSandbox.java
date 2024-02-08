package com.lxy.lojcodesandbox;

import com.lxy.lojcodesandbox.model.ExecuteCodeRequest;
import com.lxy.lojcodesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    /**
     * 执行方法
     *
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}