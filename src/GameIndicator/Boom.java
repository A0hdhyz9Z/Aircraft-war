package GameIndicator;
/*
 * @Project_name: Aircraft war
 * @Class_name: Boom
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description: 爆炸的图像及音效的绘制
 */

import GameUI.ImageUnion;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Thread.sleep;

public class Boom {
    int x;
    int y;
    public int count = 0;
    BufferedImage[] images = {
            ImageUnion.Image_boom_1, ImageUnion.Image_boom_1,
            ImageUnion.Image_boom_2, ImageUnion.Image_boom_2,
            ImageUnion.Image_boom_3, ImageUnion.Image_boom_3, ImageUnion.Image_boom_3
    };

    /**
     * 构造方法
     *
     * @param x            横坐标
     * @param y            位置纵坐标
     * @param SoundEffect 爆炸音效
     */
    public Boom(int x, int y, boolean SoundEffect) {
        this.x = x;
        this.y = y;
        if (SoundEffect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Music.boommusic();
                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 绘画图像
     *
     * @param g 绘制对象
     */
    public void paint(Graphics g) {
        g.drawImage(images[count], x, y, null);
        count++;
    }
}
