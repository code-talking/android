package com.business.e.bl.comassigntwo;

public class Config {

    static final String IP = "192.168.1.199";

    static final String PORT = "8080";

    static final String HTTPBEGIN = "http://";
    static final String SEMICOLON = ":";
    static final String HTTPEND = "/pic/random";

    static final String UNSPLASH = "https://source.unsplash.com/random";


    static String getRequestURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(HTTPBEGIN);
        sb.append(IP);
        sb.append(SEMICOLON);
        sb.append(PORT);
        sb.append(HTTPEND);

//        System.out.println("url : " + sb.toString());

        return sb.toString();
    }

    static String getUnSplashPic() {
        return UNSPLASH;
    }
}
