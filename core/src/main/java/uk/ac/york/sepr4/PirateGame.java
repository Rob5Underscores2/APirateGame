package uk.ac.york.sepr4;

import com.badlogic.gdx.Game;
import lombok.Getter;

public class PirateGame extends Game {

	private MenuScreen menuScreen;
	@Getter
	private GameScreen gameScreen;

	private UpgradeScreen upgradeScreen;

	public static PirateGame PIRATEGAME;
	
	@Override
	public void create () {
		PIRATEGAME = this;
	    //switchScreen(ScreenType.MENU);
		//FOR DEVELOPMENT
		switchScreen(ScreenType.GAME);
	}

	public void gameOver() {
		gameScreen = null;
	}

	public void switchScreen(ScreenType screenType){
		switch (screenType) {
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
            case GAME:
                if(gameScreen == null) gameScreen = new GameScreen(this);
                this.setScreen(gameScreen);
                break;
			case UPGRADES:
				if(upgradeScreen == null) upgradeScreen = new UpgradeScreen(this);
				this.setScreen(upgradeScreen);
                break;

		}
	}
}
