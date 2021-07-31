package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: CommonUI
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
import GameObject.CommonBoss;
import GameObject.CommonEnemy;
import GameObject.HeroFighter;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

/*
 * 普通游戏界面类，继承Jpanel，实现Runnable接口
 */

public class CommonUI extends JPanel implements Runnable {
    public int[] array = new int[10240];
    public ArrayList<CommonEnemy> enemyplanes = new ArrayList<>();
    public int mapmove = 0;
    public HeroFighter Herofighter_1 = new HeroFighter(GameUI.ImageUnion.Image_heroplane);
    public HeroFighter Herofighter_2 = new HeroFighter(GameUI.ImageUnion.Image_plane_5);
    public LinkedList<Bullet> bullets = new LinkedList<>();
    public LinkedList<Bullet> bossbulletsright = new LinkedList<>();
    public LinkedList<Bullet> bossbulletsleft = new LinkedList<>();
    public LinkedList<Boom> booms = new LinkedList<>();
    CommonBoss boss;
    public int score1 = 0;
    public int score2 = 0;
    public int i = 0;
    public int j = 0;//i和j是计数器，确保程序流程的有序进行
    public boolean SoundEffect;


    /**
     * 产生游戏界面
     *
     * @param rand         根据特定随机数产生特定敌机横坐标数组，实现每次游戏地图的随机与联机的一致
     * @param SoundEffect 音效设置
     */
    public CommonUI(int rand, boolean SoundEffect) {
        setSize(1200, 900);
        this.SoundEffect = SoundEffect;
        toarray(rand);
    }

