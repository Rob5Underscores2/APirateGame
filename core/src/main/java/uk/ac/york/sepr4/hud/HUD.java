package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import uk.ac.york.sepr4.GameScreen;
import uk.ac.york.sepr4.object.building.Building;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.building.Department;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.object.quest.QuestManager;

import java.util.Optional;

public class HUD {

    private GameScreen gameScreen;
    private QuestManager questManager;

    private Label goldLabel, goldValueLabel, xpLabel, xpValueLabel, locationLabel, questLabel, captureStatus;
    public  TextButton upgradeShipSpeedButton, upgradeshipHealthButton, upgradeShipDamageButton;

    @Getter
    private Table table;
    private Stage stage;

    private long endMessageShowTime, startMessageShowTime, gameStartTime;

    /***
     * Class responsible for storing and updating HUD variables.
     * Creates table which is drawn to the stage!
     * @param gameScreen instance of GameScreen from which to get HUD values.
     */
    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.questManager = gameScreen.getQuestManager();
        //Amount of time in ms to show the end of quest message
        this.gameStartTime = System.currentTimeMillis();
        this.endMessageShowTime = 3000;
        this.startMessageShowTime = 3000;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        //define a table used to organize our hud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("default_skin/uiskin.json"));

        //set default label values
        goldLabel = new Label("GOLD", new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        goldValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        xpLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        xpValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        locationLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));
        captureStatus = new Label("", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));

        questLabel = new Label("Test", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));


        //NEW: on-screen buttons that will signal the user to upgrade their ship
        upgradeShipSpeedButton =  new TextButton("Upgrade ship speed - Required: " + gameScreen.shipSpeedUpgradeCost + "gold [press 1]", skin);
        upgradeshipHealthButton =  new TextButton("Upgrade ship health - Required: " + gameScreen.shipHealthUpgradeCost + "gold [press 2]", skin);
        upgradeShipDamageButton =  new TextButton("Upgrade cannon damage - Required: " + gameScreen.shipHealthUpgradeCost + "gold [press 3]", skin);



        //UI widgets will be initialised as a table around the screen
        table.add(goldLabel).expandX().padTop(5);
        table.add(locationLabel).expandX().padTop(5);
        table.add(xpLabel).expandX().padTop(5);
        table.row();
        table.add(goldValueLabel).expandX();
        table.add(captureStatus).expandX();
        table.add(xpValueLabel).expandX();
        table.row();
        table.add(questLabel).expandX().padLeft(10); //TODO: Fix position of quest label.
        table.row();
        //NEW - new on-screen buttons will appear on the bottom left of the screen
        table.add(upgradeShipSpeedButton).expand().bottom().left();
        table.row();
        table.add(upgradeshipHealthButton).bottom().left();
        table.row();
        table.add(upgradeShipDamageButton).bottom().left();

        Gdx.input.setInputProcessor(stage);
    }

    /***
     * Update label values - called during stage render
     */
    public void update() {
        EntityManager entityManager = gameScreen.getEntityManager();
        Player player = entityManager.getOrCreatePlayer();




        //balance and xp overheads
        goldValueLabel.setText(""+player.getBalance());
        //+" ("+(player.getLevelProgress())*100+"%)"
        xpValueLabel.setText(""+player.getLevel());

        //Quest status:
        questLabel.setText(updateQuestMessage());

        //location overhead
        boolean captured = false;
        Optional<Building> loc = entityManager.getPlayerLocation();
        if(loc.isPresent()) {
            locationLabel.setText(loc.get().getName().toUpperCase());
            if(loc.get() instanceof College) {
                if (entityManager.getOrCreatePlayer().getCaptured().contains(loc.get())) {
                    captured = true;
                }
            }
            //TODO: Neaten "Allied with" label
            //Checks to see if the player is at a department, if so, it shows the department and its appropriate alliance.
            else if (loc.get() instanceof Department){
                locationLabel.setText(locationLabel.getText() + " (ALLIED WITH " + ((Department) loc.get()).getAllied().getName() + ")");
            }
        } else {
            locationLabel.setText("OPEN SEAS");
        }
        if(captured) {
            captureStatus.setText("(CAPTURED)");
        } else {
            captureStatus.setText("");
        }

    }

    /**
     * Checks to see if the most recently completed quest was completed within the time specified by the
     * endMessageShowTime variable, if it was then it returns the completed message's end message.
     * @return String containing the actual message.
     */
    private String updateQuestMessage(){
        String msg;
        //The whole method works based on comparisons between System.currentTimeMillis() and the two constants
        //start(and end)MessageShowtime
        if (System.currentTimeMillis()<this.gameStartTime+startMessageShowTime){
            //If the game has started but hasn't been running for longer than the startMessageShowTime
            msg = this.questManager.getCurrentQuest().getStartMessage();
        }
        //This section manages the quest completion message from the most recently completed quest.
        else if (this.questManager.getLastQuest() != null){
            long timeSinceLastQuestCompletion = System.currentTimeMillis() - this.questManager.getLastQuest().getTimeCompleted();
            if (timeSinceLastQuestCompletion <endMessageShowTime) {
                msg = this.questManager.getLastQuest().getEndMessage();
            }
            else if (timeSinceLastQuestCompletion < startMessageShowTime + endMessageShowTime && this.questManager.getCurrentQuest() != null){
                msg = this.questManager.getCurrentQuest().getStartMessage();
            }
            else {
                msg = ("Active Quest: " + this.questManager.getQuestStatus());
            }
        }
        else {
            msg = ("Active Quest: " + this.questManager.getQuestStatus());
        }
       return msg;
    }

}
