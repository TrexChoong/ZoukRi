package com.trexworkshop.www.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.trexworkshop.www.ZoukRI;
import com.trexworkshop.www.asset.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trexworkshop.www.groups.instructorMainGroup;
import com.trexworkshop.www.models.Courses;
import com.trexworkshop.www.models.userZouk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Owner on 4/9/2016.
 */
public class MainScreen extends Screen {
    userZouk myUser = ZoukRI.getInstance().getMyUser();
    instructorMainGroup instructorMain = null;

    public MainScreen(){
        Assets.loadTextures("main/BG.png", "main/DefaultPlayerAvatar.png",
                "main/ContinueButtonDisable.png","common/whiteBox.png");
        Assets.getManager().finishLoading();

        Image bg = new Image(Assets.getDrawable("main/BG.png"));
        bg.setPosition(getWorldWidth()/2,getWorldHeight()/2, Align.center);
        addActor(bg);

        Image playerIcon = new Image(Assets.getDrawable("main/DefaultPlayerAvatar.png"));
        playerIcon.setPosition(bg.getX()+150,bg.getY(Align.top)-100, Align.topLeft);
        addActor(playerIcon);

        Image playerName = new Image(Assets.getTextDrawable(myUser.getName(),50,-1,false));
        playerName.setPosition(playerIcon.getX(Align.right)+50,playerIcon.getY());
        addActor(playerName);

        if(myUser.getIsInstructor()!=0) {
            Image instructorOption = new Image(Assets.getDrawable("main/ContinueButtonDisable.png"));
            instructorOption.setPosition(bg.getX(Align.right) - 200, bg.getY(Align.top) - 100, Align.topRight);
            instructorOption.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    instructorMain = new instructorMainGroup();
                    addActor(instructorMain);
                }
            });
            addActor(instructorOption);

            Image instructorOptionLabel = new Image(Assets.getTextDrawable("Instructor Menu",20,-1,false));
            instructorOptionLabel.setPosition(instructorOption.getX(Align.center),instructorOption.getY(Align.center),Align.center);
            instructorOptionLabel.setTouchable(Touchable.disabled);
            addActor(instructorOptionLabel);
        }
        try{
            fetchCourses();
        }
        catch(Exception ex)
        {
            System.out.println(" url exeption! "+ex.getMessage() );
        }



    }

    protected void update(float delta) {
    }

    @Override
    protected boolean processBackKey() {
        ZoukRI.getInstance().setScreen(ZoukRI.getInstance().getLoginScreen());
        return false;
    }

    public void fetchCourses() throws UnsupportedEncodingException {
        String data = URLEncoder.encode("course", "UTF-8")
                + "=" + URLEncoder.encode("true", "UTF-8");

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
        System.out.println("result :"+jsonReceived.fromJson(Courses.class,text));
//        System.out.println("result :"+text);

//        ZoukRI.getInstance().setFullCourses(jsonReceived.fromJson(Courses.class,text));
//        if(jsonReceived!=null){
//            ZoukRI.getInstance().getMyUser().setName(user);
//            ZoukRI.getInstance().setScreen(new MainScreen());
//        }

    }
}
