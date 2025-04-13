package PaooGame.Tiles;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class TileCache {
    private static final Map<Entry<String,Integer>,Tile> cache = new HashMap<>();
    //mapare context-id-tileCuTextura
    private static final Map<String,BufferedImage> tilesheets=new HashMap<>();


    public Tile getTile(String path, int id){
        Entry<String,Integer> key=new AbstractMap.SimpleEntry<>(path,id);
        if(cache.containsKey(key)){
            return cache.get(key);
        }

        BufferedImage sheet= tilesheets.computeIfAbsent(path,p->{
            try{
                File f= new File(p);
                if(!f.exists()){
                    System.err.println("Error! tilesheet not found" + p);
                    return null;
                }
                return ImageIO.read(f);
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        });

        if(sheet==null){
            System.err.println("Error: could not get tilesheet for path: " +path);
            return null;
        }


        Tile tile=Tile.loadTile(sheet,id);
        if(tile!=null){
            cache.put(key,tile);
        }
        else{
            System.err.println("Tile with id: "+id + "from sheet: "+path+" did not load correctly");
        }
        return tile;

    }
   //vreau sa le pastrez in hash map cu source - id -->> tile cu loaded texture
    // la find vreau sa dau sursa si id-ul si sa imi returneze tile-ul sau sa creeze unul nou si sa il returneze
}
