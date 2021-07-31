package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: ImageUnion
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description:
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUnion {

    //public static BufferedImage background;
    public static BufferedImage Image_background;
    public static BufferedImage Image_LOGE;
    public static BufferedImage Image_herobullet;
    public static BufferedImage Image_enemybullet;
    public static BufferedImage Image_boom_1;
    public static BufferedImage Image_boom_2;
    public static BufferedImage Image_boom_3;
    public static BufferedImage Image_boss;
    public static BufferedImage Image_bossbullet;
    public static BufferedImage Image_plane_1;
    public static BufferedImage Image_plane_2;
    public static BufferedImage Image_plane_3;
    public static BufferedImage Image_plane_4;
    public static BufferedImage Image_plane_5;
    public static BufferedImage Image_heroplane;
    public static BufferedImage Image_gameover;


    static {
        try {
            Image_background = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/background.jpg"));
            Image_LOGE = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/LOGE.gif"));
            Image_herobullet = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/HeroBullet.png"));
            Image_enemybullet = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/EnemyBullet.png"));
            Image_boom_1 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/boom1.png"));
            Image_boom_2 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/boom2.png"));
            Image_boom_3 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/boom3.png"));
            Image_boss = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/boss.png"));
            Image_bossbullet = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/bossbullet.png"));
            Image_plane_1 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/plane1.png"));
            Image_plane_2 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/plane2.png"));
            Image_plane_3 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/plane3.png"));
            Image_plane_4 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/plane4.png"));
            Image_plane_5 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/plane5.png"));
            Image_gameover = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/gameover.jpg"));
            Image_heroplane = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/heroplane.png"));

        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }
}
