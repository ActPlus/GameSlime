package sk.actplus.slime;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameSlime extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
  

	
	@Override
	public void create () {
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		batch.begin();
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
