package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import lombok.Getter;
import uk.ac.york.sepr4.GameInstance;
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

    @Getter
    private Table table;

    /***
     * Class responsible for storing and updating SailHUD variables.
     * Creates table which is drawn to the stage!
     * @param sailScreen instance of SailScreen from which to get SailHUD values.
     */
    public StatsHUD(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        // Local widths and heights.

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        stage = new Stage(new FitViewport(w, h, new OrthographicCamera()));

        //define a table used to organize our sailHud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //set default label values
        goldLabel = new Label("GOLD", StyleManager.generateLabelStyle(25, Color.GOLD));
        goldLabel.setAlignment(Align.center);
        goldValueLabel = new Label("0", StyleManager.generateLabelStyle(25, Color.BLACK));

        xpLabel = new Label("LEVEL", StyleManager.generateLabelStyle(25, Color.GREEN));
        xpLabel.setAlignment(Align.center);
        xpValueLabel = new Label("0", StyleManager.generateLabelStyle(25, Color.BLACK));

        locationLabel = new Label("", StyleManager.generateLabelStyle(30, Color.MAGENTA));
        locationLabel.setAlignment(Align.center);

        table.add(goldLabel)
                .minWidth(Value.percentWidth(0.25f, table))
                .expandX()
                .padTop(5);
        table.add(locationLabel)
                .width(Value.percentWidth(0.50f, table))
                .expandX()
                .padTop(5);
        table.add(xpLabel)
                .width(Value.percentWidth(0.25f, table))
                .expandX()
                .padTop(5);
        table.row();
        table.add(goldValueLabel).expandX();
        table.add();
        table.add(xpValueLabel).expandX();
        table.row();
        table.debug();
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
        } else {
            locationLabel.setText("");
        }

        stage.act();
        stage.draw();

    }

}
