package com.evermine.animgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Animation<TextureRegion> link;
	float stateTime;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("character.png");
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
	}

	@Override
	public void render () {
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = link.getKeyFrame(stateTime,true);
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(frame, 200, 100);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
