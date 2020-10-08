package com.myknife.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import sun.rmi.runtime.Log;

import static com.myknife.game.Constants.SPIN_FACTOR;
import static com.myknife.game.Constants.WOOD_HEIGTH;
import static com.myknife.game.Constants.WORLD_SIZE;


public class Wood implements InputProcessor {

    Vector2 position = null;
    Viewport viewport = null;
    Texture woodImage;
    Texture knife;
    Sprite woodSprite;
    Sprite spriteKnife;
    ArrayList<StuckKnife> stuckKnives = new ArrayList<StuckKnife>();
    Boolean toss, overlapped, overlappedFruit, turn;
    DelayedRemovalArray<Knife> knifeRemoval;
    DelayedRemovalArray<Fruit> fruitRemoval;
    Fruit fruitDeneme;
    int slicedFruitIndex;

    public Wood(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        slicedFruitIndex = -1;
        overlapped = false;
        overlappedFruit = false;
        turn = true;
        toss = false;
        woodInit();

        knifeRemoval = new DelayedRemovalArray<Knife>(false, 1);
        fruitRemoval = new DelayedRemovalArray<Fruit>(false, 6);

        woodPrepare();

        nextKnifeAdd(); //starter knife(blue)


            fruitDeneme = orange(0);

    }

    private void woodPrepare() {
        //Default Knives are added to Wood
        stuckKnives.clear();

        knife = new Texture("knife1/drawable-mdpi/default_knife.png");
        spriteKnife = new Sprite(knife);
        spriteKnife.setOrigin(spriteKnife.getWidth()/2,(woodSprite.getHeight()+spriteKnife.getHeight())/2);

        for(int i = 0; i < 4; i++){
            stuckKnives.add(new StuckKnife("knife1/drawable-mdpi/default_knife.png",
                    spriteKnife.getWidth() / 2,
                    (woodSprite.getHeight() + spriteKnife.getHeight()) / 2 ,
                    i * 90.0f));
        }

        //Fruits are added to Wood
        fruitRemoval.clear();

        for(int i = 0; i < 4; i++){
            float angle = 45 + i * 90;
            int random = MathUtils.random(1);
            if(random == 0) {
                fruitRemoval.add(orange(angle));
            }else{
                fruitRemoval.add(apple(angle));
            }
        }



    }

    private void woodInit() {
        woodImage = new Texture("knife1/drawable-mdpi/wood.png");
        woodSprite = new Sprite(woodImage);
    }

    public void update(float delta) {
        woodSprite.rotate(delta* SPIN_FACTOR);

        for(int i=0; i<stuckKnives.size();i++){
            stuckKnives.get(i).update(delta);
        }
        /*
        if(Gdx.input.isTouched()){
            fruitDeneme.updateSlice(delta);
        }else {
            fruitDeneme.update(delta);
        }
         */
        for(Fruit fruit: fruitRemoval){
            if(fruit.getSliced()){
                fruit.updateSlice(delta);
            }else{
                fruit.update(delta);
            }
        }

        tossKnife(delta);

    }

    private void tossKnife(float delta) {
        knifeRemoval.begin();


        if((toss == true) && (knifeRemoval.size == 1)) {
            Rectangle thrown = knifeRemoval.get(0).sprite.getBoundingRectangle();
            thrown.setHeight(thrown.getHeight()/1.5f);
            thrown.setWidth(thrown.getWidth()/6);
                for(Knife knife : stuckKnives){
                    Rectangle stuck = knife.sprite.getBoundingRectangle();
                    stuck.setWidth(stuck.getWidth()/1.5f);
                    stuck.setHeight(stuck.getHeight()/1.5f);
                if(stuck.overlaps(thrown)){
                    overlapped = true;
                }
                int index = 0;
                for(Fruit fruit: fruitRemoval){     //slice fruit check
                    thrown.setHeight(thrown.getHeight()*1.5f);
                    thrown.setWidth(thrown.getWidth()*6);
                    Rectangle fruit1 = fruit.sprite1.getBoundingRectangle();
                    Rectangle fruit2 = fruit.sprite2.getBoundingRectangle();
                    if((thrown.overlaps(fruit1) || thrown.overlaps(fruit2)) && !fruit.getSliced()){
                        overlappedFruit = true;
                        slicedFruitIndex = index;
                    }
                    index++;
                    thrown.setHeight(thrown.getHeight()/1.5f);
                    thrown.setWidth(thrown.getWidth()/6);
                }
            }

            if(overlapped){
                knifeRemoval.removeIndex(0);
                nextKnifeAdd();
                overlapped = false;
                toss = false;
            }else if(knifeRemoval.get(0).getPosition().y > WOOD_HEIGTH - knifeRemoval.get(0).getSprite().getHeight()/2){
                knifeRemoval.removeIndex(0);
                nextKnifeAdd();
                stuckKnifeAdd();
                System.out.println("1");
                toss = false;
            }else if(overlappedFruit){
                overlappedFruit = false;
                Fruit fruitTemp = fruitRemoval.get(slicedFruitIndex);
                fruitTemp.setSliced(true);
                fruitRemoval.set(slicedFruitIndex, fruitTemp);
                knifeRemoval.removeIndex(0);
                nextKnifeAdd();
                stuckKnifeAdd();
                System.out.println("2");
                toss = false;

            }else{
                knifeRemoval.get(0).update(delta);
            }
        }

        knifeRemoval.end();
    }

    public void render(SpriteBatch batch) {


        for(int i=0; i<stuckKnives.size();i++){
            stuckKnives.get(i).render(batch);
        }
        if( (knifeRemoval.size == 1)) {
            knifeRemoval.get(0).render(batch);
        }

        woodSprite.setPosition(WORLD_SIZE/2 - woodSprite.getWidth()/2, WOOD_HEIGTH );
        woodSprite.draw(batch);

        /*
        if(Gdx.input.isTouched()){
            fruitDeneme.renderSlice(batch);
        }else {
            fruitDeneme.render(batch);
        }
         */
        for(Fruit fruit: fruitRemoval){
            if(fruit.getSliced()){
                fruit.renderSlice(batch);
            }else{
                fruit.render(batch);
            }
        }


    }
    public void nextKnifeAdd(){
        if(turn) {
            knifeRemoval.add(new Knife("knife1/drawable-mdpi/blue_knife.png"));
        }else{
            knifeRemoval.add(new Knife("knife1/drawable-mdpi/red_knife.png"));
        }
    }
    public void stuckKnifeAdd(){
        if(turn){
            stuckKnives.add(new StuckKnife("knife1/drawable-mdpi/red_knife.png",
                    spriteKnife.getWidth() / 2, (woodSprite.getHeight() + spriteKnife.getHeight()) / 2));
        }else {
            stuckKnives.add(new StuckKnife("knife1/drawable-mdpi/blue_knife.png",
                    spriteKnife.getWidth() / 2, (woodSprite.getHeight() + spriteKnife.getHeight()) / 2));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        toss = true;
        turn = !turn;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private Fruit orange(float random){
        return new Fruit("knife1/drawable-mdpi/orange_half_1.png",
                "knife1/drawable-mdpi/orange_half_2.png",
                "knife1/drawable-mdpi/orange_leaf_2.png",
                woodSprite.getHeight()/2,
                random);
    }
    private Fruit apple(float random){
        return new Fruit("knife1/drawable-mdpi/apple_half_2.png",
                "knife1/drawable-mdpi/apple_half_1.png",
                "knife1/drawable-mdpi/apple_leaf.png",
                woodSprite.getHeight()/2,
                random);
    }
}