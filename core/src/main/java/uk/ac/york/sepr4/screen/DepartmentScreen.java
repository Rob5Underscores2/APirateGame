package uk.ac.york.sepr4.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import uk.ac.york.sepr4.GameInstance;
import uk.ac.york.sepr4.io.FileManager;
import uk.ac.york.sepr4.object.building.Department;

public class DepartmentScreen extends PirateScreen {

    private Department department;

    public DepartmentScreen(GameInstance gameInstance, Department department) {
        super(gameInstance, new Stage(new ScreenViewport()), FileManager.menuScreenBG);
        this.department = department;

        setEnableStatsHUD(true);

        createShopMenu();
    }

    @Override
    public void renderInner(float delta) {

    }

    private void createShopMenu() {

    }
}
