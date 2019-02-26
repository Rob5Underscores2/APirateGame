package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.object.building.Building;
import uk.ac.york.sepr4.object.building.Department;
import uk.ac.york.sepr4.object.building.MinigameBuilding;
import uk.ac.york.sepr4.screen.SailScreen;

import java.util.Optional;

public class MessageHUD {

    private GameInstance gameInstance;

    @Getter
    private Stage stage;


    //Added for Assessment 3: Many labels and tables for the different features added in SailHUD
    private Label inDerwentBeforeEndLabel, departmentPromptLabel, minigamePromptLabel;

    @Getter
    private Table table, inDerwentBeforeEndTable, departmentPromptTable, minigamePromptTable;

    /***
     * Class responsible for storing and updating SailHUD variables.
     * Creates table which is drawn to the stage!
     * @param sailScreen instance of SailScreen from which to get SailHUD values.
     */
    public MessageHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        // Local widths and heights.

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        stage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        createTable();
    }

    private void createTable() {
        //define a table used to organize our sailHud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        // Assessment 3: Add the department prompt
        departmentPromptLabel = new Label("E to enter Department", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        departmentPromptTable = new Table();
        departmentPromptTable.center();
        departmentPromptTable.setFillParent(true);
        departmentPromptTable.add(departmentPromptLabel).padBottom(100).expandX();

        // Assessment 3: Minigame screen
        minigamePromptLabel = new Label("E to enter Minigame", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        minigamePromptTable = new Table();
        minigamePromptTable.center();
        minigamePromptTable.setFillParent(true);
        minigamePromptTable.add(minigamePromptLabel).padBottom(100).expand();

        stage.addActor(table);
        stage.addActor(departmentPromptTable);
        stage.addActor(minigamePromptTable);
    }

    /***
     * Update label values - called during stage render
     */
    public void update() {
        //location overhead
        Optional<Building> loc = gameInstance.getEntityManager().getPlayerLocation();
        departmentPromptTable.setVisible(false);
        minigamePromptLabel.setVisible(false);
        if (loc.isPresent()) {
            Building building = loc.get();
            if (building instanceof Department) {
                departmentPromptTable.setVisible(true);
            } else if (building instanceof MinigameBuilding) {
                minigamePromptLabel.setVisible(true);
            }
        }

        stage.act();
        stage.draw();
    }

}
