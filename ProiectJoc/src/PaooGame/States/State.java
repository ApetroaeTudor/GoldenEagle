package PaooGame.States;

import java.awt.*;

import PaooGame.Entities.Enemy;
import PaooGame.RefLinks;

public abstract class State
{
    protected String stateName = "";

    private static State previousState  = null; /*!< Referinta catre starea anterioara a jocului.*/
    private static State currentState   = null; /*!< Referinta catre starea curenta a jocului: game, meniu, settings, about etc.*/
    protected RefLinks reflink;
    public State(RefLinks refLink)
    {
        this.reflink = refLink;
    }

    public static void setState(State state)
    {
        previousState = currentState;
        currentState = state;
    }

    public static State getState()
    {
        return currentState;
    }


    public abstract String getStateName();
    public abstract void setEnemy(Enemy enemy);

    public abstract void update();
    public abstract void draw(Graphics g);

    public abstract void restoreState();

    public abstract void loadState(boolean access);
    public abstract void storeState(boolean access);


}
