# 基于Java的雷霆战机游戏

## 一、需求定义

要求基于Java设计和实现一个网络游戏/在线游戏（双人或多人游戏，不是人机对战）。要求运用如下技术：

 面向对象编程技术：类、对象、接口、包、封装、继承、多态性、设计模式。
 异常处理
 输入输出流
 集合
 多线程技术
 图形化的用户界面
 TCP/IP或UDP客户端和服务器网络编程技术

基于以上要求，现设计与开发基于Java的雷霆战机游戏，该项目满足以下需求：

1. 实现玩家飞机的移动,子弹的发射
2. 飞机和子弹的碰撞判定,出界判定
3. 敌机的添加,移动和子弹的发射
4. 得分与生命值功能
5. 碰撞时动态爆炸效果的绘制
6. BOSS独特的移动和子弹移动方式
7. 可关闭开启的音乐与音效
8. 难度选择，简单和困难模式
9. 联机，单机模式选择
10. 排行榜相关功能
11. 注册，登录，注销账户功能
12. 导出个人成绩功能

## 二、功能介绍

主要功能有用户的注册与登录、单人模式难度的选择、基于战果的积分系统、积分排行榜、修改已注册用户的密码、注销账户、创建房间进行联机对战、玩家飞机的控制、子弹的发射、敌机的折线运动、飞机的血量显示、控制游戏音效的播放、飞机与飞机和飞机与子弹之间的碰撞爆炸效果、游戏进程的对话框，导出排行榜至Excel。

## 三、程序技术方案的设计与实现

包括4个包下辖23个类，5个音频文件和 17个图像文件；导入6个第三方类库

其中，UML图解析如下：


### Main

```Java

import GameUI.Client;

/*
 * 入口1
 */
public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            new Client();
        }  catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
```

### Main2

```Java

import GameUI.Client;
/*
 * 入口2
 */
public class Main2 {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            new Client();
        }  catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
```



### 1、GameObject包

功能是绘制游戏中各个物体，包含绘制的方法、移动的方法、获取坐标的方法

#### 1.1 Bullet类：

​		子弹的坐标变化及绘制方法，和出界判定

```Java
package GameObject;

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

```



#### 1.2 CommonBoss类：

```Java
package GameObject;
/*
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

```



#### 1.3 CommonEnemy类：

```Java
package GameObject;
/*
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

```



#### 1.4 HardBoss类：

```Java
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
 * @Description: 困难BOSS的独有子弹发射方式
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

```



#### 1.5 HardEnemy类：

```Java
package GameObject;
/*
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

```

1.5.1 构造器以初始化

```Java
public HardEnemy(int m, BufferedImage image, int score) {
        super(m, image, score);
    }
```



#### 1.6 HeroFighter类：

```Java
package GameObject;
/*
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

```



### 2、GameIndicator包：

显示游戏的各项数值及音乐，图像

#### 2.1 Boom类

```Java
package GameIndicator;
/*
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

```



#### 2.2 Fly类

```Java
package GameIndicator;

import java.awt.image.BufferedImage;

public class Fly {
    public int x;
    public int y;
    public BufferedImage image;
}

```

#### 2.3 Music类

```java
package GameIndicator;
/*
 * @Project_name: Aircraft war
 * @Class_name: Music
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
 * @Description: 音效的输入及绘制
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 音乐类，AudioClip类需要调用Applet，而其已经过时无效,无法正常使用。后来查询找到替代类Clip及其配合使用的AudioSystem。
 * 即便如此，替代类Clip仍有缺陷，比如当一个线程在使用一个Clip时，另一个线程调用音乐播放是无效的。因此对音效采取了轮流播放，足以满足需求。
 */
public class Music {
    public static Clip backgroundmusic;
    public static Clip Shootmusic;
    public static Clip Shootmusic2;
    public static Clip Shootmusic3;
    public static Clip boommusic;
    public static Clip boommusic2;
    public static Clip boommusic3;
    public static int i = 1;
    static int j = 1;

    static {
        try {

            backgroundmusic = AudioSystem.getClip();
            Shootmusic = AudioSystem.getClip();
            Shootmusic2 = AudioSystem.getClip();
            Shootmusic3 = AudioSystem.getClip();
            boommusic = AudioSystem.getClip();
            boommusic2 = AudioSystem.getClip();
            boommusic3 = AudioSystem.getClip();
            backgroundmusic.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/background.wav")));
            Shootmusic.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/shoot.wav")));
            Shootmusic2.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/shoot.wav")));
            Shootmusic3.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/shoot.wav")));
            boommusic.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/boom1.wav")));
            boommusic2.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/boom2.wav")));
            boommusic3.open(AudioSystem.getAudioInputStream(new File("E:/Project/Resources/Audio/boom3.wav")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 爆炸音效的绘制，不能用start，start播放完毕后再start时，从上次结束的地方开始播放，相当于一个Clip只能用一遍。用loop(1)代替
     */
    public static void boommusic() {
        if (i == 1) {
            i = 2;
            boommusic.loop(1);
        } else if (i == 2) {
            i = 3;
            boommusic2.loop(1);
        } else if (i == 3) {
            i = 1;
            boommusic3.loop(1);
        }
    }

    /**
     * 射击音效的绘制，不能用start，start播放完毕后再start时，从上次结束的地方开始播放，相当于一个Clip只能用一遍。用loop(1)代替
     */
    public static void Shootmusic() {
        if (j == 1) {
            j = 2;
            Shootmusic.loop(1);
        } else if (j == 2) {
            j = 3;
            Shootmusic2.loop(1);
        } else if (j == 3) {
            j = 1;
            Shootmusic3.loop(1);
        }
    }
}

```

#### 2.4 ImageUnion类

```Java
package GameUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUnion {

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
            Image_plane_4 = ImageIO.read(new FileInputStream("E/Project/Resources/Image/plane4.png"));
            Image_plane_5 = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/plane5.png"));
            Image_gameover = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/gameover.jpg"));
            Image_heroplane = ImageIO.read(new FileInputStream("E:/Project/Resources/Image/heroplane.png"));
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }
}

```



### 3、GameUI包

游戏的各个图形用户界面的绘制，及客户端

#### 3.1 LoginInterface类：

