package com.myknife.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.myknife.game.Constants.KNIFE_VELOCITY;
import static com.myknife.game.Constants.SPIN_FACTOR;
import static com.myknife.game.Constants.WOOD_HEIGTH;
import static com.myknife.game.Constants.WORLD_SIZE;

public class Knife {
    private Texture texture;
    protected Sprite sprite;
    protected Vector2 position;
    private float vx, vy;
    private Boolean isHit;

    public Knife(String pngPath){
        texture = new Texture(pngPath);
        sprite = new Sprite(texture);
        position = new Vector2(WORLD_SIZE/2 - sprite.getWidth()/2, 0 );
        isHit = false;

        init();
    }

    public void init(){
        sprite.setPosition(WORLD_SIZE/2 - sprite.getWidth()/2, 0 );

        vx = MathUtils.random()*2000 - 500.0f;
        vy = -(1000.0f+ MathUtils.random()*200);

    }

    public void update(float delta){
        position.y = position.y + delta * (KNIFE_VELOCITY - delta* 9.8f);
        sprite.setPosition(position.x, position.y);
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);

        position.x = sprite.getX();
        position.y = sprite.getY();
    }

    public void updateHit(float delta){

        position.x += delta * vx;
        position.y += delta * (vy - delta * 500.0f);
        sprite.rotate(delta*3000.0f);
    }

    public void renderHit(SpriteBatch batch){
        sprite.setPosition(position.x,
                position.y);
        sprite.draw(batch);

        position.x = sprite.getX();
        position.y = sprite.getY();

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

    public Boolean getHit() {
        return isHit;
    }

    public void setHit(Boolean hit) {
        isHit = hit;
    }
}
