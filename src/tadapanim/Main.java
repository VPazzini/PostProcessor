package tadapanim;

import javax.swing.JFrame;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        TADAPAnim tadap = new TADAPAnim();
        JFrame frame = new JFrame();
        frame.add(tadap);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("Animation");

        tadap.load("nodes/fort.1001");
        tadap.load("nodes/fort.1002");
        tadap.load("nodes/fort.1003");
        tadap.load("nodes/fort.1004");
        tadap.load("nodes/fort.1005");
        tadap.load("nodes/fort.1006");
        tadap.load("nodes/fort.1007");
        tadap.load("nodes/fort.1008");
        tadap.load("nodes/fort.1009");
        tadap.addSupport(8E-5);
        tadap.load("nodes/fort.1010");
        tadap.load("nodes/fort.1011");

        frame.setVisible(true);
        //while (tadap.save()) {
        //}
        while(true){
            tadap.repaint();
            Thread.sleep(5);
        }

    }
}
