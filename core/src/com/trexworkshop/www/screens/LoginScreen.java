package com.trexworkshop.www.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.trexworkshop.www.ZoukRI;
import com.trexworkshop.www.asset.Assets;
import com.trexworkshop.www.models.userZouk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by trex on 9/2/2016.
 */
public class LoginScreen extends Screen {
    public BitmapFont helvetica30 = null;
    int holderUser = -1;
    int holderPass = -1;

    public LoginScreen(){
        Assets.loadTextures("login/DisconnectMessageBox.png", "login/LoginButtonUp.png",
                "login/OKButtonUp.png","common/whiteBox.png");
        //Assets.loadFonts(35, "Helvetica-Bold.ttf");
        Assets.getManager().finishLoading();

//        BitmapFont helveticaBold35 = Assets.getFont(35, "Helvetica-Bold.ttf");


        Image bg = new Image(Assets.getDrawable("login/DisconnectMessageBox.png"));
        bg.setPosition(getWorldWidth()/2,getWorldHeight()/2, Align.center);
        bg.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               stage.unfocusAll();
               Gdx.input.setOnscreenKeyboardVisible(false);
           }
        });
        addActor(bg);

        //helvetica30 = Assets.getFont(35, "Helvetica-Bold.ttf");
        TextField.TextFieldStyle inputFont = new TextField.TextFieldStyle();
        //inputFont.font = helveticaBold35;
        inputFont.background = Assets.getDrawable("common/whiteBox.png");
        inputFont.fontColor = Color.WHITE;


        final VisTextField userField = new VisTextField();
        VisTextField.VisTextFieldStyle holder = userField.getStyle();
       // holder.font = helveticaBold35;
       // userField.setStyle(holder);
        userField.setSize(300,100);
        userField.setPosition(bg.getX(Align.center)-100,bg.getY(Align.center)-100,Align.center);
        addActor(userField);

        Image userLabel = new Image(Assets.getTextDrawable("Username : ",50,-1,false));
        userLabel.setPosition(userField.getX()-50,userField.getY(Align.center),Align.right);
        addActor(userLabel);

        final VisTextField passField = new VisTextField();
        //VisTextField.VisTextFieldStyle holder2 = passField.getStyle();
        //holder2.font = helveticaBold35;
        //passField.setStyle(holder2);
        passField.setSize(300,100);
        passField.setPosition(userField.getX(Align.center),userField.getY()-50,Align.top);
        addActor(passField);

        Image passLabel = new Image(Assets.getTextDrawable("Password : ",50,-1,false));
        passLabel.setPosition(userField.getX()-50,passField.getY(Align.center),Align.right);
        addActor(passLabel);

        Button loginSubmit = new Button(Assets.getDrawable("login/LoginButtonUp.png"));
        loginSubmit.setPosition(userField.getX(Align.right)+50,userField.getY()-25,Align.left);
        loginSubmit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String user = userField.getText();
                String pass = passField.getText();
                Boolean login = true;
                try{
                    submitData(user,pass,login);
                }
                catch(Exception ex)
                {
                    System.out.println(" url exeption! "+ex.getMessage() );
                }
            }
        });
        addActor(loginSubmit);

        ZoukRI.getInstance().setLoginScreen(this);
    }

    protected boolean processBackKey() {
        //Gdx.app.debug(KLPoker.TAG, "LoginScreen backKey");
        Gdx.app.exit();
        return true;
    }

    protected void update(float delta) {
    }

    // Create GetText Metod
    public  void  submitData(String user, String pass, Boolean flag)  throws UnsupportedEncodingException
    {
        // Get user defined values
        String Login   = user;
        String Pass   = pass;
        Boolean flagSend = flag;

        // Create data variable for sent values to server

        String data = URLEncoder.encode("user", "UTF-8")
                + "=" + URLEncoder.encode(Login, "UTF-8");

        data += "&" + URLEncoder.encode("pass", "UTF-8")
                + "=" + URLEncoder.encode(Pass, "UTF-8");

        data += "&" + URLEncoder.encode("login", "UTF-8")
                + "=" + URLEncoder.encode(flagSend.toString(), "UTF-8");

        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL("http://www.trexworkshop.com/zouk/zoukLogin.php");

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
        ZoukRI.getInstance().setMyUser(jsonReceived.fromJson(userZouk.class,text));
        if(jsonReceived!=null){
            ZoukRI.getInstance().getMyUser().setName(user);
            ZoukRI.getInstance().setScreen(new MainScreen());
        }

    }

}
