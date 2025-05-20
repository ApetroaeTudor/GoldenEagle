package PaooGame.DatabaseManaging;

import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.nio.file.AccessDeniedException;

public class ProxyDataManager implements DataManager {

    private ConcreteDataManager concreteDataManager;
    private RefLinks reflink;


    public ProxyDataManager(RefLinks reflink){
        this.reflink = reflink;
    }

    @Override
    public int load(String key, boolean access) throws AccessDeniedException,IllegalArgumentException {
        if(!access){
            throw new AccessDeniedException("Sorry, you don't have the permission to load..\n");
        }

        boolean keyFound = false;
        for(String token : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
            if(key.compareTo(token)==0){
                keyFound = true;
            }
        }
        if(!keyFound){
            throw new IllegalArgumentException("Invalid key given.\nLoad failed..");
        }

        if(this.concreteDataManager == null){
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }

        int returnVal = this.concreteDataManager.load(key,true);
        if( returnVal ==  -1){
            throw new IllegalStateException("The concrete DataManager buffer is in an invalid state.\nPlease first store some data from the database..");
        }

        return returnVal;

    }

    @Override
    public void store(String key, int value, boolean access) throws AccessDeniedException,IllegalArgumentException {
        if(!access){
            throw new AccessDeniedException("Sorry, you don't have the permission to save..\n");
        }
        switch (key){
            case Constants.CURRENT_STATE:
                if( !(value==1 || value==2 || value==3) ){
                    throw new IllegalArgumentException("Invalid level ID given.\nSave failed..");
                }
                break;
            case Constants.HERO_HEALTH:
            case Constants.TIGER0_HEALTH:
            case Constants.TIGER1_HEALTH:
            case Constants.BASIC_SKELETON0_HEALTH:
            case Constants.BASIC_SKELETON1_HEALTH:
            case Constants.STRONG_SKELETON0_HEALTH:
            case Constants.BOSS_HEALTH:
            case Constants.MINOTAUR0_HEALTH:
            case Constants.MINOTAUR1_HEALTH:
            case Constants.GHOST0_HEALTH:
            case Constants.GHOST1_HEALTH:
                if( value<0 || value > 100){
                    throw new IllegalArgumentException("Invalid argument given.\nHealth has to be between 0 and 100.\nSave failed..");
                }
                break;
            case Constants.HERO_HAS_WHIP:
                if( !(value == 0 || value ==1)){
                    throw new IllegalArgumentException("Invalid argument given.\nThis data is boolean so please insert only 0/1.\nSave failed..");
                }
                break;
            case Constants.HERO_NR_OF_FLEES:
                if( value < 0 || value >this.reflink.getHero().getMaxNrOfEscapes()){
                    throw new IllegalArgumentException("Invalid argument given.\nThe value has to be between 0 and " + this.reflink.getHero().getMaxNrOfEscapes()+".\nSave failed..");
                }
                break;
            case Constants.HERO_NR_OF_COLLECTED_SAVES:
                if( value < 0 || value >6){
                    throw new IllegalArgumentException("Invalid argument given.\nThe value has to be between 0 and  6.\nSave failed..");
                }
                break;
            case Constants.HERO_NR_OF_FINISHED_LEVELS:
                if(!(value == 0 || value == 1 || value ==2 || value == 3)){
                    throw new IllegalArgumentException("Invalid argument given.\nThe value has to be between 0 and  3.\nSave failed..");
                }
                break;
            case Constants.HERO_GOLD:
                if(value<0){
                    throw new IllegalArgumentException("Invalid amount of gold!\nIt has to be positive!\nSave failed..");
                }
                break;
            case Constants.HERO_X:
            case Constants.HERO_Y:
            case Constants.TIMESTAMP:
                if(value<0){
                    throw new IllegalArgumentException("Invalid argument given.\nThe timestamp epoch time value should always be positive.\nSave failed..");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid key given.\nSave failed..");
        }

        if (this.concreteDataManager == null){
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        this.concreteDataManager.store(key,value,true);
    }

    @Override
    public void resetBuffer(boolean access) throws AccessDeniedException {
        if(!access){
            throw new AccessDeniedException("Sorry, you don't have the permission to access the Concrete Data Manager..\n");
        }
        if (this.concreteDataManager == null){
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }
        this.reflink.getGame().getLevel1State().restoreState();
        this.reflink.getGame().getLevel2State().restoreState();
        this.reflink.getGame().getLevel3State().restoreState();
        this.concreteDataManager.resetBuffer(true);
    }

    @Override
    public void loadBuffer(boolean access) throws AccessDeniedException {
        if(!access){
            throw new AccessDeniedException("Sorry, you don't have the permission to access the Concrete Data Manager..\n");
        }

        if(this.concreteDataManager == null){
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }

        this.concreteDataManager.loadBuffer(access);
    }

    @Override
    public void storeBuffer(boolean access) throws AccessDeniedException, IllegalArgumentException {
        if(!access){
            throw new AccessDeniedException("Sorry, you don't have the permission to access the Concrete Data Manager..\n");
        }
        if(this.concreteDataManager == null){
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }

        boolean invalidData=false;

        for(String key : Constants.ALL_DATA_MANAGEMENT_CONSTANTS){
            if(this.concreteDataManager.load(key,access)==-1){
                invalidData = true;
            }
        }

        if(invalidData){
            throw new IllegalArgumentException("Can't currently store data in the database.\nSome values are invalid.\nStoring failed..");
        }

        this.concreteDataManager.storeBuffer(access);
    }

    @Override
    public int[] loadScore(boolean access) throws AccessDeniedException {
            if(!access){
                throw new AccessDeniedException("Sorry, you don't have permision to load scores from the database..\n");
            }

            if(this.concreteDataManager == null){
                this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
            }

            return this.concreteDataManager.loadScore(access);

    }

    @Override
    public void storeScore(boolean access, int score1, int score2, int score3) throws AccessDeniedException, IllegalArgumentException {
        if(!access){
            throw new AccessDeniedException("Sorry, you don't have permision to store scores into the database..\n");
        }

        if(score1<0 || score1>999 || score2<0 || score2>999 || score3<0 || score3>999){
            throw new IllegalArgumentException("Please insert scores between 0 and 999..\n");
        }

        if(this.concreteDataManager == null){
            this.concreteDataManager = ConcreteDataManager.getInstance(this.reflink);
        }

       this.concreteDataManager.storeScore(access,score1,score2,score3);
    }

}
