package PaooGame.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

//    private boolean[] keyboardState=new boolean[256];
    private int[] keyPressedCount=new int[256]; //numar de cate ori apas o tasta, daca o tin apasata numaratoarea creste
    //daca ii dau release, numaratoarea se pune pe 0, daca ajunge la maxim_int atunci numaratoarea stagneaza

    public KeyboardInput(){ //initializare pe 0
        for(int i=0;i<256;i++){
            keyPressedCount[i]=0;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(keyPressedCount[e.getKeyCode()]<Integer.MAX_VALUE)
            keyPressedCount[e.getKeyCode()]+=1;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressedCount[e.getKeyCode()] = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

//    public boolean[] getKeyboardState(){
//        return this.keyboardState;
//    }
    public int[] getKeyPressedCount(){
        return this.keyPressedCount;
    }
}
