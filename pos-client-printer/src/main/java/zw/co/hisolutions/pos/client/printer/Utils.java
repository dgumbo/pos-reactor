package zw.co.hisolutions.pos.client.printer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

public class Utils {

    public static Image getBarcode(String value) {
        Code128Bean barcode = new Code128Bean();          
        barcode.setModuleWidth(5.0);
        barcode.setBarHeight(80.0);
        barcode.setFontSize(20.0);
        barcode.setPattern("");
        barcode.doQuietZone(false);
        BarcodeDimension dim = barcode.calcDimensions(value);
        int width = (int) dim.getWidth(0);
        int height = (int) dim.getHeight(0);
        barcode.setBarHeight(height);
        
        BufferedImage imgtext = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imgtext.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);

        try {
            barcode.generateBarcode(new Java2DCanvasProvider(g2d, 0), value);
        } catch (IllegalArgumentException e) {
            g2d.drawRect(0, 0, width - 1, height - 1);
            g2d.drawString(value, 2, height - 3);
            System.err.println(e);
        }

        g2d.dispose();
        return imgtext;
    }

}
