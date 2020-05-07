package com.p6e.cloud.core.group;

import com.p6e.cloud.netty.P6eCloudNettyClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

class P6eCloudCoreGroupCacheModel {
    private int len;
    private int num;
    private ThreadPoolExecutor executor;
    private List<GroupArray> cache = new ArrayList<>();

    P6eCloudCoreGroupCacheModel(ThreadPoolExecutor executor) {
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
    P6eCloudCoreGroupCacheModel(int len, int num, ThreadPoolExecutor executor) {
        this.len = len;
        this.num = num;
        this.executor = executor;
    }

    synchronized int[] add(P6eCloudNettyClient client) {
        int[] r = new int[] {-1, -1};
        for (int i = 0; i < cache.size(); i++) {
            if ((r[1] = cache.get(i).add(client)) >= 0) {
                r[0] = i;
                break;
            }
        }
        if (r[1] < 0) {
            GroupArray groupArray = new GroupArray(this.len);
            if ((r[1] = groupArray.add(client)) >= 0) {
                cache.add(groupArray);
                r[0] = cache.size() - 1;
            } else throw new RuntimeException(this.getClass().toString() + " ADD ERROR !!");
        }
        return r;
    }

    synchronized void del(int[] coordinate) {
        cache.get(coordinate[0]).del(coordinate[1]);
    }

    public List<GroupArray> cache() {
        return cache;
    }

    synchronized void pushMessage(String message) {
        int size = cache.size();
        for (int i = 0; i < (size > num ? num : size) ; i++) {
            executor.execute(new MessageHandle(i, num, cache, message));
        }
    }

    private static class MessageHandle extends Thread {
        private int i;
        private int num;
        private String message;
        private List<GroupArray> cache;

        MessageHandle(int i, int num, List<GroupArray> cache, String message) {
            this.i = i;
            this.num = num;
            this.cache = cache;
            this.message = message;
        }

        @Override
        public void run() {
            super.run();
            while (i < cache.size()) {
                for (P6eCloudNettyClient client : cache.get(i).__clients__()) {
                    if (client != null) client.sendMessage(message);
                }
                i = i + num;
            }
        }
    }

    private static class GroupArray {
        private int len;
        private int size = 0;
        private int index = 0;
        private P6eCloudNettyClient[] clients;

        GroupArray(int len) {
            this.len = len;
            this.clients = new P6eCloudNettyClient[len];
        }

        int size() {
            return size;
        }

        int add(P6eCloudNettyClient client) {
            if (size == len) return -1;
            int r = -1;
            if (index < len) {
                if (clients[index] == null) {
                    r = index;
                    clients[index++] = client;
                } else {
                    for (int i = index + 1; i < len; i++) {
                        if (clients[i] == null) {
                            r = i;
                            index = i;
                            clients[index++] = client;
                            break;
                        }
                    }
                    if (r == -1) {
                        for (int i = 0; i < index; i++) {
                            if (clients[i] == null) {
                                r = i;
                                index = i;
                                clients[index++] = client;
                                break;
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < len; i++) {
                    if (clients[i] == null) {
                        r = i;
                        index = i;
                        clients[index++] = client;
                        break;
                    }
                }
            }
            if (r >= 0) size = size + 1;
            else {
                index = 0;
                size = len;
            }
            return r;
        }

        void del(int index) {
            clients[index] = null;
        }

        P6eCloudNettyClient[] __clients__() {
            return clients;
        }

    }
}
