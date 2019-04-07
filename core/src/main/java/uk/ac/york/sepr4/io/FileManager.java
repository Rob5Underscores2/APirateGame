package uk.ac.york.sepr4.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;

@Data
public class FileManager {


    private static final String path = "images/sprites/";

    public static Texture menuScreenBG = new Texture(Gdx.files.internal("images/screen/menu.png"));


    public static Texture ENEMY = new Texture(Gdx.files.internal(path+"enemy.png"));
    public static Texture DEADENEMY = new Texture(Gdx.files.internal(path+"deadEnemy.png"));

    public static Texture BOSS = new Texture(Gdx.files.internal(path+"boss.png"));

    public static Texture PLAYER = new Texture(Gdx.files.internal(path+"player.png"));

    public static Texture CANNONBALL = new Texture(Gdx.files.internal(path+"cannonball.png"));

    public static Texture LOOT = new Texture(Gdx.files.internal(path+"crew.png"));

    public static Texture EXPLOSION1 = new Texture(Gdx.files.internal(path+"explosion1.png"));
    public static Texture EXPLOSION2 = new Texture(Gdx.files.internal(path+"explosion2.png"));
    public static Texture EXPLOSION3 = new Texture(Gdx.files.internal(path+"explosion3.png"));

    public static Texture ORANGEFIRE = new Texture(Gdx.files.internal(path+"fire1.png"));
    public static Texture REDFIRE = new Texture(Gdx.files.internal(path+"fire2.png"));

    public static Texture MIDDLEBOATTRAIL1 = new Texture(Gdx.files.internal(path+"Middleboattrail.png"));

    public static Texture firingFrame(int number){
        return new Texture(Gdx.files.internal(path+"cannon/frame"+number+".png"));
    }

    public static Texture fire_on_boat(int number){
        return new Texture(Gdx.files.internal(path+"fire_on_boat/frame"+number+".png"));
    }

    public static Texture krackenFrame(int number) {
        return new Texture(Gdx.files.internal(path+"krackenFrames/kr_frame"+number+".png"));
    }

    private static final String minigamePath = "images/minigame/";
    public static Texture MINIGAME_PLAYER_1 = new Texture(Gdx.files.internal(minigamePath+"pirate_holstered.png"));
    public static Texture MINIGAME_PLAYER_2 = new Texture(Gdx.files.internal(minigamePath+"pirate_shooting.png"));
    public static Texture MINIGAME_ENEMY_EASY_1 = new Texture(Gdx.files.internal(minigamePath+"pirate_holstered_right_easy.png"));
    public static Texture MINIGAME_ENEMY_EASY_2 = new Texture(Gdx.files.internal(minigamePath+"pirate_shooting_right_easy.png"));
    public static Texture MINIGAME_ENEMY_MED_1 = new Texture(Gdx.files.internal(minigamePath+"pirate_holstered_right_medium.png"));
    public static Texture MINIGAME_ENEMY_MED_2 = new Texture(Gdx.files.internal(minigamePath+"pirate_shooting_right_medium.png"));
    public static Texture MINIGAME_ENEMY_HARD_1 = new Texture(Gdx.files.internal(minigamePath+"pirate_holstered_right_hard.png"));
    public static Texture MINIGAME_ENEMY_HARD_2 = new Texture(Gdx.files.internal(minigamePath+"pirate_shooting_right_hard.png"));
    public static Texture MINIGAME_ENEMY_VHARD_1 = new Texture(Gdx.files.internal(minigamePath+"pirate_holstered_right_veryhard.png"));
    public static Texture MINIGAME_ENEMY_VHARD_2 = new Texture(Gdx.files.internal(minigamePath+"pirate_shooting_right_veryhard.png"));


