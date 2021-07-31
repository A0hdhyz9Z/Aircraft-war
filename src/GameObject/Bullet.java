package GameObject;
/*
 * @Project_name: Aircraft war
 * @Class_name: Bullet
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description: 子弹的坐标变化及绘制方法，和出界判定
 */

import java.awt.*;
import java.awt.image.BufferedImage;

import GameIndicator.Fly;

/**
 * 子弹飞行类,子弹的坐标变化
 */
public class Bullet extends Fly {

    public Bullet(int x, int y, BufferedImage image) {
        this.image = image;
        this.x = x + image.getWidth() / 2;
        this.y = y + image.getHeight() / 2;
    }

    /**
     * 友军子弹移动及其绘制方法
     *
     * @param g
     */
    public void MyBulletPaint(Graphics g) {
        this.y = y - (image.getHeight() / 2);
        g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
    }

    /**
     * 敌军子弹移动及其绘制方法
     *
     * @param g
     */
    public void EnemyBulletPaint(Graphics g) {
        y = y + image.getHeight() / 30;
        g.drawImage(image, x, y, image.getWidth() / 2, image.getHeight() / 2, null);
    }

    /**
     * BOSS子弹绘制方法
     *
     * @param g
     */
    public void BossBulletPaint(Graphics g) {
        g.drawImage(image, x, y, image.getWidth() / 2, image.getHeight() / 2, null);
    }

    /**
     * 出界判定
     *
     * @return
     */
    public boolean OutBound() {
        return y > 1000 || y < 0 || x > 1200 || x < -10;
    }
}
