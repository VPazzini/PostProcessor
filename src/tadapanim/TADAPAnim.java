package tadapanim;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TADAPAnim extends JPanel {

    private Graphics2D g2d;

    private final ArrayList<ArrayList<int[]>> list = new ArrayList<>();
    private final ArrayList<Point[]> orSupport = new ArrayList<>();
    private final ArrayList<Point[]> support = new ArrayList<>();
    private int ind = 0;
    private int nodeNum = 1;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        Point p = null;
        for (ArrayList<int[]> nodePos : list) {
            int temp = ind;
            if (temp >= nodePos.size()) {
                temp = nodePos.size() - 1;
            }

            Point nPoint = new Point(nodePos.get(temp)[0] + 10, nodePos.get(temp)[1] + 10);
            g2d.drawOval(nodePos.get(temp)[0], nodePos.get(temp)[1], 20, 20);
            int i = 0;
            for (Point[] ps : support) {
                if (ps[0].distance(nPoint) <= 10) {
                    ps[0] = new Point(nPoint.x, nPoint.y - 10);
                }
                if (ps[0].distance(nPoint) > 10 && ps[0].distance(nPoint) < 15 && ps[0].distance(orSupport.get(i)[0]) > 1) {
                    ps[0] = new Point(nPoint.x, nPoint.y - 10);
                }

                if (ps[1].distance(nPoint) <= 10) {
                    ps[1] = new Point(nPoint.x, nPoint.y + 10);;
                }
                if (ps[1].distance(nPoint) > 10 && ps[1].distance(nPoint) < 15 && ps[1].distance(orSupport.get(i)[1]) > 1) {
                    ps[1] = new Point(nPoint.x, nPoint.y + 10);
                }
                i++;
            }
            if (p != null) {
                g2d.drawLine(nodePos.get(temp)[0] + 10, nodePos.get(temp)[1] + 10, p.x + 10, p.y + 10);
            }
            p = new Point(nodePos.get(temp)[0], nodePos.get(temp)[1]);
            ind++;
        }
        int i = 0;
        for (Point[] ps : support) {
            g2d.draw(drawSupport(ps[0], (int) (60 - ps[0].distance(orSupport.get(i)[0])), true));
            g2d.draw(drawSupport(ps[1], (int) (60 - ps[1].distance(orSupport.get(i)[1])), false));
            //g2d.drawLine(ps[0].x - 10, ps[0].y, ps[0].x + 10, ps[0].y);
            //g2d.drawLine(ps[1].x - 10, ps[1].y, ps[1].x + 10, ps[1].y);
            i++;
        }
    }

    public boolean save() {
        BufferedImage bImg = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        cg.setRenderingHints(rh);

        Point p = null;
        for (ArrayList<int[]> nodePos : list) {
            if (ind < nodePos.size()) {
                cg.drawOval(nodePos.get(ind)[0], nodePos.get(ind)[1], 20, 20);
                if (p != null) {
                    cg.drawLine(nodePos.get(ind)[0] + 10, nodePos.get(ind)[1] + 10, p.x + 10, p.y + 10);
                }
                p = new Point(nodePos.get(ind)[0], nodePos.get(ind)[1]);

            } else {
                return false;
            }
        }
        ind++;

        if (ind % 10 == 0) {
            try {
                ImageIO.write(bImg, "jpg", new File("video/teste" + ind + ".jpg"));
            } catch (IOException ex) {
                Logger.getLogger(TADAPAnim.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(ind);
        cg.dispose();
        return true;

    }

    public void load(String path) {
        try {
            FileReader fReader = new FileReader(path);
            BufferedReader bf = new BufferedReader(fReader);

            String s;
            String[] temp;

            ArrayList<int[]> tempList = new ArrayList<>();

            while ((s = bf.readLine()) != null) {
                int index = 0;
                int[] pos = new int[3];
                temp = s.split(" ");
                int tempPos;
                boolean x = true;
                for (String st : temp) {
                    try {
                        tempPos = (int) (Double.parseDouble(st) * 2E6);
                        if (!x) {
                            tempPos += 410;
                        }
                        if (x) {
                            tempPos += 100 * nodeNum;
                            x = false;
                        }
                        pos[index++] = tempPos;
                    } catch (NumberFormatException e) {
                    }
                }

                tempList.add(pos.clone());

            }
            list.add(tempList);
            nodeNum++;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TADAPAnim.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TADAPAnim.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSupport(double gap) {
        Point[] points = new Point[2];
        points[0] = new Point(100 * nodeNum + 10, 410 - (int) (2E6 * gap / 2));
        points[1] = new Point(100 * nodeNum + 10, 410 + (int) (2E6 * gap / 2));
        support.add(points);
        Point[] points2 = new Point[2];
        points2[0] = new Point(100 * nodeNum + 10, 410 - (int) (2E6 * gap / 2));
        points2[1] = new Point(100 * nodeNum + 10, 410 + (int) (2E6 * gap / 2));
        orSupport.add(points2);
        
    }

    private Shape drawSupport(Point begin, int height, boolean top) {
        if (top) {
            return drawSupportTop(begin, height);
        } else {
            return drawSupportBot(begin, height);
        }
    }

    private Shape drawSupportBot(Point begin, int height) {
        GeneralPath gp = new GeneralPath();
        int size = 10;

        gp.moveTo(begin.x - size, begin.y);
        gp.lineTo(begin.x + size, begin.y);

        gp.lineTo(begin.x + size, begin.y + size / 2);
        gp.lineTo(begin.x - size, begin.y + size / 2);

        gp.lineTo(begin.x - size, begin.y);

        gp.moveTo(begin.x, begin.y + size / 2);
        gp.lineTo(begin.x, begin.y + size / 2 + height);
        return gp;
    }

    private Shape drawSupportTop(Point begin, int height) {
        GeneralPath gp = new GeneralPath();
        int size = 10;

        gp.moveTo(begin.x - size, begin.y);
        gp.lineTo(begin.x + size, begin.y);
        gp.lineTo(begin.x + size, begin.y - size / 2);
        gp.lineTo(begin.x - size, begin.y - size / 2);
        gp.lineTo(begin.x - size, begin.y);
        gp.moveTo(begin.x, begin.y - size / 2);
        gp.lineTo(begin.x, begin.y - size / 2 - height);
        return gp;
    }

}
