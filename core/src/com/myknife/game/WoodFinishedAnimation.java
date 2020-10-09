package com.myknife.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import static com.myknife.game.Constants.WOOD_HEIGTH;
import static com.myknife.game.Constants.WORLD_SIZE;

public class WoodFinishedAnimation {
    private TextureRegion texture1 = new TextureRegion(new Texture("knife3/drawable-mdpi/1_st_piece.png"));
    private TextureRegion texture2 = new TextureRegion(new Texture("knife3/drawable-mdpi/2_nd_piece.png"));
    private TextureRegion texture3 = new TextureRegion(new Texture("knife3/drawable-mdpi/3_rd_piece.png"));
    private TextureRegion texture4 = new TextureRegion(new Texture("knife3/drawable-mdpi/4_th_piece.png"));
    private Vector2 position1, position2, position3, position4;
    private Sprite sprite1, sprite2, sprite3, sprite4;
    public  WoodFinishedAnimation(){
        sprite1 = new Sprite(texture1);
        sprite2 = new Sprite(texture2);
        sprite3 = new Sprite(texture3);
        sprite4 = new Sprite(texture4);
        init();
    }
    public void init(){
        position1 = new Vector2(WORLD_SIZE/2 - sprite1.getWidth(), WOOD_HEIGTH + sprite1.getHeight());
        position2 = new Vector2(WORLD_SIZE/2 - sprite2.getWidth(), WOOD_HEIGTH);
        position3 = new Vector2(WORLD_SIZE/2, WOOD_HEIGTH);
        position4 = new Vector2(WORLD_SIZE/2, WOOD_HEIGTH + sprite1.getHeight());
    }
    public void update(float delta){
        position1.x += delta* -100.0f;
        position1.y += delta* 100.0f;
        position2.x += delta* -100.0f;
        position2.y += delta* -100.0f;
        position3.x += delta* 100.0f;
        position3.y += delta* -100.0f;
        position4.x += delta* 100.0f;
        position4.y += delta* 100.0f;
    }
    public void render(SpriteBatch batch){
        sprite1.setPosition(position1.x, position1.y);
        sprite1.draw(batch);
        sprite2.setPosition(position2.x, position2.y);
        sprite2.draw(batch);
        sprite3.setPosition(position3.x, position3.y);
        sprite3.draw(batch);
        sprite4.setPosition(position4.x, position4.y);
        sprite4.draw(batch);
    }
}