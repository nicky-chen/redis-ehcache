package com.nicky;

import com.nicky.cache.redis.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisEhcacheApplicationTests {

	@Autowired
    private RedisClient redisClient;

    @Test
    public void test() {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10 ; i++) {
            pool.execute(() -> {
                long time = Instant.now().toEpochMilli();
                System.out.println("start ====" +Instant.now().toEpochMilli());
                boolean result = redisClient.tryGetDistributedLock("redisaa", time+ "::" + Thread.currentThread().getId(), 1000 * 60);
                System.out.println("end ===" + result);
                System.err.println("eeee :" + redisClient.get("redisaa"));
            });
        }
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                System.err.println("result :" + redisClient.get("redisaa"));
                break;
            }
        }






    }

}
