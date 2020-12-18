package learn1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class Main {

    public static void main(String[] args) throws IOException {
        int PORT = 8080;
        String QUIT = "QUIT";
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while(true) {
                Socket accept = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
                String message = null;

                while ( (message = bufferedReader.readLine()) != null) {
                    System.out.println(accept.getPort() + "客户端发送内容:" + message);
                    bufferedWriter.write(message + "\n");
                    bufferedWriter.flush();
                    if (QUIT.equals(message)) {
                        break;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
