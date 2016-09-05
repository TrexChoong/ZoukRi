package com.trexworkshop.www.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Created by Owner on 3/9/2016.
 */
public class userZouk {
    @Setter @Getter
    int ID = 0;
    @Setter @Getter
    String name = "";
    @Setter @Getter
    int level = 0 ;
    @Setter @Getter
    int userID = 0;
    @Setter @Getter
    int isInstructor = 0;
    @Setter @Getter
    String lessonsAttended = "";
    @Setter @Getter
    String coursesEnrolled = "";

    public void userZouk(int ID, String name,int level,int userID, String lessons){
        this.ID = ID;
        this.name = name;
        lessonsAttended = lessons;
        this.level = level;
        this.userID = userID;
    }
}
