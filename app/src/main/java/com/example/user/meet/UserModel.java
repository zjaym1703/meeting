package com.example.user.meet;

/**
 * Created by 정연미 on 2018-08-12.
 */

public class UserModel {
    public String user_name;//이름
    public String user_pos;//신분
    public String user_email;//이메일--인증부분의
    public String user_image;

    public UserModel(String user_name, String user_email/*, String user_image*/) {
        this.user_name = user_name;
        this.user_email = user_email;
       // this.user_image = user_image;
    }
}
