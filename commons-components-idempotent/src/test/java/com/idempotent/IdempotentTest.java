package com.idempotent;

import com.google.gson.Gson;
import com.idempotent.controller.IdempotentController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dingkeke
 * @date 2021-08-09 16:49
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class },scanBasePackages = "com.components.idempotent")
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class IdempotentTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 单线程测试
     * @throws Exception
     */
    @Test
    public void getOneThreadResutTest() throws Exception {
//        setUp();
        CountDownLatch countDownLatch=new CountDownLatch(4);
        for (int i=0;i<4;i++){
            int params=i%2;
            String p="key="+params;
            new Thread(new ApiThread(p,countDownLatch,"/get")).start();
        }
        countDownLatch.await();
    }


    /**
     * 多线程测试
     */
    @Test
    public  void getMultiThreadResutTest()  throws Exception{
        CountDownLatch countDownLatch=new CountDownLatch(4);
        for (int i=0;i<8;i++){
            int params1=i%2;
            int params2=i%3;
            String p="key1="+params1+"&key2="+params2;
            new Thread(new ApiThread(p,countDownLatch,"/muiltkey/get")).start();
        }
        countDownLatch.await();
    }

    /**
     * 多线程测试
     */
    @Test
    public  void postMultiThreadResutTest()  throws Exception{
        CountDownLatch countDownLatch=new CountDownLatch(8);
        for (int i=0;i<8;i++){
            int params1=i%2;
            int params2=i%3;
            String p="key=1";
            IdempotentController.TestRequest request=new IdempotentController.TestRequest(params1+"",params2+"");
            new Thread(new ApiThread(p,countDownLatch,"/muiltkey/post",request)).start();
        }
        countDownLatch.await();
    }


    /**
     * 多线程测试
     */
    @Test
    public  void postMultiThreadResutTestV1()  throws Exception{
        CountDownLatch countDownLatch=new CountDownLatch(8);
        for (int i=0;i<8;i++){
            int params1=i%2;
            int params2=i%3;
            String p="key=1";
            IdempotentController.TestRequest request=new IdempotentController.TestRequest(params1+"",params2+"");
            new Thread(new ApiThread(p,countDownLatch,"/muiltkey1/post",request)).start();
        }
        countDownLatch.await();
    }

    @Test
    public void getMultiThreadNoKeyResutTest()  throws Exception {

        CountDownLatch countDownLatch=new CountDownLatch(4);
        for (int i=0;i<4;i++){
            new Thread(new ApiThread(null,countDownLatch,"/noKey")).start();
        }
        countDownLatch.await();

    }



    class ApiThread implements Runnable{
        private String params;
        private CountDownLatch countDownLatch;
        private boolean isGet=true;
        private String url;
        private IdempotentController.TestRequest testRequest;

        public ApiThread(String params,CountDownLatch countDownLatch,String url){
            this.countDownLatch=countDownLatch;
            this.params=params;
            this.isGet=true;
            this.url=url;
        }

        public ApiThread(String params, CountDownLatch countDownLatch, String url, IdempotentController.TestRequest testRequest){
            this.countDownLatch=countDownLatch;
            this.params=params;
            this.url=url;
            this.isGet=false;
            this.testRequest=testRequest;
        }

        @Override
        public void run() {
            try {
                if (isGet) {
                    mockMvc.perform(get(url+"?" + params)).andExpect(status().isOk());
                }else{
                    if (Objects.nonNull(testRequest)){
                        Gson gson=new Gson();
                        String body=  gson.toJson(testRequest);
                        mockMvc.perform(post(url+"?" + params).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
                    }else {
                        mockMvc.perform(post(url + "?" + params)).andExpect(status().isOk());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                countDownLatch.countDown();
            }
        }
    }
}
