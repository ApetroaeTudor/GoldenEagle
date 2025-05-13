package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Entities.Entity;
import PaooGame.Config.Constants;
import PaooGame.RefLinks;

import java.awt.*;

/*! \class public class AboutState extends State
    \brief Implementeaza notiunea de credentiale (about)
 */
public class AboutState extends State
{
    /*! \fn public AboutState(RefLinks refLink)
        \brief Constructorul de initializare al clasei.

        \param refLink O referinta catre un obiect "shortcut", obiect ce contine o serie de referinte utile in program.
     */
    protected String stateName = Constants.ABOUT_STATE;
    public AboutState(RefLinks refLink)
    {
        ///Apel al constructorului clasei de baza.
        super(refLink);
    }

    @Override
    public String getStateName(){
        return stateName;
    }

    @Override
    public void setEnemy(Enemy enemy) {

    }

    /*! \fn public void Update()
        \brief Actualizeaza starea curenta a meniu about.
     */
    @Override
    public void update()
    {

    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza (randeaza) pe ecran starea curenta a meniu about.

        \param g Contextul grafic in care trebuie sa deseneze starea jocului pe ecran.
     */
    @Override
    public void draw(Graphics g)
    {

    }

    @Override
    public void restoreState() {

    }
}
