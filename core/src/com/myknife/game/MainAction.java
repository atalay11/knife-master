package com.myknife.game;

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

import static com.myknife.game.Constants.APPLE_TYPE;
import static com.myknife.game.Constants.ORANGE_TYPE;
import static com.myknife.game.Constants.PEAR_TYPE;
import static com.myknife.game.Constants.SPIN_FACTOR;
import static com.myknife.game.Constants.WOOD_HEIGTH;


public class MainAction implements InputProcessor {

    Vector2 position = null;
    Viewport viewport;
    Texture knife;
    Sprite spriteKnife;
    ArrayList<StuckKnife> stuckKnives = new ArrayList<StuckKnife>();
    Boolean toss, overlapped, overlappedFruit, turn , gameFinished;
    DelayedRemovalArray<Knife> knifeRemoval;
    DelayedRemovalArray<Fruit> fruitRemoval;
    WoodFinishedAnimation woodFinishedAnimation;
    Knife lastHitKnife;
    Fruit lastHitFruit;
    Wood wood;
    Fruit fruitDeneme;
    int slicedFruitIndex,blueKnife,redKnife;

    public MainAction(Viewport viewport) {
        this.viewport = viewport;
        init();
    }

    public void init() {
        slicedFruitIndex = -1;
        overlapped = false;
        overlappedFruit = false;
        turn = true;
        toss = false;
        redKnife = blueKnife = 5;
        gameFinished = false;
        wood = new Wood();
        woodFinishedAnimation = new WoodFinishedAnimation();
        lastHitKnife = null;
        lastHitFruit = null;

        knifeRemoval = new DelayedRemovalArray<Knife>(false, 1);
        fruitRemoval = new DelayedRemovalArray<Fruit>(false, 6);

        woodPrepare();

        nextKnifeAdd(); //starter knife(blue)


            fruitDeneme = orange(0);

    }

    private void woodPrepare() {
        //Default Knives are added to Wood
        SPIN_FACTOR = 100.0f;
        stuckKnives.clear();

        knife = new Texture("knife1/drawable-mdpi/default_knife.png");
        spriteKnife = new Sprite(knife);
        spriteKnife.setOrigin(spriteKnife.getWidth()/2,(wood.sprite.getHeight()+spriteKnife.getHeight())/2);

        for(int i = 0; i < 4; i++){
            stuckKnives.add(new StuckKnife("knife1/drawable-mdpi/default_knife.png",
                    spriteKnife.getWidth() / 2,
                    (wood.sprite.getHeight() + spriteKnife.getHeight()) / 2 ,
                    i * 90.0f));
        }

        //Fruits are added to Wood
        fruitRemoval.clear();

        for(int i = 0; i < 4; i++){
            float angle = 45 + i * 90;
            int random = MathUtils.random(150);
            if(random < 50) {
                fruitRemoval.add(orange(angle));
            }else if(random < 100 && random > 50){
                fruitRemoval.add(apple(angle));
            }else{
                fruitRemoval.add(pear(angle));
            }
        }



    }


    public void update(float delta) {
        if(!gameFinished){
            wood.update(delta);
            for(int i=0; i<stuckKnives.size();i++){
                stuckKnives.get(i).update(delta);
            }
            for(Fruit fruit: fruitRemoval){
                if(fruit.getSliced()){
                    fruit.updateSlice(delta);
                }else{
                    fruit.update(delta);
                }
            }
            tossKnife(delta);

        }
        else {
            woodFinishedAnimation.update(delta);
            if(lastHitKnife != null){
                lastHitKnife.updateHit(delta);
            }
            if(lastHitFruit != null){
                lastHitFruit.updateSlice(delta);
            }
            int i=0;
            for(StuckKnife k: stuckKnives){
                k.updateFinished(delta);
                System.out.println(i++);
            }
            for(Fruit fruit: fruitRemoval){
                fruit.updateFinished(delta);
            }
        }

    }

