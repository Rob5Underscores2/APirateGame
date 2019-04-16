package uk.ac.york.sepr4.object.entity.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;
import uk.ac.york.sepr4.utils.AIUtil;

@Data
public class NPCMonster extends NPCEntity {

    public NPCMonster(Texture texture, Vector2 pos, float difficulty) {
        super(texture, pos, difficulty);
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
        super.act(deltaTime);
    }


}
