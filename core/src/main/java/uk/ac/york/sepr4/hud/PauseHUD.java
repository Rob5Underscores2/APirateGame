package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.utils.StyleManager;

public class PauseHUD {
    private GameInstance gameInstance;
    @Getter
    private Stage stage;
    @Getter
    private Table table, objectiveTable;

    /***
     * Class responsible for storing and updating SailHUD variables.
     * Creates table which is drawn to the stage!
     * @param sailScreen instance of SailScreen from which to get SailHUD values.
     */
    public PauseHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        // Local widths and heights.

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        stage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        createTable();
        createObjectiveTable();

    }

    private void createObjectiveTable() {
        objectiveTable = new Table();
        objectiveTable.top();
        objectiveTable.setFillParent(true);

        //TODO: Add objective view

        stage.addActor(objectiveTable);
    }
    private void createTable() {
        //define a table used to organize our sailHud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //Added for Assessment 3: Menu button
        TextButton btnMenu = new TextButton("Quit", StyleManager.generateTBStyle(30, Color.RED, Color.GRAY));
        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameInstance.switchScreen(gameInstance.getGame().getMenuScreen());
            }
        });

        //Assessment 3: print pause during paused state
        Label pausedLabel = new Label("PAUSED", StyleManager.generateLabelStyle(50, Color.BLACK));

        table.add(pausedLabel)
                .expandX()
                .padTop(Value.percentHeight(0.05f, table));
        table.row();
        table.add(btnMenu).expandX();

        stage.addActor(table);

    }
    /***
     * Update label values - called during stage render
     */
    public void update() {

        stage.act();
        stage.draw();
    }

}
