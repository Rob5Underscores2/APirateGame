package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.io.FileManager;
import uk.ac.york.sepr4.object.building.Department;
import uk.ac.york.sepr4.object.crew.CrewMember;
import uk.ac.york.sepr4.object.entity.Player;
import uk.ac.york.sepr4.utils.StyleManager;

public class DepartmentScreen extends PirateScreen {

    private Department department;
    private GameInstance gameInstance;

    public DepartmentScreen(GameInstance gameInstance, Department department) {
        super(gameInstance, new Stage(new ScreenViewport()), FileManager.departmentScreenBG);
        this.gameInstance = gameInstance;
        this.department = department;

        setEnableStatsHUD(true);

        createShopMenu();
    }

    @Override
    public void renderInner(float delta) {

    }

    private void createShopMenu() {
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Label welcome = new Label("Welcome to the "+department.getName()+" Department!",
                StyleManager.generateLabelStyle(30, Color.GOLD));
        TextButton repair = new TextButton("Click to repair your ship for "+getHealCost()+ " gold!", StyleManager.generateTBStyle(25, Color.GREEN, Color.GRAY));
        repair.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if(player.getBalance()>=getHealCost()) {
                    //has enough gold
                    player.deductBalance(getHealCost());
                    player.setHealth(player.getMaxHealth());
                }
            }
        });

        TextButton exit = new TextButton("Exit!", StyleManager.generateTBStyle(20, Color.RED, Color.GRAY));
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                gameInstance.fadeSwitchScreen(gameInstance.getSailScreen());
            }
        });

        Label noUpgrade = new Label("You have not unlocked this crew member!", StyleManager.generateLabelStyle(25, Color.BLACK));

        CrewMember crewMember = department.getCrewMember();
        TextButton upgrade = new TextButton("Upgrade " + crewMember.getName() + " for "+crewMember.getUpgradeCost()+" gold!",
                StyleManager.generateTBStyle(25, Color.BLUE, Color.GRAY));
        upgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ev, float x, float y) {
                if(player.getBalance()>=crewMember.getUpgradeCost()) {
                    //has enough gold
                    player.deductBalance(crewMember.getUpgradeCost());
                    crewMember.upgrade();
                }
            }
        });

        table.add(welcome).padTop(Value.percentHeight(0.05f, table)).expandX();
        table.row();
        table.add(repair).padTop(Value.percentHeight(0.02f, table)).expandX();
        table.row();
        if(player.getCrewMembers().contains(crewMember)) {
            //has unlocked crew member
            table.add(upgrade).padTop(Value.percentHeight(0.02f, table)).expandX();;
        } else {
            table.add(noUpgrade).padTop(Value.percentHeight(0.02f, table)).expandX();
        }
        table.row();
        table.add(exit).padTop(Value.percentHeight(0.02f, table)).expandX();

        getStage().addActor(table);
    }

    private Integer getHealCost() {
        Player player = gameInstance.getEntityManager().getOrCreatePlayer();
        return (int)Math.round(department.getHealCost()*(player.getMaxHealth()-player.getHealth()));
    }
}
