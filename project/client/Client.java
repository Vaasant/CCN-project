import java.net.*;
import java.io.*;

public class Client {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    // constructor to put ip address and port
    public Client(String address, int port, String command, String fileName) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            // if the command is get http request
            // if()

            // string to read message from input
            if (command.equals("GET")) {
                // String request = "GET https://www.amazon.com HTTP/1.1\n"
                String request = "GET " + fileName + " HTTP/1.1\n"
                 + "Host:" + address + "\n" + "Connection:close"
                        + "\n" + "accept: text/html,image/gif,image/jpeg\n" + "accept-language: en-US\n"
                        + "accept-encoding: gzip, deflate\n" + "User-Agent : Mozilla/4.0 \n"
                        +"content-type:text/html\n";
                // request = request + ""
                // +"Connection : Keep-Alive\n";
                // + "path: /index.html\n"
                // +"accept:
                // text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n"
                // +"accept-encoding: gzip, deflate, br\n"
                // +"accept-language: en-US,en;q=0.9";
                System.out.println(request);
                
                // line = input.readLine();
                out.writeBytes(request);
                // out.writeUTF(request);
                // out.close();
                String line = input.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = input.readLine();

                }
                

            } else if (command.equals("PUT")) {

                // String request = "GET https://www.amazon.com HTTP/1.1\n"
                String request = "PUT " + fileName + " HTTP/1.1\n";
                System.out.println(request);
                out.writeUTF(request);

                File file = new File(fileName);
                if (file.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = br.readLine()) != null) {

                        out.writeUTF(st);
                        System.out.println("sending THE FILE CONTENT");
                    }
                    out.close();

                }

                String line = input.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = input.readLine();

                }

            }

            // close the connection

            input.close();
            out.close();
            socket.close();

        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

    }

    public static void main(String args[]) {
        String IP = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String filename = args[3];
        Client client = new Client(IP, port, command, filename);
    }
}
