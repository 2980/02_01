/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ichabod;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author unouser
 */
public class ThreadedServer {
    
    public static void main(String[] args) throws IOException {
        new ThreadedServer();
    }

    public ThreadedServer() throws IOException {
        
        System.out.println("Started Ichabod in " +
              System.getProperty("user.dir") + ". Have a nice day.");
        
        int socketNum = 5001;
        
        ServerSocket serverSocket = new ServerSocket(socketNum);
        
        System.out.println("Ichabod is listening on " + socketNum + ". Hope your day is even better.");
        
        
        while(true)
        {
            Socket socket = serverSocket.accept();
            
            StarterSocket runnableSocket = new StarterSocket(socket);
            
            new Thread(runnableSocket).start();
        }
        
    }
}
    
    
    
