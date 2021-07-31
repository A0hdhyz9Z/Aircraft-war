package GameObject;
/*
 * @Project_name: Aircraft war
 * @Class_name: CommonBoss
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description:普通BOSS的绘制，其移动方式，添加子弹，被击中与击杀
 */

import GameIndicator.Fly;
import GameUI.ImageUnion;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class CommonBoss extends Fly {
    public int Life = 20;           //boss血量
    public int i = 0;           //不同的i代表不同的移动方式
    public int j = 0;           //j逐渐增加，增加60次发射一次子弹
    public boolean Orient = false;      //子弹发射方向

    /**
     * 构造方法，初始化boss位置和图片
     */
    public CommonBoss() {
        x = 500;
        y = 0;
        this.image = ImageUnion.Image_boss;
    }

    /**
     * 传入画板和子弹List，BOSS进行移动并向集合中添加子弹，绘画BOSS，最后对子弹list进行paint
     *
     * @param g
     * @param BossBullets
     */
    public void paint(Graphics g, LinkedList<Bullet> BossBullets) {
        move(BossBullets);
        g.drawImage(image, x, y, null);
        for (Bullet bullet : BossBullets) {
            bullet.BossBulletPaint(g);
        }
    }

    /**
     * 传入子弹List，向其中增加子弹对象
     *
     * @param BossBullets
     */
    public void Shoot(LinkedList<Bullet> BossBullets) {
        for (int i = 0; i < 9; i++) {
            BossBullets.add(new Bullet(x, y, ImageUnion.Image_bossbullet));
        }
    }


    /**
     * 移动Boss，添加子弹
     *
     * @param BossBullets 向该集合中添加子弹
     */
    public void move(LinkedList<Bullet> BossBullets) {
        if (i == 0) {
            x = x - 5;
            if (x <= 10) {
                i = 1;
            }
        } else if (i == 1) {
            y += 3;
            x += 5;
            if (y >= 300) {
                i = 2;
            }
        } else if (i == 2) {
            x += 5;
            y -= 3;
            if (x > 900) {
                i = 3;
            }
        } else if (i == 3) {
            x -= 5;
            if (x < 500) {
                i = 0;
            }
        }
        j++;
        if (j % 60 == 0) {
            Shoot(BossBullets);
            if (Orient) {
                Orient = false;
            } else {
                Orient = true;
            }
        }

    }

    /**
     * 判定击中与击杀
     *
     * @param myplane
     * @return
     */
    public boolean Killed(HeroFighter myplane) {
        Iterator<Bullet> it = myplane.bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            if (x < bullet.x && bullet.x < x + image.getWidth() && y < bullet.y && bullet.y < y + image.getHeight()) {
                it.remove();
                if (Life == 1) {
                    Life = 0;
                    return true;
                }
                if (Life > 1) {
                    Life -= 1;
                }
            }
        }
        return false;
    }
}
