package uk.ac.york.sepr4.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import uk.ac.york.sepr4.GameScreen;
import uk.ac.york.sepr4.object.building.Building;
import uk.ac.york.sepr4.object.building.College;
import uk.ac.york.sepr4.object.entity.EntityManager;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.object.quest.QuestManager;

import java.util.Optional;

public class HUD {

    private GameScreen gameScreen;
    private QuestManager questManager;

    private Label goldLabel, goldValueLabel, xpLabel, xpValueLabel, locationLabel, questLabel, captureStatus;
    @Getter
    private Table table;

    private long lastQuestCompletion;
    private long endMessageShowTime;

    /***
     * Class responsible for storing and updating HUD variables.
     * Creates table which is drawn to the stage!
     * @param gameScreen instance of GameScreen from which to get HUD values.
     */
    public HUD(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.questManager = gameScreen.getQuestManager();
        //Amount of time in ms to show the end of quest message
        this.endMessageShowTime = 5000;

        //define a table used to organize our hud's labels
        table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //set default label values
        goldLabel = new Label("GOLD", new Label.LabelStyle(new BitmapFont(), Color.GOLD));
        goldValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        xpLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        xpValueLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        locationLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));
        captureStatus = new Label("", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));

        questLabel = new Label("Test", new Label.LabelStyle(new BitmapFont(), Color.MAGENTA));

        table.add(goldLabel).expandX().padTop(5);
        table.add(locationLabel).expandX().padTop(5);
        table.add(xpLabel).expandX().padTop(5);
        table.row();
        table.add(goldValueLabel).expandX();
        table.add(captureStatus).expandX();
        table.add(xpValueLabel).expandX();
        table.row();
        table.add(questLabel).expandX().padLeft(10); //TODO: Fix position of quest label.

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
        if (this.questManager.getLastQuest() != null){
            long timeSinceLastQuestCompletion = System.currentTimeMillis() - this.questManager.getLastQuest().getTimeCompleted();
            if (timeSinceLastQuestCompletion <endMessageShowTime) {
                msg = this.questManager.getLastQuest().getEndMessage();
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
