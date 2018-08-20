package com.example.user.meet;

/**
 * Created by 정연미 on 2018-08-20.
 */

public class BoardModel {
    public String board_name;//이름
    public String user_id;//이메일--인증부분의
    public String board_date;

    public BoardModel(String board_name, String user_id, String board_date) {
        this.board_name = board_name;
        this.user_id = user_id;
        this.board_date=board_date;
    }
}