```Java
package GameUI;
/*
 * @Description: 登录界面的绘制
 */

import javax.swing.*;
import java.awt.*;

public class LoginInterface extends JPanel {

    /**
    The following are the names of text and buttons
     */
    JTextField UserName = new JTextField(16);
    JPasswordField Password = new JPasswordField(16);
    JButton LoginButton = new JButton("登录");
    JButton RegisterButton = new JButton("注册");
    JLabel JLabel_Username = new JLabel("用户名：");
    JLabel JLabel_Password = new JLabel("密码： ");

    /**
    The following is the design of login page UI , which is a conductor
     */
    public LoginInterface() {
        setLayout(null);
        UserName.setBounds(525, 500, 100, 30);
        JLabel_Username.setBounds(460, 500, 110, 30);
        Password.setBounds(525, 550, 100, 30);
        JLabel_Password.setBounds(460, 550, 110, 30);
        LoginButton.setBounds(525, 600, 100, 30);
        RegisterButton.setBounds(525, 670, 100, 30);
        Font font = new Font(null, 2, 15);
        JLabel_Username.setFont(font);
        JLabel_Password.setFont(font);
        JLabel_Password.setForeground(Color.CYAN);
        JLabel_Username.setForeground(Color.CYAN);
        add(UserName);
        add(Password);
        add(LoginButton);
        add(RegisterButton);
        add(RegisterButton);
        add(JLabel_Username);
        add(JLabel_Password);
    }

    /**
    Add the background image and the loge image
     */
   public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Image Image_background = new ImageIcon("E:/Project/Resources/Image/background.jpg").getImage();
        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        //Image Image_LOGE = new ImageIcon("E:/Project/Resources/Image/LOGE.jfif").getImage();
       g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }

}
```

#### 3.2 HomePage类

```Java
package GameUI;
/*
 * @Description: 主界面的绘制
 */

import javax.swing.*;
import java.awt.*;

public class HomePage extends JPanel {

    /**
    The following are the names of text and buttons
     */
    JButton SingleModel = new JButton("单人模式");
    JButton EstablishRoom = new JButton("创建房间");
    JButton JoinRoom = new JButton("加入房间");
    JButton Rank = new JButton("查看排行榜");
    JButton Set = new JButton("设置");
    JButton Logout = new JButton("注销");
    JButton Modify = new JButton("修改密码");

    /**
    The following is the design of home page UI , which is a conductor
     */
    public HomePage() {
        setLayout(null);
        EstablishRoom.setBounds(525, 500, 100, 30);
        JoinRoom.setBounds(525, 540, 100, 30);
        SingleModel.setBounds(525, 580, 100, 30);
        Rank.setBounds(525, 620, 100, 30);
        Set.setBounds(525, 660, 100, 30);
        Modify.setBounds(525, 700, 100, 30);
        Logout.setBounds(525, 740, 100, 30);

        add(EstablishRoom);
        add(JoinRoom);
        add(SingleModel);
        add(Rank);
        add(Set);
        add(Logout);
        add(Modify);
    }

    /**
    Add the background image and the loge image
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }
}

```

#### 3.3 ModifyPage类

```Java
package GameUI;
/*
 * @Description: 修改密码界面的绘制
 */

import javax.swing.*;
import java.awt.*;

public class ModifyPage extends JPanel {

    JPasswordField passwordfield0 = new JPasswordField(16);
    JPasswordField passwordField = new JPasswordField(16);
    JPasswordField passwordField2 = new JPasswordField(16);
    JButton modify_button = new JButton("修改密码");
    JButton return_jButton = new JButton("返回");
    JLabel jLabel = new JLabel("输入旧密码:");
    JLabel jLabel2 = new JLabel("输入新密码:");
    JLabel jLabel3 = new JLabel("确认新密码:");

    public ModifyPage() {
        setLayout(null);
        Font font = new Font(null, 2, 15);
        jLabel.setFont(font);
        jLabel2.setFont(font);
        jLabel3.setFont(font);
        jLabel2.setForeground(Color.CYAN);
        jLabel.setForeground(Color.CYAN);
        jLabel3.setForeground(Color.CYAN);
        passwordfield0.setBounds(525, 500, 100, 30);
        jLabel.setBounds(440, 500, 100, 30);
        passwordField.setBounds(525, 550, 100, 30);
        jLabel2.setBounds(440, 550, 100, 30);
        passwordField2.setBounds(525, 600, 100, 30);
        jLabel3.setBounds(440, 600, 100, 30);
        modify_button.setBounds(525, 650, 100, 30);
        return_jButton.setBounds(20, 20, 100, 30);
        add(passwordfield0);
        add(passwordField);
        add(passwordField2);
        add(modify_button);
        add(return_jButton);
        add(jLabel);
        add(jLabel2);
        add(jLabel3);
    }

    /**
      Add the background image and the loge image
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }
}

```



#### 3.4 RegisterPage类

```Java
package GameUI;
/*
 * @Description: 注册用户界面的绘制
 */

import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JPanel {

    JTextField usernamefield = new JTextField(12);
    JPasswordField passwordField = new JPasswordField(12);
    JPasswordField passwordField2 = new JPasswordField(12);
    JButton register_button2 = new JButton("注册");
    JButton return_jButton = new JButton("返回");
    JLabel jLabel = new JLabel("输入用户名:");
    JLabel jLabel2 = new JLabel("输入密码:");
    JLabel jLabel3 = new JLabel("确认密码:");

    public RegisterPage() {
        setLayout(null);
        Font font = new Font(null, 2, 15);
        jLabel.setFont(font);
        jLabel2.setFont(font);
        jLabel3.setFont(font);
        jLabel2.setForeground(Color.CYAN);
        jLabel.setForeground(Color.CYAN);
        jLabel3.setForeground(Color.CYAN);
        usernamefield.setBounds(525,500,100,30);
        jLabel.setBounds(440,500,100,30);
        passwordField.setBounds(525,550,100,30);
        jLabel2.setBounds(440,550,100,30);
        passwordField2.setBounds(525,600,100,30);
        jLabel3.setBounds(440,600,100,30);
        register_button2.setBounds(525,650,100,30);
        return_jButton.setBounds(20,20,100,30);
        add(usernamefield);
        add(passwordField);
        add(passwordField2);
        add(register_button2);
        add(return_jButton);
        add(jLabel);
        add(jLabel2);
        add(jLabel3);
    }

    /**
  Add the background image and the loge image
 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }

}

```



#### 3.5 RoomPage类

```Java
package GameUI;
/*
 * @Description: 创建房间界面的绘制
 */

import javax.swing.*;
import java.awt.*;

public class RoomPage extends JPanel {

    JButton startbutton = new JButton("开始游戏");
    JLabel jLabel = new JLabel();

    /**
     * 构造器方法以初始化
     * @param port 房间号
     */
    public RoomPage(int port) {
        setLayout(null);
        jLabel.setFont(new Font(null, 6, 45));
        jLabel.setForeground(Color.BLACK);
        jLabel.setSize(320, 300);
        jLabel.setLocation(20, 20);
        startbutton.setBounds(500, 630, 120, 40);
        jLabel.setText("房间号:" + port);
        add(startbutton);
        add(jLabel);
    }

    /**
  Add the background image and the loge image
 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

    }
}

```



