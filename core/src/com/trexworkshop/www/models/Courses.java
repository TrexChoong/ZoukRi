package com.trexworkshop.www.models;

/**
 * Created by Owner on 4/9/2016.
 */
public class Courses {
    int courseID = 0;
    String courseName = "";
    String courseDescription = "";
    String courseType = "";

    Courses (int id,String name,String descript,String type){
        courseID = id;
        courseName = name;
        courseDescription = descript;
        courseType = type;
    }
}
