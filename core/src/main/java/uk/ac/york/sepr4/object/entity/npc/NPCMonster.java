package uk.ac.york.sepr4.object.entity.npc;

import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.io.FileManager;

@Data
public class NPCMonster extends NPCEntity {

    private Integer spriteFrame = 1;
    private float spriteUpdate = 0.05f;

    public NPCMonster(Vector2 pos, float difficulty) {
        super(FileManager.krackenFrames(1), pos, difficulty);
    }

    /***
     *  This is the control logic of the NPCs AI. It uses functions from mainly AIUtil to be able to make decisions on how it is meant to behave.
     *  They are broken down into sections as to be able to make the code and control structure easier to read.
     *  When calling this function it will actually make the NPC that is in the world do the actions.
     *
     * @param deltaTime time since last act
     */
    public void act(float deltaTime) {
        //AIUtil.actNPCBoat(this, deltaTime); //need actnpcmonster
        upateKrakenSprite(deltaTime);
        super.act(deltaTime);
    }

    private void upateKrakenSprite(float delta) {
        if(spriteUpdate <= delta) {
            spriteUpdate = 0.05f;
            if (spriteFrame == 17) {
                spriteFrame = 1;
            } else {
                spriteFrame++;
            }
            setTexture(FileManager.krackenFrames(spriteFrame));
        } else {
            spriteUpdate-=delta;
        }
    }


}
