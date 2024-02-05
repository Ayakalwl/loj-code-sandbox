package com.lxy.lojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.lxy.lojcodesandbox.model.ExecuteCodeRequest;
import com.lxy.lojcodesandbox.model.ExecuteCodeResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JavaNativeCodeSandbox implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);

    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 2. 编译diamante，得到 class 文件
        String compileCmd = String.format("javac -encoding utf-8%s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            // 等待程序执行，获取错误码
            int exitValue = compileProcess.waitFor();
            // 正常退出
            if (exitValue == 0) {
                System.out.println("编译成功");
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutputLine);
                }
                System.out.println(compileOutputStringBuilder);
            } else {
                // 异常退出
                System.out.println("编译失败，错误码： " + exitValue);
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutputLine);
                }
                // 分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                StringBuilder errorCompileOutputStringBuilder = new StringBuilder();

                // 逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorCompileOutputStringBuilder.append(errorCompileOutputLine);
                }
                System.out.println(compileOutputStringBuilder);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
