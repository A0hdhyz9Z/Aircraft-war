package GameObject;
/*
 * @Project_name: Aircraft war
 * @Class_name: HardEnemy
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description: 困难敌机左右摇摆的绘制
 */

import GameUI.ImageUnion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class HardEnemy extends CommonEnemy{
    /**
     * 构造方法以初始化
     *
     * @param m 横坐标
     * @param image 敌机图像
     * @param score 得分
     */
    public HardEnemy(int m, BufferedImage image, int score) {
        super(m, image, score);
    }

    /**
     * 敌机移动，能够左右摇摆，每移动20次向集合中添加一发子弹，然后绘画该敌机及发射的子弹
     *
     * @param g
     * @param bullets
     */
    @Override
    public void paint(Graphics g, LinkedList<Bullet> bullets) {
        y += 4;
        if (x % 2 == 0){
            x = x + y / 100;
        }
        else{
            x = x - y / 100;
        }
        Count++;
        if (Count % 20 == 0) {
            bullets.add(new Bullet(x, y, ImageUnion.Image_enemybullet));
        }
        g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
        Draw_bullet(g, bullets);
    }
}
