import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {

    private static final String Prefix = "D:/geekbrains/OOP/Practice_5/web";
    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                for(;;) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected");
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream(), StandardCharsets.UTF_8));

                    PrintWriter writer = new PrintWriter(
                            socket.getOutputStream());

                    while (!reader.ready()) ;

                    String[] items = new String[10];
                    if (reader.ready()){
                        String line = reader.readLine();
                        items = line.split(" ");
                    }

                    while (reader.ready()) {
                        System.out.println(reader.readLine());
                    }
                    Path fileLink = Path.of(Prefix, items[1]);
                    if (Files.exists(fileLink) && !Files.isDirectory(fileLink)) {
                        writer.println("HTTP/1.1 200 OK");
                        writer.println("Content-Type: text/html; charset=utf-8");
                        writer.println();
                        try (BufferedReader br = Files.newBufferedReader(fileLink)){
                            br.transferTo(writer);
                        }
                        writer.flush();
                    }else{

                    writer.println("HTTP/1.1 404 OK");
                    writer.println("Content-Type: text/html; charset=utf-8");
                    writer.println();
                    writer.println("<h1>File is not exist</h1>");
                    writer.flush();}

                   // socket.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
