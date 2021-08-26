package com.catchlog.test;

import com.catchlog.CatchLogAspect;
import com.catchlog.CatchLogAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CatchLogAutoConfiguration.class, com.catchlog.test.Demo.class, CatchLogAspect.class})
public class CatchLogTest {

    @Resource
    private com.catchlog.test.Demo demo;

    @Test
    public void testAspect() {
        demo.doSomething();
    }

    @Test
    public void testCatchAndLog() {
        com.catchlog.test.Demo.Request request = new com.catchlog.test.Demo.Request();
        request.name = "Frank";
        request.age = 18;
        demo.execute(request);
    }

    @Test
    public void testExecuteWithResponse(){
        com.catchlog.test.Demo.Request request = new com.catchlog.test.Demo.Request();
        request.name = "Frank";
        request.age = 18;
        demo.executeWithResponse(request);
    }

    @Test
    public void testExecuteWithVoid(){
        demo.executeWithVoid();
    }

    @Test
    public void testExecuteWithExceptionAndVoid(){
        demo.executeWithExceptionAndVoid();
    }

    @Test
    public void testExecuteWithExceptionAndDemoResponse(){
        demo.executeWithExceptionAndDemoResponse();
    }

    @Test
    public void testExecuteWithBizExceptionAndResponse(){
        demo.executeWithBizExceptionAndResponse();
    }

    @Test
    public void testExecuteWithSysExceptionAndResponse(){
        demo.executeWithSysExceptionAndResponse();
    }

    @Test
    public void testExecuteWithExceptionAndResponse(){
        demo.executeWithExceptionAndResponse();
    }

}