    /**
     * 核心方法，结束的判定，地图移动，死亡判定，画boss，画爆炸，画敌机，画分数和生命，判定我方飞机是否被击中
     *
     * @param g 绘制对象
     */
    @Override
    public void paint(Graphics g) {
        try {
            Gameover(g);
            if (i >= 3) return;
            mapmove(g);
            DeadJugde(g);
            DrawBoss(g, SoundEffect);
            DrawBooms(g);
            DrawEnemyplane(g, SoundEffect);
            DrawIndicator(g);
            HerofighterHit();
        } catch (ConcurrentModificationException | IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 地图移动，本质上是两张图片轮流绘制。
     *
     * @param g 绘制对象
     */
    public void mapmove(Graphics g) {
        g.drawImage(GameUI.ImageUnion.Image_background, 0, mapmove, null);
        g.drawImage(GameUI.ImageUnion.Image_background, 0, mapmove - 800, null);
        if (mapmove >= 800) mapmove = 0;
        else mapmove += 2;
    }

    /**
     * 被子弹击中的判定，子弹分为：敌机子弹，boss右射子弹，boss左射子弹
     */
    public void HerofighterHit() {
        Iterator it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = (Bullet) it.next();
            if (Herofighter_1.Hit(bullet)) {
                it.remove();
                if (Herofighter_1.Life > 0) {
                    Herofighter_1.Life--;
                }
                continue;
            }
            if (Herofighter_2.Hit(bullet)) {
                it.remove();
                if (Herofighter_2.Life > 0) {
                    Herofighter_2.Life--;
                }
            }
        }
        Iterator it2 = bossbulletsright.iterator();
        while (it2.hasNext()) {
            Bullet bullet = (Bullet) it2.next();
            if (Herofighter_1.Hit(bullet)) {
                it2.remove();
                if (Herofighter_1.Life > 0) {
                    Herofighter_1.Life--;
                }
                continue;
            }
            if (Herofighter_2.Hit(bullet)) {
                it2.remove();
                if (Herofighter_2.Life > 0) {
                    Herofighter_2.Life--;
                }
            }

        }
        Iterator it3 = bossbulletsleft.iterator();
        while (it3.hasNext()) {
            Bullet bullet = (Bullet) it3.next();
            if (Herofighter_1.Hit(bullet)) {
                it3.remove();
                if (Herofighter_1.Life > 0) {
                    Herofighter_1.Life--;
                }
                continue;
            }
            if (Herofighter_2.Hit(bullet)) {
                it3.remove();
                if (Herofighter_2.Life > 0) {
                    Herofighter_2.Life--;
                }
            }
        }
    }

    /**
     * 死亡判定，若死亡则增加爆炸，飞机移出界面外
     *
     * @param g 绘制对象
     */
    public void DeadJugde(Graphics g) {
        if (i == 0 && Herofighter_1.Dead()) {
            booms.add(new Boom(Herofighter_1.x, Herofighter_1.y, false));
            Herofighter_1.x = 1300;
            Herofighter_1.y = 1500;
            i++;
        }
        if (i == 0) {
            Herofighter_1.paint(g);
        }
        if (i == 1) {
            Herofighter_1.paint(g);
            i++;
        }//这里i的作用是：节省资源，仅需要在死亡时paint一次，以后就不会再执行paint了
        if (j == 0 && Herofighter_2.Dead()) {
            booms.add(new Boom(Herofighter_1.x, Herofighter_1.y, false));
            Herofighter_2.x = 1300;
            Herofighter_2.y = 1500;
            j++;
        }//这里j的作用是：节省资源，仅需要在死亡时paint一次，以后就不会再执行paint了
        if (j == 0) {
            Herofighter_2.paint(g);
        }
        if (j == 1) {
            Herofighter_2.paint(g);
            j++;
        }
    }

    /**
     * 两架飞机全部死亡时，绘制游戏结束图片，绘制分数
     *
     * @param g 绘制对象
     * @return 游戏结束返回true
     */
    public boolean Gameover(Graphics g) throws IOException, ClassNotFoundException, SQLException {
        if (i == 2 && j == 2) {
            g.drawImage(GameUI.ImageUnion.Image_gameover, 0, 0, null);
            g.setColor(Color.cyan);
            g.setFont(new Font(null, 0, 25));
            if (score1 != 0) {
                g.drawString("房主的分数为：" + score1, 500, 500);
            }
            if (score2 != 0) {
                g.drawString("玩家2的分数为：" + score2, 500, 530);
            }
            g.drawString("按Enter键继续", 525, 450);
            i++;
            return true;
        }
        return false;
    }

    /**
     * 工具方法，将i的范围缩小到0-1000，用于产生敌机横坐标
     *
     * @param i
     * @return
     */
    public int decrease_field(int i) {
        while (i > 1000) {
            i = i - 1000;
        }
        return i;
    }

    /**
     * 传入一个特定的数，生成特定的数组，通过decrease_field方法将数组中的数范围限制在0-1000；
     *
     * @param rand
     */
    public void toarray(int rand) {
        for (int i = 0; i < array.length; i++) {
            int number = Math.abs(130 * rand - 73 * (i - rand));
            array[i] = decrease_field(number);
        }
    }

    /**
     * 先进行了敌机的出界判定，击杀判定。然后绘画敌机，用synchronized同步避免在遍历时被另一个线程修改
     *
     * @param g
     * @param sound_effct
     */
    public synchronized void DrawEnemyplane(Graphics g, boolean sound_effct) {
        if (enemyplanes == null) {
            return;
        }
        Iterator<CommonEnemy> it = enemyplanes.iterator();
        while (it.hasNext()) {
            CommonEnemy enemyplane = it.next();
            if (enemyplane.OutBound()) {
                it.remove();
            } else if (enemyplane.Shooted(Herofighter_1)) {
                score1 += enemyplane.getScore();
                booms.add(new Boom(enemyplane.x, enemyplane.y, sound_effct));
                it.remove();
            } else if (enemyplane.Shooted(Herofighter_2)) {
                score2 += enemyplane.getScore();
                booms.add(new Boom(enemyplane.x, enemyplane.y, sound_effct));
                it.remove();
            } else if (enemyplane.Hit(Herofighter_1)) {
                if (Herofighter_1.Life > 0) Herofighter_1.Life--;
                booms.add(new Boom(enemyplane.x, enemyplane.y, sound_effct));
                it.remove();
            } else if (enemyplane.Hit(Herofighter_2)) {
                if (Herofighter_2.Life > 0) Herofighter_2.Life--;
                booms.add(new Boom(enemyplane.x, enemyplane.y, sound_effct));
                it.remove();
            }
            enemyplane.paint(g, bullets);
        }
    }

    /**
     * Boss的子弹移动，子弹绘画，
     *
     * @param g
     * @param SoundEffect
     */
    public void DrawBoss(Graphics g, boolean SoundEffect) {
        bossbulletrightmove();
        bossbulletleftmove();
        for (Bullet bullet : bossbulletsright) {
            bullet.BossBulletPaint(g);
        }
        for (Bullet bullet : bossbulletsleft) {
            bullet.BossBulletPaint(g);
        }
        if (boss == null) return;
        if (boss.Orient) {
            boss.paint(g, bossbulletsright);
        } else {
            boss.paint(g, bossbulletsleft);
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
     * 绘画爆炸效果
     *
     * @param g
     */
    public void DrawBooms(Graphics g) {
        for (Boom boom : booms) {
            boom.paint(g);
            if (boom.count == 7) {
                booms.remove(boom);
            }
        }
    }

    /**
     * 画生命与战绩
     *
     * @param g
     */
    public void DrawIndicator(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font(null, 2, 20));
        g.drawString("Score:" + score1, 20, 20);
        g.drawString("Score:" + score2, 1100, 20);
        g.drawString("Life:" + Herofighter_1.Life, 20, 40);
        g.drawString("Life:" + Herofighter_2.Life, 1100, 40);
    }

    /**
     * run方法，调用数组增加敌机，增加boss，repaint方法进行重绘
     */
    public void run() {
        int i = 0;
        while (true) try {
            if (i % 30 == 0) {
                {
                    if (i % 60 == 0) {
                        enemyplanes.add(new CommonEnemy(array[i], GameUI.ImageUnion.Image_plane_2, 20));
                    } else if (i % 90 == 0) enemyplanes.add(new CommonEnemy(array[i], GameUI.ImageUnion.Image_plane_4, 30));
                    else if (i % 150 == 0) enemyplanes.add(new CommonEnemy(array[i], GameUI.ImageUnion.Image_plane_1, 50));
                }
            }
            if (i % 7200 == 0 && boss == null) {
                boss = new CommonBoss();
            }
            i++;
            if (i == 10241) {
                i = 0;
            }
            repaint();
            if (this.i == 3) {
                this.i++;
                return;
            }
            sleep(18);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 右射子弹的移动方式
     */
    public void bossbulletrightmove() {
        int xspeed = 3;
        int yspeed = 0;
        if (bossbulletsright == null) return;
        Iterator<Bullet> it = bossbulletsright.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.x -= xspeed;
            bullet.y += yspeed;
            xspeed -= 2;
            yspeed += 2;
            if (bullet.OutBound()) {
                it.remove();
            }
        }
    }

    /**
     * 左射子弹的移动方式
     */
    public void bossbulletleftmove() {
        int xspeed = 2;
        int yspeed = 0;
        if (bossbulletsleft == null) return;
        Iterator<Bullet> it = bossbulletsleft.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.x -= xspeed;
            bullet.y += yspeed;
            xspeed += 1;
            yspeed += 2;
            if (bullet.OutBound()) {
                it.remove();
            }
        }
    }
}
