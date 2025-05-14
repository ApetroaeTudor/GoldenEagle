package PaooGame.DatabaseManaging;


import PaooGame.RefLinks;
import java.sql.*;

public class DatabaseManager {
        private static DatabaseManager instance = null;
        private RefLinks reflink;
        private DataManager concreteDataManager;

        private Connection c;
        private Statement stmt;


        public static DatabaseManager getInstance(RefLinks reflink){
            if(DatabaseManager.instance == null){
                DatabaseManager.instance = new DatabaseManager(reflink);
            }
            return DatabaseManager.instance;
        }

        private DatabaseManager(RefLinks reflink){
            this.reflink = reflink;
        }
}
