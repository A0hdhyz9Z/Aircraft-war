package Server;
/*
 * @Project_name: Aircraft war
 * @Class_name: UserDate
 *
 * @author: Li Zonghao
 * @Email: bjhdlzh@gmail.com || bjhdlzh@163.com
 * @Date: 2021/7/22
 * @Copyright: ©2021 Li Zonghao . All rights reserved.
 * 	           For commercial reprints, please contact the author for authorization.
 * 	           For non-commercial reprints, please indicate the source.
 *
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
