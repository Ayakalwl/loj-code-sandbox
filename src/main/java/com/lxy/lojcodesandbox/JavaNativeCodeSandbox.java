package com.lxy.lojcodesandbox;

import com.lxy.lojcodesandbox.model.ExecuteCodeRequest;
import com.lxy.lojcodesandbox.model.ExecuteCodeResponse;

/**
 * Java 原生代码沙箱实现（直接服用模板方法）
 */
public class JavaNativeCodeSandbox extends JavaCodeSandboxTempPlate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
