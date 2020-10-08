package com.myknife.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.myknife.game.Constants.BACKGROUND_COLOR;

public class GameScreen implements Screen {

    SpriteBatch batch;
    Texture background;

    FitViewport gameViewport;

    MainAction mainAction;


    @Override
    public void show() {
        background = new Texture("knife1/drawable-hdpi/knife_bg.png");
        batch = new SpriteBatch();

        mainAction = new MainAction(gameViewport);
        Gdx.input.setInputProcessor(mainAction);

        gameViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void render(float delta) {
        mainAction.update(delta);

        gameViewport.apply();
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,BACKGROUND_COLOR.g, BACKGROUND_COLOR.b,BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        batch.begin();
        mainAction.render(batch);

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width,height,true);
        mainAction.init();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        batch.dispose();
    }

    @Override
    public void dispose() {

    }

}
