package com.trexworkshop.www.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Getter;

public abstract class Screen extends ScreenAdapter {
	private static final float MIN_WORLD_WIDTH = 1440;
//	private static final float MAX_WORLD_WIDTH = 1440;
	private static final float MAX_WORLD_WIDTH = 1920;
	private static final float MIN_WORLD_HEIGHT = 1080;
	private static final float MAX_WORLD_HEIGHT = MIN_WORLD_HEIGHT;
	protected Screen currentScreen;
	protected Stage stage;
	@Getter
	protected float worldWidth = MAX_WORLD_WIDTH;
	@Getter
	protected float worldHeight = MAX_WORLD_HEIGHT;
	float fps = 30f;
	long delay = 1000 / (long)  fps;
	long diff, start;

	public Screen() {
		currentScreen = this;
		stage = new Stage(new ExtendViewport(MIN_WORLD_WIDTH, MIN_WORLD_HEIGHT, MAX_WORLD_WIDTH , MAX_WORLD_HEIGHT));
		worldWidth = stage.getViewport().getWorldWidth();
		worldHeight = stage.getViewport().getWorldHeight();
		InputMultiplexer inputs = new InputMultiplexer();
		inputs.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown (int keycode) {
				return ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) && processBackKey();
			}
		});
		inputs.addProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(inputs);
	}
	protected abstract boolean processBackKey();
	
	protected void addActor(Actor actor) {
		stage.addActor(actor);
	}

	protected void FPSRate(){

		diff = System.currentTimeMillis() - start;
		if (diff < delay) {
			try {
				Thread.sleep(delay - diff);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		start = System.currentTimeMillis();
	}

	@Override
	public void render(float delta) {
		FPSRate(); //Function to change the frame rate
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
		stage.act(delta);
		stage.draw();
	}

	protected abstract void update(float delta);

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void dispose () {
		if(stage != null) stage.dispose();
	}

	/**
	 * pop out prompt after change screen type name
	 */
	public enum PromptType {
		KickedFromTableDueToInactivePrompt,
		TournamentResultPrompt,
		MaintenancePrompt,
		KOPEventEndsPrompt,
		MergeTokenPrompt,
		TopUpPrompt,
		DailyTopUpPrompt
	}
}