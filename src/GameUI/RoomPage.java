package GameUI;
/*
 * @Project_name: Aircraft war
 * @Class_name: RoomPage
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/21
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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

        //Image Image_background = new ImageIcon("E:/Li Zonghao/Li Zonghao Date/北京交通大学/我的资料/课程学习/大一下2021.3/软件工程实训 陈旭东/Project/Resources/Image/background.jpg").getImage();
        g.drawImage(GameUI.ImageUnion.Image_background, 0, 0,1200 ,950,null);

    }
}
