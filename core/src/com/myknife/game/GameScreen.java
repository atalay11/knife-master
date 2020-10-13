package com.myknife.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.myknife.game.Constants.APPLE_HIT;
import static com.myknife.game.Constants.BACKGROUND_COLOR;
import static com.myknife.game.Constants.ORANGE_HIT;
import static com.myknife.game.Constants.PEAR_HIT;
import static com.myknife.game.Constants.WOOD_WIDTH;
import static com.myknife.game.Constants.WORLD_HEIGHT;
import static com.myknife.game.Constants.WORLD_WIDTH;

public class GameScreen implements Screen {

    SpriteBatch batch;
    //SpriteBatch hudBatch;
    Texture background;

    StretchViewport gameViewport;
    //ScreenViewport hudVPort;

    MainAction mainAction;

    float[] redAlpha;
    float[] blueAlpha;


    @Override
    public void show() {
        background = new Texture("knife1/drawable-hdpi/knife_bg.png");
        batch = new SpriteBatch();

        //hudVPort = new ScreenViewport();
        //hudBatch = new SpriteBatch();

        mainAction = new MainAction(gameViewport);
        Gdx.input.setInputProcessor(mainAction);

        Texture background = new Texture("knife3/drawable-mdpi/knife_bg.png");

        gameViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        redAlpha = new float[]{1, 1, 1, 1, 1};
        blueAlpha = new float[]{1, 1, 1, 1, 1};
    }

    @Override
    public void render(float delta) {
        mainAction.update(delta);

        if(mainAction.redKnife<5&&mainAction.redKnife>=0){
            redAlpha[mainAction.redKnife]=0.2f;
        }
        if (mainAction.blueKnife<5&&mainAction.blueKnife>=0){
            blueAlpha[mainAction.blueKnife]=0.2f;
        }

        gameViewport.apply();
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r,BACKGROUND_COLOR.g, BACKGROUND_COLOR.b,BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       // hudVPort.apply();

        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        //hudBatch.setProjectionMatrix(hudVPort.getCamera().combined);

        batch.begin();
        batch.draw(background,0, 0, gameViewport.getScreenWidth(), gameViewport.getWorldHeight());
        mainAction.render(batch);
        drawHud(batch);
        batch.end();

        //hudBatch.begin();
        //drawKnives(hudBatch);
        //hudBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width,height,true);
        //hudVPort.update(width,height,true);
        redAlpha = new float[]{1,1,1,1,1};
        blueAlpha = new float[]{1,1,1,1,1};
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
        //hudBatch.dispose();
    }

    @Override
    public void dispose() {

    }

    public void drawHud(SpriteBatch batch){
        for(int i = 0; i<5; i++){
            Texture red = new Texture("knife1/drawable-xxxhdpi/red_knife_count_indicator.png");
            Sprite redS = new Sprite(red);
            float scale = redS.getHeight()/redS.getWidth();
            redS.setAlpha(redAlpha[i]);
            redS.setSize(WORLD_WIDTH/10,WORLD_WIDTH/10*scale);
            redS.setPosition(WORLD_WIDTH/10,WORLD_HEIGHT/10+i*redS.getHeight()*1.1f);
            redS.draw(batch);
        }
        for (int i=0; i<5; i++){
            Texture blue = new Texture("knife1/drawable-xxxhdpi/blue_knife_count_indicator.png");
            Sprite blueS = new Sprite(blue);
            float scale = blueS.getHeight()/blueS.getWidth();
            blueS.setAlpha(blueAlpha[i]);
            blueS.setSize(WORLD_WIDTH/10,WORLD_WIDTH/10*scale);
            blueS.setPosition(WORLD_WIDTH*8/10,WORLD_HEIGHT/10+i*blueS.getHeight()*1.1f);
            blueS.draw(batch);
        }
        for (int i=0;i<3;i++){
            Texture icon;
            switch (i){
                case 0 : {
                    if (!APPLE_HIT)
                        icon = new Texture("knife1/drawable-xxxhdpi/speed_2_x.png");
                    else
                        icon = new Texture("knife2/drawable-xxxhdpi/speed_2_x.png");
                    break;
                }
                case 1 : {
                    if (!ORANGE_HIT)
                        icon = new Texture("knife1/drawable-xxxhdpi/speed_4_x.png");
                    else
                        icon = new Texture("knife2/drawable-xxxhdpi/speed_4_x.png");
                    break;
                }
                case 2 : {
                    if (!PEAR_HIT)
                        icon = new Texture("knife1/drawable-xxxhdpi/reverse.png");
                    else
                        icon = new Texture("knife2/drawable-xxxhdpi/reverse.png");
                    break;
                }
                default:icon = new Texture("knife2/drawable-xxxhdpi/speed_2_x.png");
                break;
            }
            Sprite iconS = new Sprite(icon);
            float scale = iconS.getHeight()/iconS.getWidth();
            iconS.setSize(WORLD_WIDTH/15,WORLD_WIDTH/15*scale);
            iconS.setPosition(WORLD_WIDTH*10/15f+i*iconS.getWidth()*1.3f,WORLD_HEIGHT*9/10);
            iconS.draw(batch);
        }
    }


}
