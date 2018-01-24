
package ichabod;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author B Ricks, PhD <bricks@unomaha.edu>
 */
public class Processor {

    /**
     * Process a command
     * @param command The command to process
     * @param file The file to process the command on
     * @param arguments A hashmap of the arguments passed with the command
     * @return The filename of the resulting temp image on success, null otherwise
     */
    public String Process(String command, String file, HashMap<String, String> arguments) {
        //Generate a new temp file name
        String filename = "" + Math.random() + ".png";
        try {
            //Read the original image
            BufferedImage bi = ImageIO.read(new File(file));

            int width = bi.getWidth();
            int height = bi.getHeight();

            //Generate a new image in memory
            BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            //Choose the right method
            if (command.equals("grayscale")) {
                grayscale(bi, out);
            }
            else if(command.equals("monochrome")){
                monochrome(bi, out);
            }
            else
                return null;
            
            //Write the image
            ImageIO.write(out, "png", new File(filename));
            return "\\" + filename;
        } catch (IOException ex) {
            Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Turn a color image into grayscale
     * @param bi The original image
     * @param out The modified image
     */
    private void grayscale(BufferedImage bi, BufferedImage out) {
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                Color pixel = new Color(bi.getRGB(x, y));
                
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                
                //Set to grayscale
                r = g;
                b = g;
                
                //Prevent an exception by keeping values within [0,255]
                r = clamp255(r);
                g = clamp255(g);
                b = clamp255(b);
                
                
                Color newColor = new Color(r, g, b);
                
                out.setRGB(x, y, newColor.getRGB());
                
            }
        }
    }

    /**
     * The list of commands we accept
     * @return The list of commands we accept
     */
    public String[] validCommands() {
        return new String[]{"grayscale", "monochrome"};
    }

    /**
     * Keep an int value within 0 and 255
     * @param i The value to clamp
     * @return a number between 0 and 255 inclusively
     */
    private int clamp255(int i) {
        if (i < 0) {
            return 0;
        }
        if (i >= 255) {
            return 255;
        }
        return i;
    }

    /**
     * Keep an float between 0 and 1
     * @param f The float to clamp
     * @return a float between 0 and 1 inclusively
     */
    private float clamp1(float f) {
        if (f <= 0) {
            return 0;
        }
        if (f >= 1) {
            return 1;
        }
        return f;
    }

    /**
     * Convert an image to monochrome.
     * @param bi The original image
     * @param out The modified image
     */
    private void monochrome(BufferedImage bi, BufferedImage out) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = new Color(bi.getRGB(x, y));
                
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                
                
                
                //Prevent an exception by keeping values within [0,255]
                r = clamp255(r);
                g = clamp255(g);
                b = clamp255(b);
                
                
                Color newColor = new Color(r, g, b);
                
                out.setRGB(x, y, newColor.getRGB());
                
            }
        }
    }

    /**
     * Try to add a value to an array if the x and y values are valid
     * @param error The array to add to
     * @param remainingError The value to add
     * @param width The length of the first dimension
     * @param height The length of the second dimension
     * @param x The index of the first dimension
     * @param y The index of the second dimension
     * @return True of a value was update, false otherwise
     */
    private boolean tryadd(int[][] error, int remainingError, int width, int height, int x, int y) {
        if( x < 0 || x >= width) return false;
        if( y < 0 || y >= height) return false;
        
        error[x][y] += remainingError;
        
        
        return true;
        
        
    }

}
