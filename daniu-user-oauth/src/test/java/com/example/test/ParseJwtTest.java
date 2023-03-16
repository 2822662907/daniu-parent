package com.example.test;


import org.junit.jupiter.api.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJkYW5pdSIsImlkIjoiMSJ9.MVKXDgfyRRLunc1_QPZPrp7yvKWMz5Twu9_Jt6I3yn3U4vdvShZ3ssxiRDAxAjiG3p8c54V0sxxLax1Aob-7ShhPDJHNoAA7Li85j8eC1_8QdzByqDjBz1FQT8XdDd9AWbp096Uvk5ctDxc0Y5lOsS-H89DD5FPba_8jzOjzrwAX6pWYxMrgQ0EfqRByiO8FuPCaCos4zB7Q3AgUWZmLOVFVNTRd-HApsen5yefuQiEPSKLWkdEOHysg4eRrO5t1eF5FepV3E7B42ZVTdPR2-2r6mWQHUpFXdrikchV-hdL9hyDfqlnL0BgRayi4sO0QldFCf7MWON6MbFoVngtplQ";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxxQ538mx/NxCfQP9cbt3/gOir+pI3Yjm5mJ+HbNMyo6hKFv8/M1+KxOP1FbPapt2VIPVNNxzyn4oOuG8d+anMIaPMaQUqz7SHIdKtefsd4o0EWCzc+MWd7kjag5jUF7hBP4GBPWbeQDval++6myY51Wo8I0GEcDtVmAG4u9aK1+LUPq4wABitw54LIKhKyyz1PeeL+HJWrOfd5afc3bUn0baSSgwwDa9ds2okAt6ybkiiEry0diOiVZAZuZgFoYI7fWpi2AI66OSXq6RLxV9S5f/iKHcQ4txqFhs0hEViNpWgVc0ZrrSlnNBZ76po0tgyfwBf3wv5XXPIOUalBcB7QIDAQAB-----END PUBLIC KEY-----";
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
