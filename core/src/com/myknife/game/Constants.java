package com.myknife.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Constants {
    public static final float WORLD_SIZE= Gdx.graphics.getWidth();
    public static final Color BACKGROUND_COLOR = new Color(0,0,139/255f,1);

    public static final float WOOD_HEIGTH = Gdx.graphics.getHeight()*6/10;
    public static final float SPIN_FACTOR = 100.f;

    public static final float KNIFE_VELOCITY = 2000.0f;
}
