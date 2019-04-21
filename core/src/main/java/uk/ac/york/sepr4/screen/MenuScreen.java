package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.APirateGame;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.io.FileManager;
import uk.ac.york.sepr4.utils.StyleManager;

public class MenuScreen implements Screen {

    private APirateGame game;

    private Stage stage;

    public MenuScreen(APirateGame APirateGame) {
        this.game = APirateGame;

        this.stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        createTable();
    }


    private void createTable() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //TODO: LOGO!

        //create buttons
        TextButton newGame = new TextButton("New Game", StyleManager.generateTBStyle(30, Color.GREEN, Color.GRAY));
        TextButton exit = new TextButton("Exit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));

        //add buttons to table
        table.add(newGame).fillX().uniformX();
        table.row().pad(10,0,10,0);
        table.add(exit).fillX().uniformX();

        // create button listeners
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Changed for Assessment 3: Added a resume button to the menu
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameInstance gameInstance = new GameInstance(game);
                gameInstance.start();
            }
        });

    }

    /***
     * Draw screen's background.
     */
    private void drawMenuBackground() {
        //sets background texture
        stage.getBatch().begin();
        Texture texture = FileManager.mainMenuScreenBG;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        stage.getBatch().draw(texture, 0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawMenuBackground();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
