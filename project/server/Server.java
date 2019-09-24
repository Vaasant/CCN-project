import java.net.*;
import java.io.*;

public class Server {
    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    // constructor with port
    public Server(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");
            while (true) {
                socket = server.accept();
                System.out.println("Client accepted");

                // takes input from the client socket
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());

                String line = "";

                // reads message from client until "Over" is sent
                try {
                    line = in.readUTF();
                    System.out.println(line);
                    String input[] = line.split(" ");
                    if (input[0].equals("GET")) {
                        String fileName = input[1];
                        File file = new File(fileName);
                        if (file.exists()) {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String response = "HTTP/1.1 200 OK";
                            out.writeUTF(response);
                            String st;
                            while ((st = br.readLine()) != null)
                                out.writeUTF(st);
                            System.out.println("sending 200 ok");
                        } else {
                            String response = "HTTP/1.1 404 Not Found";
                            out.writeUTF(response);
                        }

                    } else if (input[0].equals("PUT")) {
                        System.out.println("inside put" + input[1]);
                        
                        FileOutputStream outputStream = new FileOutputStream(input[1]);

                        line = in.readUTF();
                        byte[] strToBytes = line.getBytes();
                        outputStream.write(strToBytes);

                        
                        System.out.println(line);
                        while (line != null) {
                            System.out.println(line);
                            outputStream.write(line.getBytes());
                            line = in.readUTF();
                        }

                        outputStream.close();

                        String response = "HTTP/1.1 200 Created";
                        out.writeUTF(response);

                    }

                } catch (IOException i) {
                    System.out.println(i);
                }

                System.out.println("Closing connection");

                // close connection
                socket.close();
                in.close();
            }

        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}
