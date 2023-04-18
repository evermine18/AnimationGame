package com.evermine.animgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture background;
	Animation<TextureRegion> link;
	float stateTime;
	Rectangle up, down, left, right, fire;
	final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;
	@Override
	public void create () {
		Graphics.DisplayMode desktopDisplayMode = Gdx.graphics.getDisplayMode();
		batch = new SpriteBatch();
		img = new Texture("character.png");
		background = new Texture("background.png");
		int screenWidth = desktopDisplayMode.width;
		int screenHeight = desktopDisplayMode.height;
		TextureRegion frames[] = new TextureRegion[9];
		frames[0] = new TextureRegion(img,10,3,26,45);
		frames[1] = new TextureRegion(img,60,2,26,46);
		frames[2] = new TextureRegion(img,106,4,36,41);
		frames[3] = new TextureRegion(img,157,4,38,44);
		frames[4] = new TextureRegion(img,210,5,31,43);
		frames[5] = new TextureRegion(img,310,2,27,46);
		frames[6] = new TextureRegion(img,360,5,33,45);
		frames[7] = new TextureRegion(img,409,4,37,44);
		frames[8] = new TextureRegion(img,460,5,31,43);
		link = new Animation<TextureRegion>(0.08f,frames);
		// facilities per calcular el "touch"
		up = new Rectangle(0, screenHeight*2/3, screenWidth, screenHeight/3);
		down = new Rectangle(0, 0, screenWidth, screenHeight/3);
		left = new Rectangle(0, 0, screenWidth/3, screenHeight);
		right = new Rectangle(screenWidth*2/3, 0, screenWidth/3, screenHeight);
	}

	@Override
	public void render () {
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = link.getKeyFrame(stateTime,true);
		ScreenUtils.clear(1, 0, 0, 1);
		virtual_joystick_control();
		batch.begin();
		batch.draw(background,0,0);
		batch.draw(frame, 200, 100,0, 0,frame.getRegionWidth(),frame.getRegionHeight(),2,2,0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	protected int virtual_joystick_control() {
		// iterar per multitouch
		// cada "i" és un possible "touch" d'un dit a la pantalla
		for(int i=0;i<10;i++)
			if (Gdx.input.isTouched(i)) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				// traducció de coordenades reals (depen del dispositiu) a 800x480
				//game.camera.unproject(touchPos);
				if (up.contains(touchPos.x, touchPos.y)) {
					return UP;
				} else if (down.contains(touchPos.x, touchPos.y)) {
					return DOWN;
				} else if (left.contains(touchPos.x, touchPos.y)) {
					return LEFT;
				} else if (right.contains(touchPos.x, touchPos.y)) {
					return RIGHT;
				}
			}
		return IDLE;
	}
}
