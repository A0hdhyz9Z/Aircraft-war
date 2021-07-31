package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: RegisterPage
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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

        //Image Image_background = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/background.jpg").getImage();
        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        //Image Image_LOGE = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/LOGE.jfif").getImage();
        g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }

}
