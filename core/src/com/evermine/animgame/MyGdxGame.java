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

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player link;
	Texture background;
	float stateTime;
	Rectangle up, down, left, right, fire;
	final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;
	public static Logs gameLogs;
	public static ArrayList<Player> players;
	private ServerConnection server;
	public static Texture txt;
	@Override
	public void create () {
		batch = new SpriteBatch();
		gameLogs = new Logs();
		txt =new Texture("character.png");
		server= new ServerConnection("localhost",8888);
		players = new ArrayList<Player>();
		link = new Player(txt,200,100);
		background = new Texture("background.png");
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		// facilities per calcular el "touch"
		up = new Rectangle(0, screenHeight*2/3, screenWidth, screenHeight/3);
		down = new Rectangle(0, 0, screenWidth, screenHeight/3);
		left = new Rectangle(0, 0, screenWidth/3, screenHeight);
		right = new Rectangle(screenWidth*2/3, 0, screenWidth/3, screenHeight);
	}

	@Override
	public void render () {
		stateTime += Gdx.graphics.getDeltaTime();
		ScreenUtils.clear(1, 0, 0, 1);
		playerMovement();
		batch.begin();
		server.sendPlayerPos("Pedro",100,100);
		batch.draw(background,0,0);
		link.render(batch);

		for(int i=0;i<players.size();i++){
			players.get(i).render(batch);
		}
		gameLogs.render(batch);
		batch.end();
	}
	private void playerMovement(){
		switch (virtual_joystick_control()){
			case 0:
				link.setPlayerMode(0);
				break;
			case 1:
				link.setY(link.getY()-1);
				link.setPlayerMode(1);
				break;
			case 2:
				link.setY(link.getY()+1);
				link.setPlayerMode(1);
				break;
			case 3:
				link.setX(link.getX()-1);
				link.setPlayerMode(1);
				link.setRotation(-2);
				break;
			case 4:
				link.setX(link.getX()+1);
				link.setPlayerMode(1);
				link.setRotation(2);
				break;
		}
	}
	@Override
	public void dispose () {
		batch.dispose();
		link.disposeTextures();
		for(int i=0;i<players.size();i++){
			players.get(i).disposeTextures();
		}
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
