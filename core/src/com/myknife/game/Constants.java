package com.myknife.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Constants {
    public static final float WORLD_SIZE= Gdx.graphics.getWidth();
    public static final Color BACKGROUND_COLOR = new Color(0,0,139/255f,1);

    public static final float WOOD_HEIGTH = Gdx.graphics.getHeight()*6.0f/10;
    public static float SPIN_FACTOR = 100.f;

    public static final int ORANGE_TYPE = 1;
    public static final int APPLE_TYPE = 2;
    public static final int PEAR_TYPE = 3;

    public static float WORLD_WIDTH = Gdx.graphics.getWidth();
    public static float WORLD_HEIGTH = Gdx.graphics.getHeight();

    public static float WOOD_WIDTH = WORLD_WIDTH/4;
    public static float KNIFE_WIDTH = WORLD_WIDTH/40;
    public static float FRUIT_WIDTH = WORLD_WIDTH/65;

    public static final float KNIFE_VELOCITY = 2000.0f;
}
