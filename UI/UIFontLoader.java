package UI;
 
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
 
public class UIFontLoader {
 
    private static Font baseFont;
 
    static {
        try (InputStream is = UIFontLoader.class.getResourceAsStream("/assets/fonts/PressStart2P-Regular.ttf")) {
 
            if (is == null) {
                System.err.println("ERREUR: ressource de police /assets/fonts/PressStart2P-Regular.ttf introuvable !");
                baseFont = new Font("SansSerif", Font.PLAIN, 12);
            } else {
                baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            }
 
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            baseFont = new Font("SansSerif", Font.PLAIN, 12);
        }
    }
 
    public static Font get(float size) {
        return baseFont.deriveFont(size);
    }
}