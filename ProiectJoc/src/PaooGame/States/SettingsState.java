package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Entities.Entity;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

public class SettingsState extends State
{
    protected String stateName = Constants.SETTINGS_STATE;
    public SettingsState(RefLinks refLink)
    {
        ///Apel al construcotrului clasei de baza.
        super(refLink);
    }

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Enemy enemy) {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void draw(Graphics g)
    {

    }

    @Override
    public void restoreState() {

    }

    @Override
    public void loadState(boolean access) {

    }

    @Override
    public void storeState(boolean access) {

    }
}