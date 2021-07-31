package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: HardUI
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/22
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description:
 */

import GameIndicator.Boom;
import GameObject.Bullet;
import GameObject.HardBoss;
import GameObject.HardEnemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class HardUI extends CommonUI implements Runnable {
    private ArrayList<HardEnemy> enemyplanes = new ArrayList<>();
    LinkedList<Bullet> hardbossbullet = new LinkedList<>();
    HardBoss boss;

    /**
     * 构造器，与父类相同
     *
     * @param rand
     * @param SoundEffect
     */
    public HardUI(int rand, boolean SoundEffect) {
        super(rand, SoundEffect);

    }

    /**
     * 比起CommonBoss，多了一个子弹集合，因此需要重写判定方法，多判定一个集合
     *
     * @Override public void if_myplane_Hit_bullet() {
     * super.if_myplane_Hit_bullet();
     * Iterator it4 = hellbossbullet.iterator();
     * while (it4.hasNext()) {
     * Bullet bullet = (Bullet) it4.next();
     * if (myplane1.Hit(bullet)) {
     * it4.remove();
     * if (myplane1.Life > 0) {
     * myplane1.Life--;
     * }
     * continue;
     * }
     * if (myplane2.Hit(bullet)) {
     * it4.remove();
     * if (myplane2.Life > 0) {
     * myplane2.Life--;
     * }
     * }
     * }
     * }
     * <p>
     * /**
     * 比起CommonBoss，难度提高，因此需要重写绘画方法，迭代器类型改为HellEnemyplane
     */
    @Override
    public void DrawEnemyplane(Graphics g, boolean SoundEffect) {
        if (enemyplanes == null) {
            return;
        }
        Iterator<HardEnemy> it = enemyplanes.iterator();
        while (it.hasNext()) {
            HardEnemy enemyplane = it.next();
            if (enemyplane.OutBound()) {
                it.remove();
            } else if (enemyplane.Shooted(Herofighter_1)) {
                score1 += enemyplane.getScore();
                booms.add(new Boom(enemyplane.x, enemyplane.y, SoundEffect));
                it.remove();
            } else if (enemyplane.Shooted(Herofighter_2)) {
                score2 += enemyplane.getScore();
                booms.add(new Boom(enemyplane.x, enemyplane.y, SoundEffect));
                it.remove();
            } else if (enemyplane.Hit(Herofighter_1)) {
                if (Herofighter_1.Life > 0) Herofighter_1.Life--;
                booms.add(new Boom(enemyplane.x, enemyplane.y, SoundEffect));
                it.remove();
            } else if (enemyplane.Hit(Herofighter_2)) {
                if (Herofighter_2.Life > 0) Herofighter_2.Life--;
                booms.add(new Boom(enemyplane.x, enemyplane.y, SoundEffect));
                it.remove();
            }
            enemyplane.paint(g, bullets);
        }
    }

    /**
     * 比起CommonBoss，多了一个子弹集合，因此需要重写绘画方法，多移动并绘画一个集合
     *
     * @param g
     * @param SoundEffect
     */
    @Override
    public void DrawBoss(Graphics g, boolean SoundEffect) {
        bossbulletrightmove();
        bossbulletleftmove();
        hellbossbulletmove();
        for (Bullet bullet : bossbulletsright) {
            bullet.BossBulletPaint(g);
        }
        for (Bullet bullet : bossbulletsleft) {
            bullet.BossBulletPaint(g);
        }
        for (Bullet bullet : hardbossbullet) {
            bullet.BossBulletPaint(g);
        }
        if (boss == null) return;
        if (boss.Orient) {
            boss.paint(g, bossbulletsright, hardbossbullet);
        } else {
            boss.paint(g, bossbulletsleft, hardbossbullet);
        }
        if (boss.Killed(Herofighter_1)) {
            score1 += 1000;
            booms.add(new Boom(boss.x, boss.y, SoundEffect));
            boss = null;
            return;
        }
        if (boss.Killed(Herofighter_2)) {
            score2 += 1000;
            booms.add(new Boom(boss.x, boss.y, SoundEffect));
            boss = null;
            return;
        }
    }

    /**
     * 重写run方法，boss类改为HardBoss
     */
    @Override
    public void run() {

        int i = 0;
        while (true) try {
            if (i % 30 == 0) {
                    if (i % 60 == 0) {
                        enemyplanes.add(new HardEnemy(array[i], GameUI.ImageUnion.Image_plane_2, 20));
                    } else if (i % 90 == 0) enemyplanes.add(new HardEnemy(array[i], GameUI.ImageUnion.Image_plane_4, 30));
                    else if (i % 150 == 0) enemyplanes.add(new HardEnemy(array[i], GameUI.ImageUnion.Image_plane_1, 50));
            }
            if (i % 7200 == 0 && boss == null) {
                boss = new HardBoss();
            }
            i++;
            if (i == 10241) {
                i = 0;
            }
            HerofighterHit();
            repaint();
            if (this.i == 3) {
                this.i++;
                break;
            }
            sleep(18);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ConcurrentModificationException ee) {

        }
    }

    /**
     * 困难模式下Boss特有子弹的移动
     */
    public void hellbossbulletmove() {
        double xspeed = 3, yspeed = 0;
        if (hardbossbullet == null) return;
        Iterator<Bullet> it = hardbossbullet.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.x -= xspeed;
            bullet.y += yspeed;
            xspeed -= 0.15;
            yspeed = Math.sqrt(9 - Math.pow(xspeed, 2));
            if (bullet.OutBound()) {
                it.remove();
            }
        }
    }
}
