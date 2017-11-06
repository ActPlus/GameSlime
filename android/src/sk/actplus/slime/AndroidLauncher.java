package sk.actplus.slime;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gushikustudios.rube.RubeLoaderTest;

import sk.actplus.slime.GameSlime;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useCompass = false;
		config.useAccelerometer = false;

		initialize(new GameSlime(), config);

		/*
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		initialize(new RubeLoaderTest(true), cfg);
		*/
	}
}
