package GameObject;
/*
 * @Project_name: Aircraft war
 * @Class_name: CommonEnemy
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description: 普通敌机的绘制，其移动方式，添加子弹，被击中与击杀
 */

import GameIndicator.Fly;
import GameUI.ImageUnion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

public class CommonEnemy extends Fly {
    public int Count;           //Count逐渐增加，达到一定次数就发射子弹
    private int Score;

    /**
     * 构造方法以初始化敌机
     *
     * @param m     水平坐标
     * @param image 图像
     * @param Score 其被击杀的分数
     */
    public CommonEnemy(int m, BufferedImage image, int Score) {
        this.image = image;
        this.Score = Score;
        y = 0;
        x = m;
    }

    /**
     * 敌机移动，增加子弹，绘画敌机，绘画敌机子弹集合
     *
     * @param g       绘制对象
     * @param bullets 子弹集
     */
    public void paint(Graphics g, LinkedList<Bullet> bullets) {
        y += 4;
        Count++;
        if (Count % 22 == 0) {
            bullets.add(new Bullet(x, y, ImageUnion.Image_enemybullet));
        }
        g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
        Draw_bullet(g, bullets);
    }

    /**
     * 判定出界
     *
     * @return boolean
     */
    public boolean OutBound() {
        return y > 1000;
    }

    /**
     * 绘制子弹List
     *
     * @param g       画布
     * @param bullets 子弹List
     */
    public void Draw_bullet(Graphics g, LinkedList<Bullet> bullets) {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            if (bullet.OutBound()) {
                it.remove();
            }
            bullet.EnemyBulletPaint(g);
        }
    }

    /**
     * 获得成绩
     *
     * @return score
     */
    public int getScore() {
        return Score;
    }

    /**
     * 被herofighter击中判定
     *
     * @param myplane
     * @return boolean
     */
    public boolean Shooted(HeroFighter myplane) {
        Iterator<Bullet> it = myplane.bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            if (x < bullet.x && bullet.x < x + image.getWidth() && y < bullet.y && bullet.y < y + image.getHeight()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * 撞击判定
     *
     * @param myplane
     * @return
     */
    public boolean Hit(HeroFighter myplane) {
        return x < myplane.x + 30 && myplane.x < x + image.getWidth() && y < myplane.y && myplane.y < y + image.getHeight() / 2;
    }
}
