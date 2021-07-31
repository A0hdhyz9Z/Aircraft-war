package GameObject;
/*
 * @Project_name: Aircraft war
 * @Class_name: HardBoss
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

import GameUI.ImageUnion;

import java.awt.*;
import java.util.LinkedList;

public class HardBoss extends CommonBoss {
    /**
     * 初始化
     */
    public HardBoss() {
        super();
        super.Life = 30;
    }

    /**
     * 相比普通BOSS而言，多了一个参数另一种子弹集合hardbossbullet，因二者移动方式不同不能放在同一个集合
     *
     * @param g 绘制对象
     *
     * @param BossBullets 子弹集
     * @param HardBossBullet 额外的子弹集
     */
    public void paint(Graphics g, LinkedList<Bullet> BossBullets, LinkedList<Bullet> HardBossBullet) {
        move(BossBullets, HardBossBullet);
        g.drawImage(image, x, y, null);
        for (Bullet bullet : BossBullets) {
            bullet.BossBulletPaint(g);
        }
    }

    /**
     * 添加20发子弹
     *
     * @param BossBullets 子弹集
     */
    public void HardShoot(LinkedList<Bullet> BossBullets) {
        for (int i = 0; i < 20; i++) {
            BossBullets.add(new Bullet(x, y, ImageUnion.Image_bossbullet));
        }
    }

    /**
     * 移动BOSS以及添加子弹
     *
     * @param BossBullets 子弹集
     * @param HardBossBullet 额外的子弹集
     */
    public void move(LinkedList<Bullet> BossBullets, LinkedList<Bullet> HardBossBullet) {
        super.move(BossBullets);
        if (j % 320 == 0) {
            HardShoot(HardBossBullet);
        }
    }
}
