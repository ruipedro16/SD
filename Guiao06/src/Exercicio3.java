import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerWorker implements Runnable {
    private Socket socket;

    public ServerWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            int sum = 0;
            int n = 0;
            String line;
            while ((line = in.readLine()) != null) {
                try {
                    sum += Integer.parseInt(line);
                    n++;
                } catch (NumberFormatException e) {
                    // Ignore invalid integers
                }
            }

            out.println(sum);
            out.flush();

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Exercicio3 {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12345);

        while (true) {
            Socket socket = ss.accept();
            Thread worker = new Thread(new ServerWorker(socket));
            worker.start();
        }

    }
}
