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
        int levelWidth = 0;
        int levelHeight = 0;
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
            case Constants.TIMESTAMP:
                if(value<0){
                    throw new IllegalArgumentException("Invalid argument given.\nThe timestamp epoch time value should always be positive.\nSave failed..");
                }
                break;
            case Constants.HERO_X:
                if(this.concreteDataManager == null){
                    this.concreteDataManager =ConcreteDataManager.getInstance(this.reflink);
                }
                switch (this.concreteDataManager.load(Constants.CURRENT_STATE,true)){
                    case 1:
                        levelWidth = Constants.LEVEL1_PIXEL_WIDTH;
                        break;
                    case 2:
                        levelWidth = Constants.LEVEL2_PIXEL_WIDTH;
                        break;
                    case 3:
                        levelWidth = Constants.LEVEL3_PIXEL_WIDTH;
                        break;
                    default:
                        throw new IllegalArgumentException("Can't extract level data in order to check player x and y.\n Please first save the CURRENT_STATE.\nSave failed..");
                }
                if(value<0 || value > levelWidth){
                    throw new IllegalArgumentException("The given player X is invalid.\nSave failed..");
                }
                break;
            case Constants.HERO_Y:
                if(this.concreteDataManager == null){
                    this.concreteDataManager =ConcreteDataManager.getInstance(this.reflink);
                }
                switch (this.concreteDataManager.load(Constants.CURRENT_STATE,true)){
                    case 1:
                        levelHeight = Constants.LEVEL1_PIXEL_HEIGHT;
                        break;
                    case 2:
                        levelHeight = Constants.LEVEL2_PIXEL_HEIGHT;
                        break;
                    case 3:
                        levelHeight = Constants.LEVEL3_PIXEL_HEIGHT;
                        break;
                    default:
                        throw new IllegalArgumentException("Can't extract level data in order to check player x and y.\n Please first save the CURRENT_STATE.\nSave failed..");
                }
                if(value<0 || value > levelHeight){
                    throw new IllegalArgumentException("The given player Y is invalid.\nSave failed..");
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
        this.concreteDataManager.resetBuffer(true);
    }

}
