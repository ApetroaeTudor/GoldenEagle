package PaooGame.DatabaseManaging;

import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ConcreteDataManager implements DataManager{

    private Map<String,Integer> buffer;
    private RefLinks reflink;

    private static ConcreteDataManager instance = null;
    public static ConcreteDataManager getInstance(RefLinks reflink){
        if(ConcreteDataManager.instance == null){
            ConcreteDataManager.instance = new ConcreteDataManager(reflink);
        }
        return instance;
    }


    private ConcreteDataManager(RefLinks reflink){
        this.reflink = reflink;
        this.buffer = new HashMap<>();
        this.buffer.put(Constants.CURRENT_STATE,-1);

        this.buffer.put(Constants.HERO_HEALTH,-1);
        this.buffer.put(Constants.HERO_X,-1);
        this.buffer.put(Constants.HERO_Y,-1);
        this.buffer.put(Constants.HERO_HAS_WHIP,-1);
        this.buffer.put(Constants.HERO_NR_OF_FLEES,-1);

        this.buffer.put(Constants.TIGER0_HEALTH,-1);
        this.buffer.put(Constants.TIGER1_HEALTH,-1);

        this.buffer.put(Constants.BASIC_SKELETON0_HEALTH,-1);
        this.buffer.put(Constants.BASIC_SKELETON1_HEALTH,-1);
        this.buffer.put(Constants.STRONG_SKELETON0_HEALTH,-1);

        this.buffer.put(Constants.BOSS_HEALTH,-1);
        this.buffer.put(Constants.MINOTAUR0_HEALTH,-1);
        this.buffer.put(Constants.MINOTAUR1_HEALTH,-1);
        this.buffer.put(Constants.GHOST0_HEALTH,-1);
        this.buffer.put(Constants.GHOST1_HEALTH,-1);

        this.buffer.put(Constants.TIMESTAMP,-1);

    }

    @Override
    public int load(String key, boolean access) {
        return this.buffer.get(key);
    }

    @Override
    public void store(String key, int value, boolean access) {
        this.buffer.put(key,value);
    }

    @Override
    public void resetBuffer(boolean access){
        this.buffer.put(Constants.CURRENT_STATE,1); //lvl1

        this.buffer.put(Constants.HERO_HEALTH,100); //full health
        this.buffer.put(Constants.HERO_X,Constants.HERO_LEVEL1_STARTING_X);
        this.buffer.put(Constants.HERO_Y,Constants.HERO_LEVEL1_STARTING_Y);
        this.buffer.put(Constants.HERO_HAS_WHIP,0);
        this.buffer.put(Constants.HERO_NR_OF_FLEES,2);

        this.buffer.put(Constants.TIGER0_HEALTH,100);
        this.buffer.put(Constants.TIGER1_HEALTH,100);

        this.buffer.put(Constants.BASIC_SKELETON0_HEALTH,100);
        this.buffer.put(Constants.BASIC_SKELETON1_HEALTH,100);
        this.buffer.put(Constants.STRONG_SKELETON0_HEALTH,100);

        this.buffer.put(Constants.BOSS_HEALTH,100);
        this.buffer.put(Constants.MINOTAUR0_HEALTH,100);
        this.buffer.put(Constants.MINOTAUR1_HEALTH,100);
        this.buffer.put(Constants.GHOST0_HEALTH,100);
        this.buffer.put(Constants.GHOST1_HEALTH,100);

        this.buffer.put(Constants.TIMESTAMP, (int)Instant.now().getEpochSecond());

        this.reflink.setDataRefreshSignal(true);

    }
}
