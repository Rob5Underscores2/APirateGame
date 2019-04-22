package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.io.FileManager;
import uk.ac.york.sepr4.object.building.Building;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.utils.StyleManager;

import java.util.Optional;

public class StatsHUD {

    private GameInstance gameInstance;
    @Getter
    private Stage stage;

    //Added for Assessment 3: Many labels and tables for the different features added in SailHUD
    private Label goldLabel, goldValueLabel, xpLabel, xpValueLabel, locationLabel;

    private Image hudLocation;

    @Getter
    private Table table;

    /***
     * Class responsible for storing and updating SailHUD variables.
     * Creates table which is drawn to the stage!
     * @param gameInstance from which to get SailHUD values.
     */
    public StatsHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        // Local widths and heights.

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        stage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        //background images (wooden effect) for stats
        Image hudTopLeft = new Image(FileManager.hudTopLeft);
        hudTopLeft.setY(Gdx.graphics.getHeight()-hudTopLeft.getHeight());
        stage.addActor(hudTopLeft);

        Image hudTopRight = new Image(FileManager.hudTopRight);
        hudTopRight.setY(Gdx.graphics.getHeight()-hudTopRight.getHeight());
        hudTopRight.setX(Gdx.graphics.getWidth()-hudTopRight.getWidth());
        stage.addActor(hudTopRight);

        hudLocation = new Image(FileManager.hudMiddle);
        hudLocation.setX((Gdx.graphics.getWidth()-hudLocation.getWidth())/2);
        hudLocation.setY(Gdx.graphics.getHeight()-hudLocation.getHeight());
        hudLocation.setVisible(false);
        stage.addActor(hudLocation);

        //define a table used to organize our sailHud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //table.debug();

        //stats hud images
        Image hudGold = new Image(FileManager.hudGold);
        hudGold.setScaling(Scaling.fit);
        Image hudLevel = new Image(FileManager.hudLevel);
        hudLevel.setScaling(Scaling.fit);

        //set default label values
        goldValueLabel = new Label("0", StyleManager.generateLabelStyle(35, Color.BLACK));
        goldValueLabel.setAlignment(Align.center);

        xpValueLabel = new Label("0", StyleManager.generateLabelStyle(35, Color.BLACK));
        xpValueLabel.setAlignment(Align.center);

        locationLabel = new Label("", StyleManager.generateLabelStyle(30, Color.BLACK));
        locationLabel.setAlignment(Align.center);

        table.add(hudGold)
                .minWidth(Value.percentWidth(0.05f, table))
                .expandX();
        table.add(goldValueLabel)
                .minWidth(Value.percentWidth(0.05f, table))
                .expandX();
        table.add(locationLabel)
                .width(Value.percentWidth(0.80f, table))
                .expandX();
        table.add(xpValueLabel)
                .width(Value.percentWidth(0.05f, table))
                .expandX();
        table.add(hudLevel)
                .width(Value.percentWidth(0.05f, table))
                .expandX();

        stage.addActor(table);

    }

    /***
     * Update label values - called during stage render
     */
    public void update() {
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();

        //balance and xp overheads
        goldValueLabel.setText(""+player.getBalance());
        //+" ("+(player.getLevelProgress())*100+"%)"
        xpValueLabel.setText(""+player.getLevel());

        Optional<Building> loc = gameInstance.getEntityManager().getPlayerLocation();
        if(loc.isPresent()) {
            Building building = loc.get();
            locationLabel.setText(building.getName().toUpperCase());
            hudLocation.setVisible(true);
        } else {
            locationLabel.setText("");
            hudLocation.setVisible(false);
        }

        stage.act();
        stage.draw();

    }

}
