package com.itheima.test;

import com.itheima.reggie.ReggieApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = ReggieApplication.class)
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作 String 类型数据
     */
    @Test
    public void testString(){
        redisTemplate.opsForValue().set("city123","beijing");

        String value = (String)redisTemplate.opsForValue().get("city123");
        System.out.println(value);

        redisTemplate.opsForValue().set("key1", "value1", 10l, TimeUnit.SECONDS);

        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city1234", "nanjing");
        System.out.println(aBoolean);
    }

    /**
     * 操作 hash 类型数据
     */
    @Test
    public void hashTest(){
        HashOperations hashOperations = redisTemplate.opsForHash();

        hashOperations.put("002", "name", "xiaoming");       //存值
        hashOperations.put("002", "age", "20");
        hashOperations.put("002", "address", "beijing");

        String age = (String) hashOperations.get("002", "age");    //取值
        System.out.println(age);

        // 获取 hash 结构中的所有字段
        Set keys = hashOperations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        // 获取 hash 结构中的所有值
        List values = hashOperations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作 list 类型数据
     */
    @Test
    public void listTest(){
        ListOperations listOperations = redisTemplate.opsForList();

        // 存值
        listOperations.leftPush("mylist", "a");
        listOperations.leftPushAll("mylist", "b","c","d");

        // 取值
        List<String> mylist = listOperations.range("mylist", 0, -1);
        for (String value : mylist) {
            System.out.println(value);
        }

        // 获取列表长度 llen
        Long size = listOperations.size("mylist");
        int lSize = size.intValue();
        for(int i = 0; i < lSize; i++){
            // 出队列
            String element = (String)listOperations.rightPop("mylist");
            System.out.println(element);
        }
    }

    /**
     * 操作 set 类型数据
     */
    @Test
    public void setTest(){
        SetOperations setOperations = redisTemplate.opsForSet();

        // 存值
        setOperations.add("myset", "a","b","a");

        // 取值
        Set<String> myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }

        // 删除成员
        setOperations.remove("myset", "a","b");

        // 取值
        myset = setOperations.members("myset");
        for (String o : myset) {
            System.out.println(o);
        }
    }

    /**
     * 操作 Zset 类型数据
     */
    @Test
    public void testZset(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        // 存值
        zSetOperations.add("myzset", "a",10.0);
        zSetOperations.add("myzset", "b",11.0);
        zSetOperations.add("myzset", "c",12.0);
        zSetOperations.add("myzset", "a",13.0);

        // 取值
        Set<String> myzset = zSetOperations.range("myzset", 0, -1);
        for (String s : myzset) {
            System.out.println(s);
        }

        // 修改分数
        zSetOperations.incrementScore("myzset", "b", 20.0);
        //取值
        myzset = zSetOperations.range("myzset", 0, -1);
        for (String s : myzset) {
            System.out.println(s);
        }

        //删除成员
        zSetOperations.remove("myzset", "a","b");

        // 取值
        myzset = zSetOperations.range("myzset", 0, -1);
        for (String s : myzset) {
            System.out.println(s);
        }
    }

    /**
     * 通用操作, 针对不同的数据类型都可以操作
     */
    @Test
    public void commonTest(){
        // 获取 Redis 中所有的 key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        // 判断某个 key 是否存在
        Boolean itcast = redisTemplate.hasKey("itcast");
        System.out.println(itcast);

        // 删除指定 key
        Boolean delete = redisTemplate.delete("myzset");
        System.out.println("delete opoeration is --" + delete);

        // 获取指定 key 对应的 value 的数据类型
        DataType dataType = redisTemplate.type("myset");
        System.out.println(dataType);
    }
}
