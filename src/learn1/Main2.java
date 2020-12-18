package learn1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端
 */
public class Main2 {

    public static void main(String[] args) throws IOException {
        String HOST = "localhost";
        String QUIT = "QUIT";
        int PORT = 8080;
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            boolean connected = socket.isConnected();
            if (connected) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                //读取控制台输入数据
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                while(true) {
                    String message = consoleReader.readLine();
                    bufferWriter.write(message + "\n");
                    bufferWriter.flush();
                    String serverMessage = bufferedReader.readLine();
                    System.out.println("接受服务端消息：" + serverMessage);
                    if(QUIT.equals(serverMessage)) {
                        break;
                    }
                }


            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(socket != null) {
                socket.close();
            }
        }
    }
}
