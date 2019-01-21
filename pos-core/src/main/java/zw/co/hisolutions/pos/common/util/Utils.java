package zw.co.hisolutions.pos.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

public class Utils {

    public static String prepairString(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "";
        }
        return email;
    }
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static String prepairStringForQuery(String val) {
        return "%" + prepairString(val).replaceAll("\\s", "%") + "%";
    }

    public static boolean empty(String name) {
        if (name == null) {
            return true;
        }
        if (name.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static String padStringRight(String fullname,int len) {
        return ((fullname.length() > len) ? fullname.substring(0, len) : String.format(("%-"+len+"s"), fullname));
    }
    public static String padStringLeft(String fullname,int len) {
        return ((fullname.length() > len) ? fullname.substring(0, len) : String.format(("%"+len+"s"), fullname));
    }

    public static Integer parseInt(String val, String defaultVal) {
        try {
            return Integer.valueOf(val);
        } catch (Exception e) {
        }
        try {
            return Integer.valueOf(defaultVal);
        } catch (Exception e) {
        }
        return 0;
    }

    public static Long parseLong(String val, String defaultVal) {
        try {
            return Long.valueOf(val);
        } catch (Exception e) {
        }
        try {
            return Long.valueOf(defaultVal);
        } catch (Exception e) {
        }
        return 0l;
    }

    public static String parseDefaultIfEmpty(String get, String defaultValue) {
        if (!empty(get)) {
            return prepairString(get);
        }
        return prepairString(defaultValue);
    }

    public static Date parseDateDayStart(String get, String defaultValue) {
        Date d;
        try {
            d = sdf.parse(get);
            return d;
        } catch (Exception ex) {
            try {
                d = sdf.parse(defaultValue);
            } catch (Exception ex1) {
            }
        }
        d = new Date();
        return dateStart(d);
    }

    public static Date parseDateNullable(String get) {
        Date d;
        try {
            d = sdf.parse(get);
            return d;
        } catch (Exception ex) {
        }
        return null;
    }

    public static Date parseDateDayEnd(String get, String defaultValue) {
        Date d = parseDateDayStart(get, defaultValue);
        return new Date(d.getTime() + (24 * 60 * 60 * 1000 - 1));
    }

    public static Object parseIfEmpty(String get, String defaultValue) {
        if (!empty(get)) {
            return prepairString(get);
        }
        return prepairString(defaultValue);
    }

    public static Object parseLikeDefaultIfEmpty(String get, String defaultValue) {
        if (!empty(get)) {
            return prepairStringForQuery(get);
        }
        return prepairStringForQuery(defaultValue);
    }

    private static Date dateStart(Date d) {
        return new DateTime(d).withTimeAtStartOfDay().toDate();
    }
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
public static String showDisplayDate(Date date)
    {
    return new SimpleDateFormat("dd MMM yyyy").format(date);
    }
}
