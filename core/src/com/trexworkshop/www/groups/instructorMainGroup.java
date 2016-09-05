package com.trexworkshop.www.groups;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.trexworkshop.www.ZoukRI;
import com.trexworkshop.www.asset.Assets;
import com.trexworkshop.www.models.Courses;
import com.trexworkshop.www.models.userZouk;
import com.trexworkshop.www.screens.MainScreen;
import com.trexworkshop.www.screens.Screen;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Owner on 5/9/2016.
 */
public class instructorMainGroup extends Group{
    private newCourseGroup newCourse = null;

    public instructorMainGroup(){
        try {
            requestCourses();
        }
        catch(Exception ex) {
            System.out.println("Request failed :" + ex);
        }
        Assets.loadTextures("groups/instructorMain/PopUpBackground.png","common/Overlay.png",
                "common/TabUp.png");
        Assets.getManager().finishLoading();

        Screen screen = (Screen)ZoukRI.getInstance().getScreen();
        Image bg = new Image(Assets.getDrawable("groups/instructorMain/PopUpBackground.png"));
        bg.setPosition(screen.getWorldWidth()/2,screen.getWorldHeight()/2, Align.center);
        addActor(bg);

//        Image overlay = new Image(Assets.getDrawable("common/Overlay.png"));
//        overlay.setPosition(bg.getX()+50,bg.getY(Align.top)-150,Align.topLeft);
//        addActor(overlay);

        Image courseText = new Image(Assets.getTextDrawable("Courses",50,-1,false));
        courseText.setPosition(bg.getX()+50,bg.getY(Align.top)-150,Align.topLeft);
        addActor(courseText);

        Image courseAdd = new Image(Assets.getDrawable("common/TabUp.png"));
        courseAdd.setPosition(courseText.getX(Align.right)+100,courseText.getY());
        courseAdd.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newCourse = new newCourseGroup();
                addActor(newCourse);
            }
        });
        addActor(courseAdd);

        Image courseAddLabel = new Image(Assets.getTextDrawable("New",30,-1,false));
        courseAddLabel.setPosition(courseAdd.getX(Align.center),courseAdd.getY(Align.center),Align.center);
        courseAddLabel.setTouchable(Touchable.disabled);
        addActor(courseAddLabel);



    }

    public  void  requestCourses()  throws UnsupportedEncodingException
    {
        // Get user defined values
        String course  = "true";

        // Create data variable for sent values to server

        String data = URLEncoder.encode("course", "UTF-8")
                + "=" + URLEncoder.encode(course, "UTF-8");

        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://www.trexworkshop.com/zouk/zoukCourses.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        // Show response on activity
        Json jsonReceived= new Json();
        JsonValue courseArray = new JsonReader().parse(text);
        ArrayList<Courses> courseList = new ArrayList<>();
        for(JsonValue currentCourse : courseArray){
            Courses holder = jsonReceived.fromJson(Courses.class,currentCourse.toString());
            courseList.add(holder);
        }
        ZoukRI.getInstance().setFullCourses(courseList);
    }
}
