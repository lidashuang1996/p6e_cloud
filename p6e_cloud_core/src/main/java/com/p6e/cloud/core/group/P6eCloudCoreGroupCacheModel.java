package com.p6e.cloud.core.group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class P6eCloudCoreGroupCacheModel {
    private int len;
    private int num;
    private ThreadPoolExecutor executor;
    private List<GroupArray> cache = new ArrayList<GroupArray>();

    public P6eCloudCoreGroupCacheModel(ThreadPoolExecutor executor) {
        this(500, 5, executor);
    }

    /**
     * 创建 P6eCloudCoreGroupCacheModel 对象
     * 用来缓存用户连接对象的模型
     * 在推送消息的使用会采用多线程方式快速推送
     * @param len 线程最大处理数
     * @param num 开始的线程数
     * @param executor 全局的线程池 【为了避免给每个组创建一个线程池，造成资源浪费】
     */
    public P6eCloudCoreGroupCacheModel(int len, int num, ThreadPoolExecutor executor) {
        this.len = len;
        this.num = num;
        this.executor = executor;
    }

    public synchronized void add(Object o) {
        boolean bool = false;
        for (GroupArray array : cache) if (bool = array.add(o)) break;
        if (!bool) {
            GroupArray groupArray = new GroupArray(this.len);
            if (groupArray.add(o)) cache.add(groupArray);
            else throw new RuntimeException(this.getClass().toString() + " ADD ERROR !!");
        }
    }

    public synchronized void del(Object o) {
        for (GroupArray array : cache) if (array.del(o)) break;
    }

    public synchronized void pushMessage(byte[] bytes) {
        int size = cache.size();
        for (int i = 0; i < (size > num ? num : size) ; i++) {
            executor.execute(new MessageHandle(i, num, cache, bytes));
        }
    }

    private static class MessageHandle extends Thread {
        private int i;
        private int num;
        private byte[] bytes;
        private List<GroupArray> cache;

        MessageHandle(int i, int num, List<GroupArray> cache, byte[] bytes) {
            this.i = i;
            this.num = num;
            this.cache = cache;
            this.bytes = bytes;
        }

        @Override
        public void run() {
            super.run();
            while (i < cache.size()) {
                i = i + num;
                for (Object o : cache.get(i).__objects__()) {
                    // 推送数据
                }
            }
        }
    }

    private static class GroupArray {
        private int len;
        private int size = 0;
        private int index = 0;
        private Object[] objects;

        GroupArray(int len) {
            this.len = len;
            this.objects = new Object[len];
        }

        int size() {
            return size;
        }

        boolean add(Object o) {
            if (size == len) return false;
            boolean bool = false;
            if (index < len) {
                if (objects[index] == null) {
                    bool = true;
                    objects[index++] = o;
                } else {
                    for (int i = index + 1; i < len; i++) {
                        if (objects[i] == null) {
                            index = i;
                            bool = true;
                            objects[index++] = o;
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < len; i++) {
                    if (objects[i] == null) {
                        index = i;
                        bool = true;
                        objects[index++] = o;
                        break;
                    }
                }
            }
            if (bool) size = size + 1;
            else size = len;
            return bool;
        }

        boolean del(Object o) {
            return false;
        }

        Object[] __objects__() {
            return objects;
        }


    }




}