#### 3.6 CommonUI类

```Java
package GameUI;

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
    public int j = 0;			//i和j是计数器，确保程序流程的有序进行
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

```



#### 3.7 HardUI类

```JAVA
package GameUI;
/*
 * @Description:困难模式的UI绘制
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

```



#### 3.8 Client类

```Java
package GameUI;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Random;

import GameIndicator.Music;

import static java.lang.Thread.sleep;

/**
 * 核心客户端。各组件的集成
 */
public class Client {
    JFrame Jframe_mainpage = new JFrame();
    HomePage JPanel_homepage = new HomePage();
    LoginInterface loginpage = new LoginInterface();
    RegisterPage registerpage = new RegisterPage();
    ModifyPage modifypage = new ModifyPage();
    RoomPage roompage = new RoomPage(0000);
    ServerSocket serverSocket;
    String username;
    String username2;
    boolean bgm = true;
    boolean SoundEffect = true;

    /**
     * 构造器，调用Music类的属性只是为了让JVM预先加载其资源
     */
    public Client() {
        Music.i = 1;
        Jframe_mainpage.setIconImage(GameUI.ImageUnion.Image_LOGE);
        loadlistener();
        Jframe_mainpage.setSize(1200, 950);
        Jframe_mainpage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Jframe_mainpage.setContentPane(loginpage);
        Jframe_mainpage.setResizable(false);
        Jframe_mainpage.setVisible(true);
    }

    /**
     * P1玩家的UI
     *
     * @param s
     * @param rand 随机数
     * @param difficulty 难度选择
     */
    public void GameUI_1(Socket s, int rand, int difficulty) {
        //困难模式
        if (difficulty == 1) {
            HardUI HardUI;
            HardUI = new HardUI(rand, SoundEffect);             //rand用于地图的随机性,SoundEffect是音效设置
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (HardUI.i == 4) {
                                Socket socket = new Socket("127.0.0.1", 111);
                                socket.getOutputStream().write(2);          //上传数据，写入输出流
                                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeInt(HardUI.score1 + HardUI.score2);            //联机时两位玩家的totalscore
                                dos.writeUTF(username);             //上传两位玩家的用户名
                                dos.writeUTF(username2);
                                break;
                            }
                            if (HardUI.Herofighter_2.Dead()) {
                                return;
                            }
                            int a = s.getInputStream().read();
                            if (a == 1) {
                                HardUI.Herofighter_2.Shoot(SoundEffect);
                            }
                            if (a == 2) {
                                HardUI.Herofighter_2.MoveRight();
                            }
                            if (a == 3) {
                                HardUI.Herofighter_2.MoveLeft();
                            }
                            if (a == 4) {
                                HardUI.Herofighter_2.MoveUp();
                            }
                            if (a == 5) {
                                HardUI.Herofighter_2.MoveDown();
                            }
                        } catch (SocketException se) {
                            Thread.currentThread().interrupt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            Jframe_mainpage.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    try {
                        if (HardUI.i == 4) {
                            s.close();
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                if (bgm) {
                                    Music.backgroundmusic.stop();
                                }
                                Jframe_mainpage.dispose();
                                Jframe_mainpage.setContentPane(JPanel_homepage);
                                Jframe_mainpage.setVisible(true);
                                Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                            }
                        }
                        if (HardUI.Herofighter_1.Dead()) {
                            return;
                        }
                        if (e.getKeyCode() == 32) {
                            s.getOutputStream().write(1);
                            HardUI.Herofighter_1.Shoot(SoundEffect);
                        }
                        if (e.getKeyCode() == 39) {
                            s.getOutputStream().write(2);
                            HardUI.Herofighter_1.MoveRight();
                        }
                        if (e.getKeyCode() == 37) {
                            s.getOutputStream().write(3);
                            HardUI.Herofighter_1.MoveLeft();
                        }
                        if (e.getKeyCode() == 38) {
                            s.getOutputStream().write(4);
                            HardUI.Herofighter_1.MoveUp();
                        }
                        if (e.getKeyCode() == 40) {
                            s.getOutputStream().write(5);
                            HardUI.Herofighter_1.MoveDown();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
            //控制音乐的开始
            Jframe_mainpage.setContentPane(HardUI);
            new Thread(HardUI).start();
            if (bgm) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Music.backgroundmusic.start();
                    }
                }).start();
            }
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        }
        //简单模式
        if (difficulty == 0) {
            CommonUI CommonUI = new CommonUI(rand, SoundEffect);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (CommonUI.Herofighter_2.Dead()) {
                                return;
                            }
                            int a = s.getInputStream().read();
                            if (a == 1) {
                                CommonUI.Herofighter_2.Shoot(SoundEffect);
                            }
                            if (a == 2) {
                                CommonUI.Herofighter_2.MoveRight();
                            }
                            if (a == 3) {
                                CommonUI.Herofighter_2.MoveLeft();
                            }
                            if (a == 4) {
                                CommonUI.Herofighter_2.MoveUp();
                            }
                            if (a == 5) {
                                CommonUI.Herofighter_2.MoveDown();
                            }
                        } catch (SocketException se) {
                            Thread.currentThread().interrupt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            Jframe_mainpage.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    try {
                        if (CommonUI.i == 4) {
                            s.close();
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                if (bgm) {
                                    Music.backgroundmusic.stop();
                                }
                                Jframe_mainpage.dispose();
                                Jframe_mainpage.setContentPane(JPanel_homepage);
                                Jframe_mainpage.setVisible(true);

                            }
                        }
                        if (CommonUI.Herofighter_1.Dead()) {
                            return;
                        }
                        if (e.getKeyCode() == 32) {
                            s.getOutputStream().write(1);
                            CommonUI.Herofighter_1.Shoot(SoundEffect);
                        }
                        if (e.getKeyCode() == 39) {
                            s.getOutputStream().write(2);
                            CommonUI.Herofighter_1.MoveRight();
                        }
                        if (e.getKeyCode() == 37) {
                            s.getOutputStream().write(3);
                            CommonUI.Herofighter_1.MoveLeft();
                        }
                        if (e.getKeyCode() == 38) {
                            s.getOutputStream().write(4);
                            CommonUI.Herofighter_1.MoveUp();
                        }
                        if (e.getKeyCode() == 40) {
                            s.getOutputStream().write(5);
                            CommonUI.Herofighter_1.MoveDown();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
            Jframe_mainpage.setContentPane(CommonUI);
            new Thread(CommonUI).start();
            if (bgm) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Music.backgroundmusic.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                }).start();
            }
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        }
    }

    /**
     * P2玩家的UI
     *
     * @param s
     * @param rand
     * @param difficulty
     */
    public void GameUI_2(Socket s, int rand, int difficulty) {
        if (difficulty == 1) {
            HardUI HardUI = new HardUI(rand, SoundEffect);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (HardUI.Herofighter_1.Dead()) {
                                return;
                            }
                            int a = s.getInputStream().read();
                            if (a == 1) {
                                HardUI.Herofighter_1.Shoot(SoundEffect);
                            }
                            if (a == 2) {
                                HardUI.Herofighter_1.MoveRight();
                            }
                            if (a == 3) {
                                HardUI.Herofighter_1.MoveLeft();
                            }
                            if (a == 4) {
                                HardUI.Herofighter_1.MoveUp();
                            }
                            if (a == 5) {
                                HardUI.Herofighter_1.MoveDown();
                            }
                        } catch (SocketException ee) {
                            Thread.currentThread().interrupt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            Jframe_mainpage.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    try {
                        if (HardUI.i == 4) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                if (bgm) {
                                    Music.backgroundmusic.stop();
                                }
                                Jframe_mainpage.dispose();
                                Jframe_mainpage.setContentPane(JPanel_homepage);
                                Jframe_mainpage.setVisible(true);
                                serverSocket = null;
                            }
                        }
                        if (HardUI.Herofighter_2.Dead()) {
                            return;
                        }
                        if (e.getKeyCode() == 32) {
                            s.getOutputStream().write(1);
                            HardUI.Herofighter_2.Shoot(SoundEffect);
                        }
                        if (e.getKeyCode() == 39) {
                            s.getOutputStream().write(2);
                            HardUI.Herofighter_2.MoveRight();
                        }
                        if (e.getKeyCode() == 37) {
                            s.getOutputStream().write(3);
                            HardUI.Herofighter_2.MoveLeft();
                        }
                        if (e.getKeyCode() == 38) {
                            s.getOutputStream().write(4);
                            HardUI.Herofighter_2.MoveUp();
                        }
                        if (e.getKeyCode() == 40) {
                            s.getOutputStream().write(5);
                            HardUI.Herofighter_2.MoveDown();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            Jframe_mainpage.setContentPane(HardUI);
            new Thread(HardUI).start();
            if (bgm) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Music.backgroundmusic.loop(-1);
                    }
                }).start();
            }
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());

        }
        if (difficulty == 0) {
            CommonUI CommonUI = new CommonUI(rand, SoundEffect);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (CommonUI.Herofighter_1.Dead()) {
                                return;
                            }
                            int a = s.getInputStream().read();
                            if (a == 1) {
                                CommonUI.Herofighter_1.Shoot(SoundEffect);
                            }
                            if (a == 2) {
                                CommonUI.Herofighter_1.MoveRight();
                            }
                            if (a == 3) {
                                CommonUI.Herofighter_1.MoveLeft();
                            }
                            if (a == 4) {
                                CommonUI.Herofighter_1.MoveUp();
                            }
                            if (a == 5) {
                                CommonUI.Herofighter_1.MoveDown();
                            }
                        } catch (SocketException ee) {
                            Thread.currentThread().interrupt();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            Jframe_mainpage.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    try {
                        if (CommonUI.i >= 4) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                if (bgm) {
                                    Music.backgroundmusic.stop();
                                }
                                Jframe_mainpage.dispose();
                                Jframe_mainpage.setContentPane(JPanel_homepage);
                                Jframe_mainpage.setVisible(true);

                                serverSocket = null;
                            }
                        }
                        if (CommonUI.Herofighter_2.Dead()) {
                            return;
                        }
                        if (e.getKeyCode() == 32) {
                            s.getOutputStream().write(1);
                            CommonUI.Herofighter_2.Shoot(SoundEffect);
                        }
                        if (e.getKeyCode() == 39) {
                            s.getOutputStream().write(2);
                            CommonUI.Herofighter_2.MoveRight();
                        }
                        if (e.getKeyCode() == 37) {
                            s.getOutputStream().write(3);
                            CommonUI.Herofighter_2.MoveLeft();
                        }
                        if (e.getKeyCode() == 38) {
                            s.getOutputStream().write(4);
                            CommonUI.Herofighter_2.MoveUp();
                        }
                        if (e.getKeyCode() == 40) {
                            s.getOutputStream().write(5);
                            CommonUI.Herofighter_2.MoveDown();
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            Jframe_mainpage.setContentPane(CommonUI);
            new Thread(CommonUI).start();
            if (bgm) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Music.backgroundmusic.loop(Clip.LOOP_CONTINUOUSLY);

                    }
                }).start();
            }
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        }
    }

    /**
     * 单机UI
     *
     * @param rand
     * @param difficulty
     */
    public void GameUI_3(int rand, int difficulty) {

        if (difficulty == 1) {
            HardUI HardUI = new HardUI(rand, SoundEffect);
            HardUI.Herofighter_2.x = 1300;
            HardUI.Herofighter_2.y = 1500;
            HardUI.Herofighter_2.Life = 0;
            Jframe_mainpage.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (HardUI.i == 4) {
                        try {
                            Socket socket = new Socket("127.0.0.1", 111);
                            socket.getOutputStream().write(3);
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(HardUI.score1);
                            dos.writeUTF(username);
                            System.out.println(HardUI.score1);
                            HardUI.i++;
                        } catch (UnknownHostException unknownHostException) {
                            unknownHostException.printStackTrace();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    if (HardUI.i >= 4 && e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (bgm) {
                            Music.backgroundmusic.stop();
                        }
                        Jframe_mainpage.dispose();
                        Jframe_mainpage.setContentPane(JPanel_homepage);
                        Jframe_mainpage.setVisible(true);

                    }
                    if (HardUI.Herofighter_1.Dead()) {
                        return;
                    }
                    if (e.getKeyCode() == 32) {
                        HardUI.Herofighter_1.Shoot(SoundEffect);
                    }
                    if (e.getKeyCode() == 39) {
                        HardUI.Herofighter_1.MoveRight();
                    }
                    if (e.getKeyCode() == 37) {
                        HardUI.Herofighter_1.MoveLeft();
                    }
                    if (e.getKeyCode() == 38) {
                        HardUI.Herofighter_1.MoveUp();
                    }
                    if (e.getKeyCode() == 40) {
                        HardUI.Herofighter_1.MoveDown();
                    }
                }
            });

            Jframe_mainpage.setContentPane(HardUI);
            new Thread(HardUI).start();
            if (bgm) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Music.backgroundmusic.loop(-1);
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        } else if (difficulty == 0) {
            CommonUI CommonUI = new CommonUI(rand, SoundEffect);
            CommonUI.Herofighter_2.x = 1300;
            CommonUI.Herofighter_2.y = 1500;
            CommonUI.Herofighter_2.Life = 0;

            Jframe_mainpage.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (CommonUI.i == 4) {
                        try {
                            Socket socket = new Socket("127.0.0.1", 111);
                            socket.getOutputStream().write(3);
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(CommonUI.score1);
                            dos.writeUTF(username);
                            System.out.println(CommonUI.score1);
                            CommonUI.i++;
                        } catch (UnknownHostException unknownHostException) {
                            unknownHostException.printStackTrace();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    if (CommonUI.i >= 4 && e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (bgm) {
                            Music.backgroundmusic.stop();
                        }
                        Jframe_mainpage.dispose();
                        Jframe_mainpage.setContentPane(JPanel_homepage);
                        Jframe_mainpage.setVisible(true);

                    }
                    if (CommonUI.Herofighter_1.Dead()) {
                        return;
                    }
                    if (e.getKeyCode() == 32) {
                        CommonUI.Herofighter_1.Shoot(SoundEffect);
                    }
                    if (e.getKeyCode() == 39) {
                        CommonUI.Herofighter_1.MoveRight();
                    }
                    if (e.getKeyCode() == 37) {
                        CommonUI.Herofighter_1.MoveLeft();
                    }
                    if (e.getKeyCode() == 38) {
                        CommonUI.Herofighter_1.MoveUp();
                    }
                    if (e.getKeyCode() == 40) {
                        CommonUI.Herofighter_1.MoveDown();
                    }
                }
            });

            Jframe_mainpage.setContentPane(CommonUI);
            new Thread(CommonUI).start();
            if (bgm) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Music.backgroundmusic.loop(Clip.LOOP_CONTINUOUSLY);
                        try {
                            sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        }
    }

    /**
     * 产生随机端口号作为房间号,产生1024~65535的随机数
     *
     * @return
     */
    public int random_port() {
        Random rand = new Random();
        return rand.nextInt(64512) + 1024;
    }

    /**
     * 加载界面监听器功能模块
     */
    public void loadlistener() {
        JPanel_homepage.Rank.addActionListener(e -> {
            try {
                String[] possibleValues = {"单人排行榜", "双人排行榜"};

                String value = (String) JOptionPane.showInputDialog(null, "请选择", "Choose", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                if (value == null) {
                    return;
                }
                if (value.equals("单人排行榜")) {
                    Socket s = new Socket("127.0.0.1", 111);
                    s.getOutputStream().write(5);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(username);
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    String rank = dis.readUTF();
                    JOptionPane.showMessageDialog(null, rank);
                } else if (value.equals("双人排行榜")) {
                    Socket s = new Socket("127.0.0.1", 111);
                    s.getOutputStream().write(4);
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(username);
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    String rank = dis.readUTF();
                    JOptionPane.showMessageDialog(null, rank);
                }
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        JPanel_homepage.Set.addActionListener(e -> {

            String option1 = "";
            String option2 = "";
            String now_status = "";

            if (bgm && SoundEffect) {
                now_status = "游戏音乐：开\n" + "游戏音效：开";
                option1 = "关闭音乐";
                option2 = "关闭音效";
            }
            if (!bgm && SoundEffect) {
                now_status = "游戏音乐：关\n" + "游戏音效：开";
                option1 = "开启音乐";
                option2 = "关闭音效";
            }
            if (bgm && !SoundEffect) {
                now_status = "游戏音乐：开\n" + "游戏音效：关";
                option1 = "关闭音乐";
                option2 = "开启音效";
            }
            if (!bgm && !SoundEffect) {
                now_status = "游戏音乐：关\n" + "游戏音效：关";
                option1 = "开启音乐";
                option2 = "开启音效";
            }
            Object[] options = {option1, option2, "全部关闭", "全部开启"};
            int response = JOptionPane.showOptionDialog(null, now_status, "Settings", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                bgm = !bgm;
            }
            if (response == 1) {
                SoundEffect = !SoundEffect;
            }
            if (response == 2) {
                bgm = false;
                SoundEffect = false;
            }
            if (response == 3) {
                bgm = true;
                SoundEffect = true;
            }
        });
        JPanel_homepage.Logout.addActionListener(e -> {
            int i = JOptionPane.showConfirmDialog(null, "你确定要注销吗？", "Choose", JOptionPane.OK_CANCEL_OPTION);
            if (i == 0) {
                username = null;
                username2 = null;
                bgm = true;
                SoundEffect = true;
                Jframe_mainpage.setContentPane(loginpage);
                Jframe_mainpage.setVisible(true);
                Jframe_mainpage.update(Jframe_mainpage.getGraphics());
            }
        });
        JPanel_homepage.Modify.addActionListener(e -> {
            Jframe_mainpage.setContentPane(modifypage);
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        });
        registerpage.register_button2.addActionListener(e -> {
            if (registerpage.passwordField.getText().length() < 6 || registerpage.passwordField.getText().length() > 16) {
                JOptionPane.showMessageDialog(null, "请输入6~16位的密码");
                return;
            }
            if (!registerpage.passwordField.getText().equals(registerpage.passwordField2.getText())) {
                JOptionPane.showMessageDialog(null, "两次密码输入不一致");
                return;
            }
            try {
                Socket s = new Socket("127.0.0.1", 111);
                s.getOutputStream().write(1);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(registerpage.usernamefield.getText());
                dos.writeUTF(registerpage.passwordField.getText());
                int a = s.getInputStream().read();
                if (a == 0) {
                    JOptionPane.showMessageDialog(null, "注册成功");
                    loginpage.UserName.setText(registerpage.usernamefield.getText());
                    registerpage.passwordField.setText("");
                    registerpage.usernamefield.setText("");
                    registerpage.passwordField2.setText("");
                    Jframe_mainpage.setContentPane(loginpage);
                    Jframe_mainpage.setVisible(true);
                    Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                }
                if (a == 1) {
                    JOptionPane.showMessageDialog(null, "用户名已存在");
                }
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        modifypage.modify_button.addActionListener(e -> {
            if (modifypage.passwordField.getText().length() < 6 || modifypage.passwordField.getText().length() > 16 ||
                    modifypage.passwordfield0.getText().length() < 6 || modifypage.passwordfield0.getText().length() > 16) {
                JOptionPane.showMessageDialog(null, "请输入6~16位的密码");
                return;
            }
            if (!modifypage.passwordField.getText().equals(modifypage.passwordField2.getText())) {
                JOptionPane.showMessageDialog(null, "两次密码输入不一致");
                return;
            }
            try {
                Socket s = new Socket("127.0.0.1", 111);
                s.getOutputStream().write(6);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(username);
                dos.writeUTF(modifypage.passwordfield0.getText());
                dos.writeUTF(modifypage.passwordField.getText());
                int a = s.getInputStream().read();
                if (a == 1) {
                    JOptionPane.showMessageDialog(null, "修改成功");
                    modifypage.passwordField.setText("");
                    modifypage.passwordfield0.setText("");
                    modifypage.passwordField2.setText("");
                    username = null;
                    Jframe_mainpage.setContentPane(loginpage);
                    Jframe_mainpage.setVisible(true);
                    Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                }
                if (a == 0) {
                    JOptionPane.showMessageDialog(null, "原密码错误");
                }
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        registerpage.return_jButton.addActionListener(e -> {

            Jframe_mainpage.setContentPane(loginpage);
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        });
        modifypage.return_jButton.addActionListener(e -> {

            Jframe_mainpage.setContentPane(JPanel_homepage);
            Jframe_mainpage.setVisible(true);
            Jframe_mainpage.update(Jframe_mainpage.getGraphics());
        });
        loginpage.RegisterButton.addActionListener(e -> {
                    Jframe_mainpage.setContentPane(registerpage);
                    Jframe_mainpage.setVisible(true);
                    Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                }
        );
        loginpage.LoginButton.addActionListener(e -> {
            if (loginpage.UserName.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "请输入用户名");
                return;
            }
            if (loginpage.Password.getText().length() < 6 || loginpage.Password.getText().length() > 16) {
                JOptionPane.showMessageDialog(null, "请输入6~16位的密码");
                return;
            }
            try {if (loginpage.UserName.getText().equals("admin")) {
                JOptionPane.showMessageDialog(null, "登录成功");
                username = loginpage.UserName.getText();
                loginpage.Password.setText("");
                loginpage.UserName.setText("");
                Jframe_mainpage.setContentPane(JPanel_homepage);
                Jframe_mainpage.setVisible(true);
                Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                return;}
                Socket s = new Socket("127.0.0.1", 111);
                s.getOutputStream().write(0);
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(loginpage.UserName.getText());
                dos.writeUTF(loginpage.Password.getText());
                int a = s.getInputStream().read();
                if (a == 0) {
                    JOptionPane.showMessageDialog(null, "登录成功");
                    username = loginpage.UserName.getText();
                    loginpage.Password.setText("");
                    loginpage.UserName.setText("");
                    Jframe_mainpage.setContentPane(JPanel_homepage);
                    Jframe_mainpage.setVisible(true);
                    Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                    return;
                }
                if (a == 1) {
                    JOptionPane.showMessageDialog(null, "用户名不存在");
                    return;
                }
                if (a == 2) {
                    JOptionPane.showMessageDialog(null, "密码错误");
                    loginpage.Password.setText("");
                }
            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                JOptionPane.showMessageDialog(null, "服务器连接失败");
            }
        });
        JPanel_homepage.SingleModel.addActionListener(e -> {
            Random rand = new Random();
            String[] possibleValues = {"困难", "普通"};
            String value = (String) JOptionPane.showInputDialog(null, "选择难度", "Choose", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
            if (value == null) {
                return;
            }
            if (value.equals("普通")) {
                GameUI_3(rand.nextInt(10), 0);
            } else if (value.equals("困难")) {
                GameUI_3(rand.nextInt(10), 1);

            }

        });
        JPanel_homepage.EstablishRoom.addActionListener(e -> {
            try {
                int port = random_port();
                serverSocket = new ServerSocket(port);
                JOptionPane.showMessageDialog(null, "建立成功：房间号：" + port);
                roompage = new RoomPage(port);
                Jframe_mainpage.setContentPane(roompage);
                Jframe_mainpage.setVisible(true);
                Jframe_mainpage.update(Jframe_mainpage.getGraphics());
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                int rand = socket.getInputStream().read();
                username2 = dis.readUTF();
                JOptionPane.showMessageDialog(Jframe_mainpage, username2 + "加入了房间，请开始游戏");
                roompage.startbutton.addActionListener(e1 -> {
                    String[] possibleValues = {"困难", "普通"};
                    String value = (String) JOptionPane.showInputDialog(null, "选择难度", "input", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                    try {
                        if (value == null) {
                            return;
                        }
                        if (value.equals("普通")) {
                            socket.getOutputStream().write(0);
                            GameUI_1(socket, rand, 0);
                        } else if (value.equals("困难")) {
                            socket.getOutputStream().write(1);
                            GameUI_1(socket, rand, 1);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            } catch (BindException ee) {
                JPanel_homepage.EstablishRoom.doClick();
            } catch (NumberFormatException NFE) {
                JOptionPane.showMessageDialog(null, "房间号格式有误");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        JPanel_homepage.JoinRoom.addActionListener(e -> {
            try {
                int port = Integer.parseInt(JOptionPane.showInputDialog("输入房间号"));
                Socket socket = new Socket("127.0.0.1", port);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Random random = new Random();
                int rand = random.nextInt(10);
                socket.getOutputStream().write(rand);
                dos.writeUTF(username);
                JOptionPane.showMessageDialog(null, "加入房间" + port + "成功\n等待房主开始游戏");
                GameUI_2(socket, rand, socket.getInputStream().read());

            } catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(null, "输入格式有误");
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "房间不存在！");
            } catch (IllegalArgumentException ee) {
                JOptionPane.showMessageDialog(null, "房间不存在！");
            }
        });
    }
}
```