    //Texture Archive
    public static Texture ALCUIN_CORVETTE = new Texture(Gdx.files.internal(path+"alcuincorvetteasset.png"));
    public static Texture ALCUIN_FRIGATE = new Texture(Gdx.files.internal(path+"alcuinfrigateasset.png"));
    public static Texture ALCUIN_MANOWAR = new Texture(Gdx.files.internal(path+"alcuinmanowarasset.png"));
    public static Texture ALCUIN_SLOOP = new Texture(Gdx.files.internal(path+"alcuinsloopasset.png"));
    public static Texture CONSTANTINE_CORVETTE = new Texture(Gdx.files.internal(path+"constantinecorvetteasset.png"));
    public static Texture CONSTANTINE_FRIGATE = new Texture(Gdx.files.internal(path+"constantinefrigateasset.png"));
    public static Texture CONSTANTINE_MANOWAR = new Texture(Gdx.files.internal(path+"constantinemanowarasset.png"));
    public static Texture CONSTANTINE_SLOOP = new Texture(Gdx.files.internal(path+"constantinesloopasset.png"));
    public static Texture DERWENT_CORVETTE = new Texture(Gdx.files.internal(path+"derwentcorvetteasset.png"));
    public static Texture DERWENT_FRIGATE = new Texture(Gdx.files.internal(path+"derwentfrigateasset.png"));
    public static Texture DERWENT_MANOWAR = new Texture(Gdx.files.internal(path+"derwentmanowarasset.png"));
    public static Texture DERWENT_SLOOP = new Texture(Gdx.files.internal(path+"derwentsloopasset.png"));
    public static Texture GOODRICK_CORVETTE = new Texture(Gdx.files.internal(path+"goodrickcorvetteasset.png"));
    public static Texture GOODRICK_FRIGATE = new Texture(Gdx.files.internal(path+"goodrickfrigateasset.png"));
    public static Texture GOODRICK_MANOWAR = new Texture(Gdx.files.internal(path+"goodrickmanowarasset.png"));
    public static Texture GOODRICK_SLOOP = new Texture(Gdx.files.internal(path+"goodricksloopasset.png"));
    public static Texture HALIFAX_CORVETTE = new Texture(Gdx.files.internal(path+"halifaxcorvetteasset.png"));
    public static Texture HALIFAX_FRIGATE = new Texture(Gdx.files.internal(path+"halifaxfrigateasset.png"));
    public static Texture HALIFAX_MANOWAR = new Texture(Gdx.files.internal(path+"halifaxmanowarasset.png"));
    public static Texture HALIFAX_SLOOP = new Texture(Gdx.files.internal(path+"halifaxsloopasset.png"));
    public static Texture JAMES_CORVETTE = new Texture(Gdx.files.internal(path+"jamescorvetteasset.png"));
    public static Texture JAMES_FRIGATE = new Texture(Gdx.files.internal(path+"jamesfrigateasset.png"));
    public static Texture JAMES_MANOWAR = new Texture(Gdx.files.internal(path+"jamesmanowarasset.png"));
    public static Texture JAMES_SLOOP = new Texture(Gdx.files.internal(path+"jamessloopasset.png"));
    public static Texture LANGWITH_CORVETTE = new Texture(Gdx.files.internal(path+"langwithcorvetteasset.png"));
    public static Texture LANGWITH_FRIGATE = new Texture(Gdx.files.internal(path+"langwithfrigateasset.png"));
    public static Texture LANGWITH_MANOWAR = new Texture(Gdx.files.internal(path+"langwithmanowarasset.png"));
    public static Texture LANGWITH_SLOOP = new Texture(Gdx.files.internal(path+"langwithsloopasset.png"));
    public static Texture NEUTRAL_CORVETTE = new Texture(Gdx.files.internal(path+"alcuincorvetteasset.png"));
    public static Texture NEUTRAL_FRIGATE = new Texture(Gdx.files.internal(path+"neutralfrigateasset.png"));
    public static Texture NEUTRAL_MANOWAR = new Texture(Gdx.files.internal(path+"neutralmanowarasset.png"));
    public static Texture NEUTRAL_SLOOP = new Texture(Gdx.files.internal(path+"neutralsloopasset.png"));
    public static Texture PIRATE_CORVETTE = new Texture(Gdx.files.internal(path+"piratecorvetteasset.png"));
    public static Texture PIRATE_FRIGATE = new Texture(Gdx.files.internal(path+"piratefrigateasset.png"));
    public static Texture PIRATE_MANOWAR = new Texture(Gdx.files.internal(path+"piratemanowarasset.png"));
    public static Texture PIRATE_SLOOP = new Texture(Gdx.files.internal(path+"piratesloopasset.png"));
    public static Texture VANBROUGH_CORVETTE = new Texture(Gdx.files.internal(path+"vanbroughcorvetteasset.png"));
    public static Texture VANBROUGH_FRIGATE = new Texture(Gdx.files.internal(path+"vanbroughfrigateasset.png"));
    public static Texture VANBROUGH_MANOWAR = new Texture(Gdx.files.internal(path+"vanbroughmanowarasset.png"));
    public static Texture VANBROUGH_SLOOP = new Texture(Gdx.files.internal(path+"vanbroughsloopasset.png"));
    public static Texture WENTWORTH_CORVETTE = new Texture(Gdx.files.internal(path+"wentworthcorvetteasset.png"));
    public static Texture WENTWORTH_FRIGATE = new Texture(Gdx.files.internal(path+"wentworthfrigateasset.png"));
    public static Texture WENTWORTH_MANOWAR = new Texture(Gdx.files.internal(path+"wentworthmanowarasset.png"));
    public static Texture WENTWORTH_SLOOP = new Texture(Gdx.files.internal(path+"wentworthsloopasset.png"));
}
