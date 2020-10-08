package com.myknife.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.myknife.game.Constants.SPIN_FACTOR;
import static com.myknife.game.Constants.WOOD_HEIGTH;
import static com.myknife.game.Constants.WORLD_SIZE;

public class StuckKnife extends Knife {

    public StuckKnife(String pngPath, float originX, float originY, float defaultKnifeRotationStart) {
        super(pngPath);
        sprite.setOrigin(originX, originY);
        sprite.rotate(defaultKnifeRotationStart);
    }

    public StuckKnife(String pngPath, float originX, float originY) {
        super(pngPath);
        sprite.setOrigin(originX, originY);
    }

    @Override
    public void update(float delta){
        sprite.rotate(delta * SPIN_FACTOR);
    }

    @Override
    public void render(SpriteBatch batch){
        sprite.setPosition(WORLD_SIZE/2 - sprite.getWidth()/2, WOOD_HEIGTH - sprite.getHeight()/2 );
        sprite.draw(batch);
    }
}
