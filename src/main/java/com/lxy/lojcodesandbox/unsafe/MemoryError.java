package com.lxy.lojcodesandbox.unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限睡眠（阻塞程序执行）
 */
public class MemoryError {

    public static void main(String[] args) throws InterruptedException {
        List<byte[]> bytes = new ArrayList<>();
        while (true) {
            bytes.add(new byte[10000]);
        }
    }
}