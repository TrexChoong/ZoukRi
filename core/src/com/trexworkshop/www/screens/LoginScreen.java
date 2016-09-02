package com.trexworkshop.www.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.trexworkshop.www.asset.Assets;

/**
 * Created by trex on 9/2/2016.
 */
public class LoginScreen extends Screen {

    public LoginScreen(){
        Assets.loadTextures("badlogic.jpg");
        Assets.getManager().finishLoading();
                System.out.println("print world :(");
        Image hello = new Image(Assets.getDrawable("badlogic.jpg"));
        hello.setPosition(getWorldWidth()/2,getWorldHeight()/2, Align.center);
        addActor(hello);
    }

    protected boolean processBackKey() {
        //Gdx.app.debug(KLPoker.TAG, "LoginScreen backKey");
        Gdx.app.exit();
        return true;
    }

    protected void update(float delta) {
    }
}
