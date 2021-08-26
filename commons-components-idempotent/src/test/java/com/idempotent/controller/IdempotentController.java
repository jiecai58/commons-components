package com.idempotent.controller;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Dingkeke
 * @date 2021-08-09 16:49
 */
@RestController
@Slf4j
public class IdempotentController {
    @GetMapping("/get")
    @Idempotent(keys = {"#key"}, expireTime = 3, info = "请勿重复查询")
    public String get(String key) throws Exception {
        Thread.sleep(50L);
        log.info("get/key[{}]",key);
        return "success";
    }

    @GetMapping("/muiltkey/get")
    @Idempotent(keys = {"#key1","#key2"}, expireTime = 3, info = "请勿重复查询")
    public String muiltGet(String key1,String key2) throws Exception {
        Thread.sleep(50L);
        log.info("get/key1[{}]-key2[{}]",key1,key2);
        return "success";
    }

    @PostMapping("/muiltkey/post")
    @Idempotent(keys = {"#key","#req.key1","#req.key2"}, expireTime = 3, info = "请勿重复查询")
    public String muiltPost(@RequestParam  String key, @RequestBody TestRequest req) throws Exception {
        Thread.sleep(50L);
        log.info("post/key[{}],req[{}]",key,req);
        return "success";
    }
    @PostMapping("/muiltkey1/post")
    @Idempotent( expireTime = 3, info = "请勿重复查询")
    public String muiltPostV2(@RequestParam String key,@RequestBody  TestRequest req) throws Exception {
        Thread.sleep(50L);
        log.info("post1/key[{}],req[{}]",key,req);
        return "success";
    }


    @GetMapping("/noKey")
    @Idempotent(expireTime = 3, info = "请勿重复查询")
    public String noKey() throws Exception {
        Thread.sleep(50L);
        log.info("noKey");
        return "success";
    }

@ToString
  public static class  TestRequest{
        String key1;
        String key2;

        public TestRequest(String key1,String key2){
            this.key1=key1;
            this.key2=key2;
        }

       public String getKey1() {
           return key1;
       }

       public String getKey2() {
           return key2;
       }

       public void setKey1(String key1) {
           this.key1 = key1;
       }

       public void setKey2(String key2) {
           this.key2 = key2;
       }

      @Override
      public String toString() {
          return "TestRequest{" +
                  "key1='" + key1 + '\'' +
                  ", key2='" + key2 + '\'' +
                  '}';
      }
  }

}
