package PaooGame.Graphics;

import java.awt.image.BufferedImage;

public class Assets
{
    public static BufferedImage[][] heroWalks;
    public static BufferedImage[][] heroJump;

    public static void Init()
    {
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/PaooGameSpriteSheet.png"));
        BufferedImage jumpSheet = ImageLoader.LoadImage("/textures/jump.png");
        BufferedImage walkSheet = ImageLoader.LoadImage("/textures/walk.png");

        heroWalks = new BufferedImage[4][9]; // 4 direcții, 9 frame-uri fiecare
        for (int dir = 0; dir < 4; dir++) {
            for (int frame = 0; frame < 9; frame++) {
                heroWalks[dir][frame] = walkSheet.getSubimage(frame * 64, dir * 64, 64, 64);
            }
        }

        heroJump = new BufferedImage[4][5];
            for(int dir=0;dir<4;dir++)
            {
                for(int frame=0; frame <5; frame++)
                {
                    heroJump[dir][frame]=jumpSheet.getSubimage(frame *64, dir*64, 64, 64);
                }
            }


    }
}
