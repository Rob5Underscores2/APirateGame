package uk.ac.york.sepr4.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import uk.ac.york.sepr4.screen.NewMinigameScreen;

public class MinigameInputProcessor implements InputProcessor {

    private NewMinigameScreen newMinigameScreen;

    public MinigameInputProcessor(NewMinigameScreen newMinigameScreen) {
        this.newMinigameScreen = newMinigameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.SPACE) {
            //start game if difficulty set but game not started
            if (newMinigameScreen.getDifficulty() != null && !newMinigameScreen.isGameStarted()) {
                newMinigameScreen.setGameStarted(true);
                return true;
            }
        }
        if(keycode == Input.Keys.Z) {
            if(newMinigameScreen.isGameStarted()) {
                //can shoot - game is started
                newMinigameScreen.playerShoot();
            }
        }
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
        return false;
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
}
