package biochart;

import java.io.*;
import java.net.Socket;

public class Client {
    private static int port = 9999;
    private static String ip = "localhost";

    public void start() throws IOException {
        Socket socket = new Socket(ip, port);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String message = null;
            new Thread(new ClientHandler(bufferedWriter)).start();
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println(message);
            }
        }catch (Exception e) {

        }finally {
            if(socket != null) {
                socket.close();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }


}

class ClientHandler implements Runnable{
    private BufferedWriter outputStream;
    private static String QUIT = "QUIT";

    public ClientHandler(BufferedWriter outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String message = null;
                //读取消息
                while ((message = bufferedReader.readLine()) != null) {
                    this.sendMessage(this.outputStream,message);
                    if(QUIT.equals(message)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发送数据
    public  void sendMessage(BufferedWriter outputStream,String message) throws IOException {
        outputStream.write(message + "\n");
        outputStream.flush();
    }

}
