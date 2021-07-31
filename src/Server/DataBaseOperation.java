package Server;
/*
 * @Project_name: Aircraft war
 * @Class_name: DateBaseOperation
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/22
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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

/*    public void connectdatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/localhost?serverTimezone=UTC", "root", "LZH1217HZL");
           // connection = ConnectionPool.getConn();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }*/

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
//                PreparedStatement ps = connection.prepareStatement(sql);
                connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
                Statement ps = connection.createStatement();
                ps.executeUpdate(sql);
//                ps.setInt(1, score);
//                ps.setString(2, p1_user_name);
//                connection.setAutoCommit(false);
//                ps.execute();
//                connection.commit();
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
//                PreparedStatement ps = connection.prepareStatement(sql);
//                ps.setInt(1, totalscore);
//                ps.setString(2, p2_user_name);
//                ps.setString(3, p1_user_name);
//                connection.setAutoCommit(false);
//                ps.execute();
//                connection.commit();
            }
            if (totalscore > getDoubleMaxScore(p2_user_name)) {
                String sql = "update userdata.userdata set doublemaxscore=" + totalscore + ",bestcompanion = '" + p1_user_name + "' where username='" + p2_user_name + "'";
                connection = DriverManager.getConnection("jdbc:mysql:///userdata", "root", "LZH1217HZL");
                Statement ps = connection.createStatement();
                ps.executeUpdate(sql);
//                PreparedStatement ps = connection.prepareStatement(sql);
//                ps.setInt(1, totalscore);
//                ps.setString(2, p1_user_name);
//                ps.setString(3, p2_user_name);
//                connection.setAutoCommit(false);
//                ps.execute();
//                connection.commit();
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
            s.getOutputStream().write(0);//0是注册成功
        } catch (SQLIntegrityConstraintViolationException e) {
            try {
                s.getOutputStream().write(1);//1是用户名已存在
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
