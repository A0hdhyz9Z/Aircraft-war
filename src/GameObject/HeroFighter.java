package GameObject;
/*
 * @Project_name: Aircraft war
 * @Class_name: HeroFighter
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/18
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description: 英雄机的绘制与移动，射击，被击中
 */

import GameIndicator.Fly;
import GameIndicator.Music;
import GameUI.ImageUnion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Thread.sleep;

public class HeroFighter extends Fly {
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int Life = 30;

    /**
     * 构造方法以初始化
     * @param image 英雄机的图像
     */
    public HeroFighter(BufferedImage image) {
        this.image = image;
        x = 200;
        y = 700;
    }

    /**
     * 左移
     */
    public void MoveLeft() {
        if (x >= 20) {
            x = x - image.getWidth() / 2;
        }
    }

    /**
     * 右移
     */
    public void MoveRight() {
        if (x <= 1000) {
            x = x + image.getWidth() / 2;
        }
    }

    /**
     * 下移
     */
    public void MoveDown() {
        if (y <= 666) {
            y += image.getHeight() / 2;
        }
    }

    /**
     * 上移
     */
    public void MoveUp() {
        if (y >= 30) {
            y -= image.getHeight() / 2;
        }
    }

    /**
     * 绘制玩家飞机及其子弹
     *
     * @param g 绘制对象
     */
    public void paint(Graphics g) {
        g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
        DrawBullet(g);

    }

    /**
     * 死亡判定
     *
     * @return boolean
     */
    public boolean Dead() {
        return Life == 0;
    }

    /**
     * 射击操作
     *
     * @param SoundEffect 射击音效
     */
    public void Shoot(boolean SoundEffect) {
        bullets.add(new Bullet(x, y, ImageUnion.Image_herobullet));
        if (SoundEffect) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Music.Shootmusic();
                    try {
                        sleep(50);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 绘画子弹
     *
     * @param g 绘制对象
     */
    public void DrawBullet(Graphics g) {
        if (bullets == null) {
            return;
        }
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            if (bullet.OutBound()) {
                it.remove();
            }
            bullet.MyBulletPaint(g);
        }
    }

    /**
     * 被击中判定
     *
     * @param bullet 子弹集
     * @return boolean
     */
    public boolean Hit(Bullet bullet) {
        return x < bullet.x && bullet.x < x + image.getWidth() && y < bullet.y && bullet.y < y + image.getHeight();
    }
}