### 4、Server包

服务器与数据库的配置与操作

#### 4.1 Server类

```Java
package Server;
/*
 * @Description: 客户端连接数据库的操作，包括添加用户，注销账号，修改密码，导出Excel
 */

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Server {
    /**
     * 数据库界面的绘制与初始化
     */
    JFrame jFrame = new JFrame();
    ServerSocket serverSocket;
    ArrayList<UserData> userdata;
    Connection connection;
    JLabel label = new JLabel("  ");
    JButton jButton1 = new JButton("ADD");
    JButton jButton2 = new JButton("REMOVE");
    JButton jButton3 = new JButton("EXCEL");
    JButton jButton4 = new JButton("REFRESH");
    JTable table;
    JPanel jPanel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");          //导入jar包使用材质包
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Server();
    }

    public Server() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
            jFrame.setBounds(600, 250, 500, 500);           //之所以在外面设置，是为了刷新时不会让界面移动
            LoadCollection();
            LoadListener();
            LoadUI();
            serverSocket = new ServerSocket(111);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new DataBaseOperation(socket);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * 读取数据库，加载进入内存
     */
    public void LoadCollection() {

        String sql = "select*from userdata";
        userdata = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                userdata.add(new UserData(rs.getString("username"), rs.getInt("doublemaxscore"), rs.getInt("alonemaxscore"), rs.getString("bestcompanion")));
            }
            rs.close();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * 加载界面
     */
    public void LoadUI() {
        jFrame.setResizable(false);
        jPanel = new JPanel();
        jPanel.add(jButton1);
        jPanel.add(label);
        jPanel.add(new JLabel("     "));
        jPanel.add(jButton2);
        jPanel.add(label);
        jPanel.add(new JLabel("   "));
        jPanel.add(jButton3);
        jPanel.add(new JLabel("   "));
        jPanel.add(jButton4);
        String[][] data = new String[userdata.size()][];
        String[] type = {"用户名", "双人最好成绩", "单人最好成绩", "最佳拍档"};
        int i = 0;
        for (UserData userdata : userdata) {
            data[i] = userdata.toString().split("-");
            i++;
        }
        table = new JTable(data, type);
        JScrollPane sp = new JScrollPane(table);
        jPanel.add(sp);
        jFrame.setContentPane(jPanel);
        jFrame.setVisible(true);
    }

    /**
     * 加载监听器
     */
    public void LoadListener() {
        jButton1.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(jFrame, "输入用户名");
            if (username == null) {
                return;
            }
            String password = JOptionPane.showInputDialog(jFrame, "输入密码");
            if (password == null || password.length() < 6 || password.length() > 16) {
                JOptionPane.showMessageDialog(jFrame, "请输入6-16位的密码");
                return;
            }

            try {
                for (UserData userdata : userdata) {
                    if (username.equals(userdata.getusername())) {
                        JOptionPane.showMessageDialog(jFrame, "用户名已存在");
                        return;
                    }
                }
                this.userdata.add(new UserData(username, 0, 0, "无"));
                String sql = "insert into userdata values(?,?,alonemaxscore,doublemaxscore,bestcompanion)";

                connection.setAutoCommit(false);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.execute();
                connection.commit();
                ps.close();
                JOptionPane.showMessageDialog(jFrame, "新户账号为：" + username + "\n密码为:" + password);
                LoadUI();
            } catch (SQLIntegrityConstraintViolationException ee) {
                JOptionPane.showMessageDialog(null, "用户已存在");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        /**
         * 注销账户
         */
        jButton2.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(jFrame, "输入要移除的账户");
            if (username != null) {
                try {
                    UserData removeduser;
                    for (UserData userdata : userdata) {
                        if (username.equals(userdata.getusername())) {
                            removeduser = userdata;
                            this.userdata.remove(removeduser);
                            String sql = "delete from userdata.userdata where username=?";
                            connection.setAutoCommit(false);
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setString(1, username);
                            ps.execute();
                            connection.commit();
                            ps.close();
                            JOptionPane.showMessageDialog(jFrame, "移除该账号成功：\n" + username);
                            LoadUI();
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(null, "此账号不存在：\n" + username);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        });
        /**
         * 导出Excel
         */
        jButton3.addActionListener(e -> {

            HSSFWorkbook wkb = new HSSFWorkbook();
            HSSFSheet sheet = wkb.createSheet("信息表");
            HSSFRow row1 = sheet.createRow(0);
            HSSFCell cell = row1.createCell(0);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            cell.setCellValue("用户信息表 " + df.format(System.currentTimeMillis()));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            HSSFRow row2 = sheet.createRow(1);
            row2.createCell(0).setCellValue("用户名");
            row2.createCell(1).setCellValue("双人最好成绩");
            row2.createCell(2).setCellValue("单人最好成绩");
            row2.createCell(3).setCellValue("最佳拍档");
            int i = 2;
            for (UserData userdata : userdata) {
                HSSFRow row3 = sheet.createRow(i);
                i++;
                row3.createCell(0).setCellValue(userdata.getusername());
                row3.createCell(1).setCellValue(userdata.getDoublemaxscore());
                row3.createCell(2).setCellValue(userdata.getAlonemaxscore());
                row3.createCell(3).setCellValue(userdata.getBestcompanion());
            }

            FileOutputStream output = null;
            try {
                output = new FileOutputStream("C:\\Users\\12488\\Desktop\\Project.xls");
                wkb.write(output);
                output.flush();
                JOptionPane.showMessageDialog(null,"信息已保存到C:\\Users\\12488\\Desktop\\Project.xls");
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        jButton4.addActionListener(e -> {
            LoadCollection();
            LoadUI();
       });
    }
}

```



