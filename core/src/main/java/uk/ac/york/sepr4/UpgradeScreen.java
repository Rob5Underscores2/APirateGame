package uk.ac.york.sepr4;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.Player;

public class UpgradeScreen implements Screen {
    private PirateGame pirateGame;
    private Stage stage;

    private EntityManager entityManager;

    int newCannonBallDamage = 0;



    public UpgradeScreen(PirateGame pirateGame) {
        this.pirateGame = pirateGame;

        // create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        entityManager = new EntityManager(pirateGame.getGameScreen());

    }

    @Override
    public void show() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));


        //create buttons
/*        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);*/

        TextButton upgradeShipSpeedButton = new TextButton("Upgrade ship speed Required: " + pirateGame.getGameScreen().shipSpeedUpgradeCost + "gold [press 1]", skin);
        TextButton decreaseCannonBallDamageButton = new TextButton("<-", skin);
        TextButton confirmButton = new TextButton("CONFIRM", skin);

        //add buttons to table
        table.add(upgradeShipSpeedButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(decreaseCannonBallDamageButton).fillX().uniformX();
        table.row();
        table.add(confirmButton).fillX().uniformX();



        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when teh screen size is changed
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
