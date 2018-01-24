package ichabod;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class StarterSocket implements Runnable {

    Socket socket;
    PrintWriter out;
    Scanner in;

    /**
     * Create an instance of this object with a reference to the socket we need
     *
     * @param inSocket The socket to process
     */
    public StarterSocket(Socket inSocket) {
        this.socket = inSocket;
    }

    /**
     * The actual processing of our socket. We grab the GET header line and then
     * process the provided path
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());

            boolean cont = true;
            while (cont && in.hasNextLine()) {
                String line = in.nextLine();

                if (line.startsWith("GET ")) {
                    String[] splits = line.split(" ");
                    String path = splits[1];
                    System.out.println("GET request for " + path);
                    handleRequest(path);
                } else if (line.equals("")) {
                    cont = false;
                }
            }
            //handleRequest("/mountains.jpeg");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            out.close();

        }

        try {
            socket.close();
            System.out.println("Closed");
        } catch (IOException ex) {
            Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Do something with the path we got from the GET request
     *
     * @param path The requested path (potentially including variables)
     */
    private void handleRequest(String path) {

        //try {
        path = path.substring(1);

        if (path.equals("")) {
            path = "index.html";
        }

        if (isFile(path)) {
            do200(path);

        } else {
            handle404();
        }

        return;

        /* if(path.equals("index.html"))
            {
                if(isFile(path))
                {
                    String file = slurp(path);
                    handle200(file);
                }
                else
                {
                    handle404();
                }
            }
            else{
                if(path.startsWith("edit/"))
                {
                    String wikiName = path.substring(5);
                    String edit = slurp("edit.html");


                    String content = "";
                    if(isFile(wikiName + ".txt")) {
                        content = slurp(wikiName + ".txt");
                    }
                        String replaced = edit.replaceAll("BATMAN", content);

                        replaced = replaced.replace("ROBIN", wikiName);

                        handle200(replaced);

                }
                else if(path.startsWith("submit/"))
                {
                   
                    path = path.substring(7);
                    
                    
                    ///Write edits variable to file system.
                    String wikiName = path.substring(0, path.indexOf("?"));
                    String parameters = path.substring(path.indexOf("?")+1);
                    String[] keyValue = parameters.split("=");
                    
                    String newContents = keyValue[1];
                    newContents = newContents.replaceAll("\\+", " ");
                    
                    Files.write(Paths.get(wikiName + ".txt"), newContents.getBytes());
                    
                    handle302("/" + wikiName);
                }
                else
                {
                    String generic = slurp("generic.html");

                    if(isFile(path + ".txt"))
                    {
                        String content = slurp(path + ".txt");
                        String replaced = generic.replace("BATMAN", content);

                        replaced = replaced.replace("ROBIN", path);

                        handle200(replaced);
                    }
                    else
                    {
                        handle302("/edit/" + path);
                    }
                }
            }*/
        //} catch (IOException ex) {
        //    Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

    /**
     * Respond with 404
     */
    private void handle404() {
        String response = "Bad news, couldn't find that page.";

        out.println("HTTP/1.0 404 Not Found");
        out.println("Content-Type: text/html");
        out.println("Content-Length: " + response.length());

        out.println();
        out.println(response);
    }

    /**
     * Respond with a 302 redirect
     *
     * @param redirect The location we should redirect the browser to
     */
    private void handle302(String redirect) {
        out.println("HTTP/1.0 302 Found");
        out.println("Location: " + redirect);
        out.println("");
    }

    /**
     * Respond with a 501
     */
    private void handle501() {
        out.println("HTTP/1.0 501");
        out.println("");
    }

    private void do200(String path) {
        //try {
        File file = new File(path);

        if (path.endsWith("html")) {
            handle200(file, "text/html");
        } else {
            handle200(file, "image/png");
        }
        //} catch (IOException ex) {
        //    Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

    /**
     * Respond with a 200 OK
     *
     * @param content The body of the message
     */
    private void handle200(File file, String mimeType) {

        FileInputStream fis = null;

        /*out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: " + mimeType);
            out.println("Content-Length: " + content.length);
            
            out.println();
            out.println(content);
            out.println();*/
        out.print("HTTP/1.0 200 OK\r\n");
        
        if (mimeType.startsWith("image")) {
            out.print("Content-Type: " + mimeType + "\r\n");

            

            try {
                /*BufferedImage bi = ImageIO.read(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bi, "png", baos);
                baos.flush();
                byte[] bytes = baos.toByteArray();
                baos.close();
                
                out.print("Content-Length: " + bytes.length + "\r\n");
                out.print("\r\n");

                for (int i = 0; i < bytes.length; i++) {
                    out.write(bytes[i]);
                    int in = bytes[i] & 0xFF;
                    System.out.println( Integer.toHexString(in));
                }
                out.flush();;
                out.close();*/
                
                 fis = new FileInputStream(file);
               
                byte[] bytes = Files.readAllBytes(file.toPath());
                 out.print("Content-Length: " + bytes.length + "\r\n");
                out.print("\r\n");
                 /*for (int i = 0; i < bytes.length; i++) {
                    out.write(bytes[i]);
                    int in = bytes[i] & 0xFF;
                    System.out.println( bytes[i]);
                }*/
                 
               
                out.flush();
                socket.getOutputStream().write(bytes,0,bytes.length);
                
                
                out.close();

            } catch (IOException ex) {
                Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                //out.println("Content-Length: " + (file.length()));
                out.print("Content-Type: " + mimeType + ";charset=utf-8\r\n");

                out.print("\r\n");
                fis = new FileInputStream(file);
                int oneByte;
                while ((oneByte = fis.read()) != -1) {
                    out.write(oneByte);
                    System.out.print((char) oneByte); // could also do this
                }
                out.flush();
                Thread.sleep(100);
                out.close();
                System.out.println("Done");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(StarterSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        System.out.println("Returning");
    }

    /**
     * Read an entire file into a string
     *
     * @param f The file to read
     * @return The contents of the file as a string
     * @throws IOException
     */
    private byte[] slurp(File f) throws IOException {

        try (
                InputStream inputStream = new FileInputStream(f);) {

            long fileSize = f.length();

            byte[] allBytes = new byte[(int) fileSize];

            inputStream.read(allBytes);

            return allBytes;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new byte[0];
    }

    /**
     * Read the entire contents on a file into a string
     *
     * @param f The name of the file
     * @return The contents of the file as a string
     * @throws IOException
     */
    private byte[] slurp(String f) throws IOException {
        return slurp(new File(f));
    }

    /**
     * Determine if the string is the path to a file
     *
     * @param path The name of the file
     * @return true if it is a file, false if the file doesn't exist or the path
     * is a directory
     */
    private boolean isFile(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }
}