#### 4.2 DataBaseOperation类

```Java
package Server;
/*
 * @Description: 数据库的执行，包括登录检查，注册添加信息，注销删除信息，保存写入成绩，读取获得成绩，修改密码更换信息
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

public class DataBaseOperation {
    Connection connection;
    Socket s;

    public DataBaseOperation(Socket socket) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
        s = socket;
        int a = 0;
        try {
            a = s.getInputStream().read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (a == 0) {
            Confirm();
        } else if (a == 1) {
            Register();
        } else if (a == 2) {
            saveDoubleRecord();
        } else if (a == 3) {
            saveAloneRecord();
        } else if (a == 4) {
            getDoubleRank();
        } else if (a == 5) {
            getAloneRank();
        } else if (a == 6) {
            Modify();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Modify() {
        String sql = "select*from userdata.userdata where username=?";
        String sql2 = "update userdata.userdata set password=? where username=?";
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String username = dis.readUTF();
            String oldpassword = dis.readUTF();
            String newpassword = dis.readUTF();
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("password").equals(oldpassword)) {
                    PreparedStatement ps2 = connection.prepareStatement(sql2);
                    ps2.setString(1, newpassword);
                    ps2.setString(2, username);
                    ps2.execute();
                    ps2.close();
                    connection.commit();
                    dos.write(1);
                } else {
                    dos.write(0);
                }
            }
            ps.close();
            rs.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Confirm() {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String username = dis.readUTF();
            String st = dis.readUTF();
            String password = null;
            String sql = "select*from userdata.userdata where username=?";
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            connection.commit();
            boolean b = rs.next();


            if (b) {            //账号正确，判断密码
                password = rs.getString("password");
                if (st.equals(password)) {
                    s.getOutputStream().write(0);           //0代表登录成功
                } else {
                    s.getOutputStream().write(2);
                }           //2是密码错误
            } else {
                s.getOutputStream().write(1);           //1是用户名不存在
            }
            ps.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getAloneRank() {

        String rank = "";
        String sql = "select username,score from userdata.rank order by score desc limit 10";
        try {
            int i = 1;
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String username = dis.readUTF();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                rank += "NO." + i++ + ":        " + rs.getString("username") + "           " + rs.getInt("score") + "分\n";
            }
            dos.writeUTF("Rank" + "     " + "Username" + "     " + "Score  \n" + rank + "我的历史最好成绩： " + getAloneMaxScore(username) + "分");
            rs.close();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void saveAloneRecord() {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int score = dis.readInt();
            String p1_user_name = dis.readUTF();
            if (score > getAloneMaxScore(p1_user_name)) {
                String sql = "update userdata.userdata set alonemaxscore=" + score + " where username='" + p1_user_name + "'";
                connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
                Statement ps = connection.createStatement();
                ps.executeUpdate(sql);
            }
            String sql = "insert into userdata.rank values(id,?,?)";
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, p1_user_name);
            ps.setInt(2, score);
            ps.execute();
            ps.close();
            connection.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDoubleRecord() {
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int totalscore = dis.readInt();
            System.out.println(totalscore);
            String p1_user_name = dis.readUTF();
            String p2_user_name = dis.readUTF();
            String double_username = p1_user_name + "与" + p2_user_name;
            if (totalscore > getDoubleMaxScore(p1_user_name)) {
                String sql = "update userdata.userdata set doublemaxscore=" + totalscore + ",bestcompanion = '" + p2_user_name + "' where username='" + p1_user_name + "'";
                connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
                Statement ps = connection.createStatement();
                ps.executeUpdate(sql);
            }
            if (totalscore > getDoubleMaxScore(p2_user_name)) {
                String sql = "update userdata.userdata set doublemaxscore=" + totalscore + ",bestcompanion = '" + p1_user_name + "' where username='" + p2_user_name + "'";
                connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
                Statement ps = connection.createStatement();
                ps.executeUpdate(sql);
            }
            String sql = "insert into userdata.doublerank values(id,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            ps.setString(1, double_username);
            ps.setInt(2, totalscore);
            ps.execute();
            ps.close();
            connection.commit();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDoubleRank() {
        String sql = "select doubleusername,totalscore from userdata.doublerank order by totalscore desc limit 10";
        String sql2 = "select bestcompanion from userdata.userdata where username=?";
        String rank = "";
        String mybestcompanion = "";
        try {
            int i = 1;
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String username = dis.readUTF();
            PreparedStatement ps = connection.prepareStatement(sql);
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            connection.setAutoCommit(false);
            ps2.setString(1, username);
            ResultSet rs = ps.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            connection.commit();
            while (rs.next()) {
                rank += "NO." + i++ + ":   " + rs.getString("doubleusername") + "     " + rs.getInt("totalscore") + "分\n";
            }
            while (rs2.next()) {
                mybestcompanion = rs2.getString("bestcompanion");
            }
            rs.close();
            rs2.close();
            ps.close();
            ps2.close();
            dos.writeUTF("Rank" + "     " + "Username" + "        " + "Score  \n" + rank + "我的历史最好成绩：" + getDoubleMaxScore(username) + "分\n" + "最佳拍档：" + mybestcompanion);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDoubleMaxScore(String username) {
        int my_max_double_score = 0;
        String sql = "select doublemaxscore from userdata.userdata where username=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            connection.commit();

            while (rs.next()) {
                my_max_double_score = rs.getInt("doublemaxscore");
            }
            rs.close();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return my_max_double_score;
    }

    public int getAloneMaxScore(String username) {
        int my_max_alone_score = 0;
        String sql = "select alonemaxscore from userdata.userdata where username=?;";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            connection.commit();
            while (rs.next()) {
                my_max_alone_score = rs.getInt("alonemaxscore");
            }
            rs.close();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return my_max_alone_score;
    }


    public void Register() {

        String sql = "insert into userdata.userdata values(?,?,alonemaxscore,doublemaxscore,bestcompanion)";
        try {

            DataInputStream dis = new DataInputStream(s.getInputStream());
            String username = dis.readUTF();
            String password = dis.readUTF();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.execute();
            connection.commit();
            ps.close();
            s.getOutputStream().write(0);			//0是注册成功
        } catch (SQLIntegrityConstraintViolationException e) {
            try {
                s.getOutputStream().write(1);			//1是用户名已存在
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```



