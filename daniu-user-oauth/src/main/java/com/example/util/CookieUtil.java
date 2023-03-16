package com.example.util;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class CookieUtil  {

    /**
     * 设置cookie
     *
     * @param response
     * @param name     cookie名字
     * @param value    cookie值
     * @param maxAge   cookie生命周期 以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String domain, String path
                                 , String name, String value, int maxAge, boolean httpOnly){
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

        /**
         * 根据cookie名称读取cookie
         * @param request
         * @return map<cookieName,cookieValue>
         */
          public static Map<String,String> readCookie(HttpServletRequest request,String ... cookieNmaes){
              Map<String, String> cookieMap = new HashMap<>();
              Cookie[] cookies = request.getCookies();
              if (cookies!=null){
                  for (Cookie cookie : cookies) {
                      String name = cookie.getName();
                      String value = cookie.getValue();

                      for (int i = 0; i < cookieNmaes.length; i++) {
                          if (cookieNmaes[i].equals(name)){
                              cookieMap.put(name,value);
                          }
                      }
                  }
              }
              return cookieMap;
          }
}
