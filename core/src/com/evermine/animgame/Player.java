package com.evermine.animgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private Texture img;
    private Animation<TextureRegion> running;
    private Animation<TextureRegion> idle;
    private int x,y;
    private float stateTime;
    private int playerMode = 0;
    private int rotation = 2;

    public Player(Texture sprite, int x,int y){
        this.img = sprite;
        // Running animation
        TextureRegion runningFrame[] = new TextureRegion[9];
        runningFrame[0] = new TextureRegion(img,10,3,26,45);
        runningFrame[1] = new TextureRegion(img,60,2,26,46);
        runningFrame[2] = new TextureRegion(img,106,4,36,41);
        runningFrame[3] = new TextureRegion(img,157,4,38,44);
        runningFrame[4] = new TextureRegion(img,210,5,31,43);
        runningFrame[5] = new TextureRegion(img,310,2,27,46);
        runningFrame[6] = new TextureRegion(img,360,5,33,45);
        runningFrame[7] = new TextureRegion(img,409,4,37,44);
        runningFrame[8] = new TextureRegion(img,460,5,31,43);
        this.running = new Animation<TextureRegion>(0.08f,runningFrame);
        // Idle animation
        TextureRegion idleFrame[] = new TextureRegion[1];
        idleFrame[0] = new TextureRegion(img,37,60,22,48);
        this.idle = new Animation<TextureRegion>(0.12f,idleFrame);
        this.x=x;
        this.y=y;
    }
    private TextureRegion getPlayerFrame(){
        if(playerMode==1){
            return running.getKeyFrame(stateTime,true);
        }
        return idle.getKeyFrame(stateTime,true);
    }
    public void render(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion playerFrame = getPlayerFrame();
        batch.draw(playerFrame, x,y,0, 0,playerFrame.getRegionWidth(),playerFrame.getRegionHeight(),rotation,2,0);
    }
    public void disposeTextures(){
        img.dispose();
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlayerMode() {
        return playerMode;
    }

    public void setPlayerMode(int playerMode) {
        this.playerMode = playerMode;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
