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

        Label currentCannonBallDamageLabel = new Label("CURRENT CANNON BALL DAMAGE: " + " X ", skin); //will show the current cannonBall damage once I know how to access and change it
        Label newCannonBallDamageLabel = new Label(String.format("NEW CANNON BALL DAMAGE: " + "%03d", newCannonBallDamage), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        TextButton increaseCannonBallDamageButton = new TextButton("->", skin);
        TextButton decreaseCannonBallDamageButton = new TextButton("<-", skin);
        TextButton confirmButton = new TextButton("CONFIRM", skin);

        //add buttons to table
        table.add(currentCannonBallDamageLabel).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(newCannonBallDamageLabel).fillX().uniformX().center();
        table.row().uniform();
        table.add(decreaseCannonBallDamageButton).width(100);
        table.add(increaseCannonBallDamageButton).width(100);
        table.row().uniform();
        table.add(confirmButton);


        // create button listeners
        increaseCannonBallDamageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //newCannonBallDamage will increment
                newCannonBallDamage+= 10;
                newCannonBallDamageLabel.setText(String.format("NEW CANNONBALL DAMAGE: " + "%03d", newCannonBallDamage));
            }
        });

        decreaseCannonBallDamageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //only decrements the new cannonBallDamage if it is greater than 0 -> might need improving later
                if(newCannonBallDamage > 0){
                    newCannonBallDamage -= 10;
                    newCannonBallDamageLabel.setText(String.format("NEW CANNONBALL DAMAGE: " + "%03d", newCannonBallDamage));
                }
            }
        });

        //once the user presses confirm the changes will be applied
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //change the amount of gold user has
                //change the amount of damage the cannonBall does
            }
        });


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
