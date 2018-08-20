package com.example.user.meet;

/**
 * Created by USER on 2018-07-09.
 */

public class GroupModel {
    private String group_name;
    private String tutor_id;

    public GroupModel(String group_name, String tutor_id) {
        this.group_name = group_name;
        this.tutor_id = tutor_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getTutor_id() {
        return tutor_id;
    }

    public void setTutor_id(String tutor_id) {
        this.tutor_id = tutor_id;
    }


}
