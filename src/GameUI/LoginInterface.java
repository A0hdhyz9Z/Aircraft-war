package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: LoginInterface
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/18
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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

        //Image Image_background = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/background.jpg").getImage();
        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        //Image Image_LOGE = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/LOGE.jfif").getImage();
       g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }

}