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
            backgroundmusic.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/background.wav")));
            Shootmusic.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/shoot.wav")));
            Shootmusic2.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/shoot.wav")));
            Shootmusic3.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/shoot.wav")));
            boommusic.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/boom1.wav")));
            boommusic2.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/boom2.wav")));
            boommusic3.open(AudioSystem.getAudioInputStream(new File("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Audio/boom3.wav")));
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
