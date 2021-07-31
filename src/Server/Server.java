package Server;
/*
 * @Project_name: Aircraft war
 * @Class_name: Server
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/22
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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
