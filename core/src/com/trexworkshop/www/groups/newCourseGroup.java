package com.trexworkshop.www.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.trexworkshop.www.ZoukRI;
import com.trexworkshop.www.asset.Assets;
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

/**
 * Created by Owner on 5/9/2016.
 */
public class newCourseGroup extends Group {
    public newCourseGroup(){
        Assets.loadTextures("common/MessageBox.png","common/TabUp.png");
        Assets.getManager().finishLoading();

        final Screen screen = (Screen)ZoukRI.getInstance().getScreen();

        Image overlay = new Image(Assets.getDrawable("common/Overlay.png"));
        overlay.setPosition(screen.getWorldWidth()/2,screen.getWorldHeight()/2, Align.center);
        overlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.getStage().unfocusAll();
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        });
        addActor(overlay);


        Image bg = new Image(Assets.getDrawable("common/MessageBox.png"));
        bg.setPosition(screen.getWorldWidth()/2,screen.getWorldHeight()/2, Align.center);
        bg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.getStage().unfocusAll();
                Gdx.input.setOnscreenKeyboardVisible(false);
            }
        });
        addActor(bg);

        Image courseNewLabel = new Image(Assets.getTextDrawable("New Course",50,-1,false));
        courseNewLabel.setPosition(bg.getX()+50,bg.getY(Align.top)-100,Align.topLeft);
        courseNewLabel.setTouchable(Touchable.disabled);
        addActor(courseNewLabel);

        Image courseTitleLabel = new Image(Assets.getTextDrawable("Course Title",30,-1,false));
        courseTitleLabel.setPosition(courseNewLabel.getX(),courseNewLabel.getY()-50,Align.topLeft);
        courseTitleLabel.setTouchable(Touchable.disabled);
        addActor(courseTitleLabel);

        Image courseDescriptionLabel = new Image(Assets.getTextDrawable("Course Description",30,-1,false));
        courseDescriptionLabel.setPosition(courseNewLabel.getX(),courseTitleLabel.getY()-150,Align.topLeft);
        courseDescriptionLabel.setTouchable(Touchable.disabled);
        addActor(courseDescriptionLabel);

        Image courseTypeLabel = new Image(Assets.getTextDrawable("Course Type",30,-1,false));
        courseTypeLabel.setPosition(courseNewLabel.getX(),courseDescriptionLabel.getY()-150,Align.topLeft);
        courseTypeLabel.setTouchable(Touchable.disabled);
        addActor(courseTypeLabel);

        //inputs
        final VisTextField courseTitle = new VisTextField();
        courseTitle.setPosition(courseTitleLabel.getX()+400,courseTitleLabel.getY(Align.center),Align.left);
        addActor(courseTitle);

        final VisTextArea courseDescription = new VisTextArea();
        courseDescription.setSize(500,300);
        courseDescription.setPosition(courseTitle.getX(),courseDescriptionLabel.getY(Align.center),Align.left);
        addActor(courseDescription);

        final VisSelectBox courseType = new VisSelectBox();
        courseType.setItems("zouk","bachatta","salsa");
        courseType.setPosition(courseTitle.getX(),courseTypeLabel.getY(Align.center),Align.left);
        addActor(courseType);

        Image submit = new Image(Assets.getDrawable("common/TabUp.png"));
        submit.setPosition(bg.getX(Align.center)-100,bg.getY()+50,Align.bottomRight);
        submit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    submitCourse(courseTitle.getText(), courseDescription.getText(), courseType.getSelected().toString());
                    clear();
                }
                catch (Exception ex){
                    System.out.println("Submit failed :" + ex);
                }
            }
        });
        addActor(submit);

        Image cancel = new Image(Assets.getDrawable("common/TabUp.png"));
        cancel.setPosition(bg.getX(Align.center)+100,bg.getY()+50,Align.bottomLeft);
        cancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();
            }
        });
        addActor(cancel);

        Image submitLabel = new Image(Assets.getTextDrawable("Submit Course",30,-1,false));
        submitLabel.setPosition(submit.getX(Align.center),submit.getY(Align.center),Align.center);
        submitLabel.setTouchable(Touchable.disabled);
        addActor(submitLabel);

        Image cancelLabel = new Image(Assets.getTextDrawable("Cancel",30,-1,false));
        cancelLabel.setPosition(cancel.getX(Align.center),cancel.getY(Align.center),Align.center);
        cancelLabel.setTouchable(Touchable.disabled);
        addActor(cancelLabel);
    }

    public  void  submitCourse(String title, String descript, String type)  throws UnsupportedEncodingException
    {
        // Get user defined values
        String courseTitle = title;
        String courseDescription = descript;
        String courseType   = type;
        String courseNew = "true";

        // Create data variable for sent values to server

        String data = URLEncoder.encode("title", "UTF-8")
                + "=" + URLEncoder.encode(courseTitle, "UTF-8");

        data += "&" + URLEncoder.encode("description", "UTF-8")
                + "=" + URLEncoder.encode(courseDescription, "UTF-8");

        data += "&" + URLEncoder.encode("type", "UTF-8")
                + "=" + URLEncoder.encode(courseType, "UTF-8");

        data += "&" + URLEncoder.encode("courseNew", "UTF-8")
                + "=" + URLEncoder.encode(courseNew, "UTF-8");

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

        //System.out.println("new response:"+text);
        // Show response on activity
//        Json jsonReceived= new Json();
//        ZoukRI.getInstance().setMyUser(jsonReceived.fromJson(userZouk.class,text));
//        if(jsonReceived!=null){
//            ZoukRI.getInstance().getMyUser().setName(user);
//            ZoukRI.getInstance().setScreen(new MainScreen());
//        }

    }
}
