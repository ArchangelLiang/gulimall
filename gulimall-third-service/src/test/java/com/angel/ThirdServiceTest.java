package com.angel;

import com.aliyun.oss.OSSClient;
import com.angel.third.ThirdServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = ThirdServiceApplication.class)
public class ThirdServiceTest {

    @Autowired
    private OSSClient ossClient;

    @Test
    public void testOssClient() throws FileNotFoundException {
        String bucketName = "gulimall-two";
        FileInputStream inputStream = new FileInputStream("D:\\GoogleDown\\ardor.png");
        ossClient.putObject(bucketName,"ardor.jpg",inputStream);
    }

}
