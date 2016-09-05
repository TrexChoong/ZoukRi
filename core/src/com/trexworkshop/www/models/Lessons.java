package com.trexworkshop.www.models;

import java.util.Date;

/**
 * Created by Owner on 4/9/2016.
 */
public class Lessons {
    int lessonID;
    int belongsToCourseID;
    String lessonTitle;
    String lessonSummary;
    Date lessonDate;

    Lessons (int id,int courseID,String title,String summary,Date date){
        lessonID = id;
        lessonTitle = title;
        lessonSummary = summary;
        lessonDate = date;
        belongsToCourseID = courseID;
    }
}
