package com.example.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken  implements Serializable {
    String accessToken;// 令牌
    String refreshToken; // 刷新令牌
    String jti; // jwt短令牌
}
