package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: Client
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