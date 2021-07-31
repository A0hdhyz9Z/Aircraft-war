
/*
 * @Project_name: Aircraft war
 * @Class_name: Main2
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
        } //try catch块设置了Swing皮肤
    }
}