#### 4.3 Userdata类

```Java
package Server;
/*
 * @Description: 从数据库获取信息
 */

public class UserData {
    private String username;
    private int doublemaxscore;
    private int alonemaxscore;
    private String bestcompanion;

    public UserData(String username, int doublemaxscore, int alonemaxscore, String bestcompanion) {
        this.username = username;
        this.doublemaxscore = doublemaxscore;
        this.alonemaxscore = alonemaxscore;
        this.bestcompanion = bestcompanion;
    }

    public String toString() {
        return username + "-" + doublemaxscore + "-" + alonemaxscore + "-" + bestcompanion;
    }

    public int getDoublemaxscore() {
        return doublemaxscore;
    }

    public int getAlonemaxscore() {
        return alonemaxscore;
    }

    public String getBestcompanion() {
        return bestcompanion;
    }

    public String getusername() {
        return username;
    }
}

```



### 5、数据库

本项目连接MySQL数据库，userdata下辖3个表

5.1 userdata表：username、password、alonemaxscore、doublemaxscore、bestcompanio

5.2 rank表：id、username、score

5.3 doublerank表：id、doubleusername、totalscore



## 四、用户手册

### 1、单机游玩

（1）运行Server类，开启客户端和数据库

  (2)  运行Main类，执行注册登录等操作，选择单人模式，支持选择难度，方向键上下左右控制英雄机移动，空格发射子弹

