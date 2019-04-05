package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.crew.CrewMember;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.utils.StyleManager;

public class PauseHUD {
    private GameInstance gameInstance;
    @Getter
    private Stage stage;
    /***
     * Class responsible for storing and updating PauseHUD variables.
     * Creates table which is drawn to the stage!
     * @param gameInstance from which to get PauseHUD variables.
     */
    public PauseHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        // Local widths and heights.

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        stage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        createTable();
        createCollegeTable();
        createCrewTable();
    }

    private void createCollegeTable() {
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();
        Table objectiveTable = new Table();
        objectiveTable.top();
        objectiveTable.setFillParent(true);

        Label collegesHeader = new Label("Colleges", StyleManager.generateLabelStyle(40, Color.BLACK));
        objectiveTable.add(collegesHeader).padTop(Value.percentHeight(0.05f, objectiveTable)).expandX();
        objectiveTable.add().padRight(Value.percentWidth(0.75f, objectiveTable)).expandX();

        for(College college : gameInstance.getBuildingManager().getColleges()) {
            boolean isCaptured = player.getCaptured().contains(college);
            Label collegeLabel = new Label(college.getName(), StyleManager.generateLabelStyle(30, (isCaptured ? Color.GREEN : Color.RED)));
            objectiveTable.row();
            objectiveTable.add(collegeLabel).padTop(Value.percentHeight(0.02f, objectiveTable)).expandX();
        }

        stage.addActor(objectiveTable);
    }

    private void createCrewTable() {
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();
        Table crewTable = new Table();
        crewTable.top();
        crewTable.setFillParent(true);

        crewTable.add().padLeft(Value.percentWidth(0.75f, crewTable)).expandX();
        Label crewHeader = new Label("Crew Members", StyleManager.generateLabelStyle(40, Color.BLACK));
        crewTable.add(crewHeader).padTop(Value.percentHeight(0.05f, crewTable)).expandX();
        Gdx.app.log("CMS", ""+player.getCrewMembers().size());
        for(CrewMember crew : player.getCrewMembers()) {
            Label crewLabel = new Label(crew.getName()+" : "+crew.getLevel()+"/"+crew.getMaxLevel(),
                    StyleManager.generateLabelStyle(30, Color.GOLD));
            crewTable.row();
            crewTable.add();
            crewTable.add(crewLabel).padTop(Value.percentHeight(0.02f, crewTable)).expandX();
        }

        stage.addActor(crewTable);
    }

    private void createTable() {
        //define a table used to organize our sailHud's labels
        Table table = new Table();
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
        stage.clear();

        createTable();
        createCollegeTable();
        createCrewTable();

        stage.act();
        stage.draw();
    }

}
