package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: HomePage
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/20
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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

        //Image Image_background = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/background.jpg").getImage();
        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

        //Image Image_LOGE = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/LOGE.jfif").getImage();
        g.drawImage(GameUI.ImageUnion.Image_LOGE, 375, 200,400 ,200,null);
    }
}
