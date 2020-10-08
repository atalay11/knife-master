package com.myknife.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static com.myknife.game.Constants.SPIN_FACTOR;
import static com.myknife.game.Constants.WOOD_HEIGTH;
import static com.myknife.game.Constants.WORLD_SIZE;

public class Wood {
    private Texture texture;
    protected Sprite sprite;
    private Vector2 position;

    public Wood(){
        texture = new Texture("knife1/drawable-mdpi/wood.png");
        sprite = new Sprite(texture);
    }

    public void update(float delta){
        sprite.rotate(delta* SPIN_FACTOR);
    }

    public void render(SpriteBatch batch){
        sprite.setPosition(WORLD_SIZE/2 - sprite.getWidth()/2, WOOD_HEIGTH );
        sprite.draw(batch);
    }
}
