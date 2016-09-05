package com.trexworkshop.www.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.trexworkshop.www.ZoukRI;
import com.trexworkshop.www.asset.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.trexworkshop.www.groups.instructorMainGroup;
import com.trexworkshop.www.models.userZouk;

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



    }

    protected void update(float delta) {
    }

    @Override
    protected boolean processBackKey() {
        ZoukRI.getInstance().setScreen(ZoukRI.getInstance().getLoginScreen());
        return false;
    }
}