### 2、联机对战

（1）运行Server类，开启客户端和数据库

（2）运行Main类，执行注册登录等操作，选择创建房间，获得房间号

（3）运行Main2类，执行注册登录等操作，选择加入房间，输入房间号，进行联机对战，操作同单人模式

### 3、注册功能

（1）运行Server类，开启客户端和数据库

（2）运行Main类，执行注册操作，输入用户名和密码，完成注册

### 4、查看排行榜

（1）运行Server类，开启客户端和数据库

（2）运行Main类，执行登录操作，选择查看排行榜，可选择单人/双人排行榜

### 5、设置音乐和音效

（1）运行Server类，开启客户端和数据库

（2）运行Main类，执行登录操作，选择设置，点击设置音乐和音效的开关

### 6、修改密码

（1）运行Server类，开启客户端和数据库

（2）运行Main类，执行登录操作，选择修改密码，输入旧密码和新密码，完成修改

### 7、注销账户

（1）运行Server类，开启客户端和数据库

（2）运行Main类，执行登录操作，选择注销，再次确定，完成注销

### 8、导出Excel表

（1）运行Server类，开启客户端和数据库，在客户端选择Excel，将自动生成Excel

## 五、项目总结

1、巩固了Java的基础技术，应用到实践中更能体会技术的理论与实践的差异

2、初步学习MySQL数据库知识，学习了数据库语法

3、初步了解到了立项申请，日报，结题报告的规范

4、认识到Java项目的开发流程，构思环节

5、充分体会到预先构思架构的重要性，大部分时间都是构思逻辑，还要花费大量时间debug，真正写代码的时间反而是最少的

6、技术不可能刚巧都会，在项目的推进过程中，随时学习是必须的，快速查看文档掌握应用方法是必备技能

7、充分认识到变量命名，代码规范的重要性