    private void tossKnife(float delta) {

        if((toss) && (knifeRemoval.size == 1)) {
            Rectangle thrown = knifeRemoval.get(0).sprite.getBoundingRectangle();
            thrown.setHeight(thrown.getHeight()/1.5f);
            thrown.setWidth(thrown.getWidth()/6);
                for(Knife knife : stuckKnives){
                    Rectangle stuck = knife.sprite.getBoundingRectangle();
                    stuck.setWidth(stuck.getWidth()/1.5f);
                    stuck.setHeight(stuck.getHeight()/1.5f);
                if(stuck.overlaps(thrown) && !knifeRemoval.get(0).getHit()){
                    overlapped = true;
                }
                int index = 0;
                for(Fruit fruit: fruitRemoval){     //slice fruit check
                    thrown.setHeight(thrown.getHeight()*1.5f);
                    thrown.setWidth(thrown.getWidth()*6);
                    Rectangle fruit1 = fruit.sprite1.getBoundingRectangle();
                    Rectangle fruit2 = fruit.sprite2.getBoundingRectangle();
                    fruit2.setWidth(fruit2.getWidth()*5/7);
                    if((thrown.overlaps(fruit1) || thrown.overlaps(fruit2)) && !fruit.getSliced() && !knifeRemoval.get(0).getHit()){
                        overlappedFruit = true;
                        slicedFruitIndex = index;
                    }
                    index++;
                    thrown.setHeight(thrown.getHeight()/1.5f);
                    thrown.setWidth(thrown.getWidth()/6);
                }
            }

            if(overlapped){
                Knife knifeTemp = knifeRemoval.get(0);
                knifeRemoval.clear();
                knifeTemp.setHit(true);
                knifeRemoval.add(knifeTemp);
                kCount();
                if(gameFinished){
                    lastHitKnife = knifeRemoval.get(0);
                }
                overlapped = false;

            }else if(overlappedFruit){
                overlappedFruit = false;
                Fruit fruitTemp = fruitRemoval.get(slicedFruitIndex);
                fruitTemp.setSliced(true);
                fruitTemp.addBuff();
                fruitRemoval.set(slicedFruitIndex, fruitTemp);
                knifeRemoval.removeIndex(0);
                nextKnifeAdd();
                stuckKnifeAdd();
                kCount();
                if(gameFinished){
                    lastHitFruit = fruitRemoval.get(slicedFruitIndex);
                    fruitRemoval.removeIndex(slicedFruitIndex);
                }
                toss = false;

            }else if(knifeRemoval.get(0).getPosition().y > WOOD_HEIGTH - knifeRemoval.get(0).getSprite().getHeight()/2){
                knifeRemoval.removeIndex(0);
                nextKnifeAdd();
                stuckKnifeAdd();
                kCount();
                toss = false;
            }else if(!knifeRemoval.get(0).getHit()){

                knifeRemoval.get(0).update(delta);

            }
            if (knifeRemoval.get(0).getHit()) {
                knifeRemoval.get(0).updateHit(delta);
                System.out.println("1");
                if (knifeRemoval.get(0).getPosition().y < -knifeRemoval.get(0).sprite.getHeight()) {
                    knifeRemoval.removeIndex(0);
                    toss = false;
                    nextKnifeAdd();
                }
            }

        }


    }

    public void render(SpriteBatch batch) {

        if(!gameFinished)
            {

                for(int i=0; i<stuckKnives.size();i++){
                    stuckKnives.get(i).render(batch);
                }
                if(knifeRemoval.get(0).getHit()){
                    knifeRemoval.get(0).renderHit(batch);
                }else {
                    knifeRemoval.get(0).render(batch);
                }

                wood.render(batch);

                for(Fruit fruit: fruitRemoval){
                    if(fruit.getSliced()){
                        fruit.renderSlice(batch);
                    }else{
                        fruit.render(batch);
                    }
                }

            }
        else
                {
                woodFinishedAnimation.render(batch);
                if(lastHitKnife != null){
                    lastHitKnife.renderHit(batch);
                }
                if(lastHitFruit != null){
                    lastHitFruit.renderSlice(batch);
                }
                for(StuckKnife k : stuckKnives){
                        k.renderFinished(batch);
                    }
                for(Fruit fruit: fruitRemoval){
                    fruit.renderFinished(batch);
                }
            }


        System.out.println(blueKnife+"  "+redKnife);

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
                    spriteKnife.getWidth() / 2, (wood.sprite.getHeight() + spriteKnife.getHeight()) / 2));
        }else {
            stuckKnives.add(new StuckKnife("knife1/drawable-mdpi/blue_knife.png",
                    spriteKnife.getWidth() / 2, (wood.sprite.getHeight() + spriteKnife.getHeight()) / 2));
        }
    }
    public void kCount(){
        if(turn)
            redKnife-=1;
        else
            blueKnife-=1;

        if(redKnife <= 0 && blueKnife <= 0){
            gameFinished = true;
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

        if(!toss)
            {turn=!turn;}
        toss = true;
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
                wood.sprite.getHeight()/2,
                random, ORANGE_TYPE);
    }
    private Fruit apple(float random){
        return new Fruit("knife1/drawable-mdpi/apple_half_2.png",
                "knife1/drawable-mdpi/apple_half_1.png",
                "knife1/drawable-mdpi/apple_leaf.png",
                wood.sprite.getHeight()/2,
                random, APPLE_TYPE);
    }
    private Fruit pear(float random){
        return new Fruit("knife1/drawable-mdpi/apple_half_2.png",
                "knife1/drawable-mdpi/apple_half_1.png",
                "knife1/drawable-mdpi/apple_leaf.png",
                wood.sprite.getHeight()/2,
                random, PEAR_TYPE);
    }
}
