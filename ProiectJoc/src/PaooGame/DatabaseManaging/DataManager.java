package PaooGame.DatabaseManaging;

import java.nio.file.AccessDeniedException;

public interface DataManager {
    public int load(String key, boolean access) throws AccessDeniedException,IllegalArgumentException;
    public void store(String key, int value, boolean access ) throws AccessDeniedException,IllegalArgumentException;
    public void resetBuffer(boolean access)throws AccessDeniedException; //metoda asta pune bufferul de date pe 0 complet, practic

    public void loadBuffer(boolean access) throws AccessDeniedException;
    public void storeBuffer(boolean access) throws AccessDeniedException, IllegalArgumentException ;
}
