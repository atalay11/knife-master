package com.myknife.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static com.myknife.game.Constants.KNIFE_VELOCITY;
import static com.myknife.game.Constants.SPIN_FACTOR;
import static com.myknife.game.Constants.WOOD_HEIGTH;
import static com.myknife.game.Constants.WORLD_SIZE;

public class Knife {
    private Texture texture;
    protected Sprite sprite;
    private Vector2 position;

    public Knife(String pngPath){
        texture = new Texture(pngPath);
        sprite = new Sprite(texture);
        position = new Vector2(WORLD_SIZE/2 - sprite.getWidth()/2, 0 );

        init();
    }

    public void init(){
        sprite.setPosition(WORLD_SIZE/2 - sprite.getWidth()/2, 0 );
    }

    public void update(float delta){
        position.y = position.y + delta * KNIFE_VELOCITY;
        sprite.setPosition(position.x, position.y);
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }





    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
