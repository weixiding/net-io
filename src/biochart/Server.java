package biochart;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端多人聊天室
 */
public class Server {
    private static int port = 9999;
    //存储多人队列
    private Map<Integer, Socket> clientMap;

    private ExecutorService executorService;

    //新客户端添加到多人对垒
    public synchronized void add(Socket socket) {
        clientMap.put(socket.getPort(),socket);
    }
    //删除客户端
    public synchronized void deleted(Socket socket) {
        clientMap.remove(socket.getPort());
    }
    //发送数据
    public synchronized void sendMessage(Socket socket,String message) throws IOException {

        for (Integer integer : clientMap.keySet()) {
            if(socket.getPort() != integer) {
                //发送数据
                BufferedWriter bufferedWriter = null;
                try {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientMap.get(integer).getOutputStream()));
                    bufferedWriter.write(message + "\n");
                    bufferedWriter.flush();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket accept = serverSocket.accept();
                executorService.execute(new ServerHandler(accept, this));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null) {
                serverSocket.close();
            }
        }


    }

    public Server() {
        this.clientMap = new HashMap<>();
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }


}

class ServerHandler implements Runnable{
    private Socket clientSocker;
    private Server server;
    private static String QUIT = "QUIT";

    public ServerHandler(Socket clientSocker, Server server) {
        this.clientSocker = clientSocker;
        this.server = server;
    }

    @Override
    public void run() {
        //添加到队列中
        server.add(clientSocker);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocker.getInputStream()));
            String message = null;
            //读取消息
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println(message);
                server.sendMessage(this.clientSocker,message);
                if(QUIT.equals(message)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            server.deleted(clientSocker);
            if(clientSocker != null) {
                try {
                    clientSocker.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}