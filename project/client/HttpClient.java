import java.net.*;
import java.io.*;

public class HttpClient {

    // initialize socket and input output streams
    private Socket socket = null;
    private BufferedReader input = null;
    private DataOutputStream out = null;

    private String buildRequestHeader(String method, String address, int port, String object) {

        String header = method + " " + "/" + object + "/ HTTP/1.1\r\n";
        header += "Host: " + address + "\r\n";

        if (method.equals("GET")) {
            header += "Connection: close" + "\r\n";
        } else if (method.equals("PUT")) {
	    File file = new File(object);
            header += "Connection: Keep-Alive\r\n";
            header += "Accept-Language: en-us\r\n";
            header += "Content-type: text/html\r\n";
            header += "Content-Length:"+file.length()+"\r\n";
        }

        header += "\r\n";

        return header;
    }

    // constructor to put ip address and port
    public HttpClient(String address, int port, String command, String filename) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
            //System.out.println(command);


            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());

            if (command.equals("GET")) {

                System.out.println("Sending GET request to the server");
                System.out.println(buildRequestHeader(command, address, port, filename));

                // prepare the request header
                out.writeBytes(buildRequestHeader(command, address, port, filename));

                // flush out the output stream
                out.flush();

                // wait for the Response
                System.out.println("Response from SERVER");

                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }

            } else if (command.equals("PUT")) {

                System.out.println("Sending PUT request to the server");

                // prepare the header
                out.writeBytes(buildRequestHeader(command, address, port, filename));
            
                
                // read the file content
                out.flush();
                BufferedReader br = new BufferedReader(new FileReader(filename));

                // add the file content
                String st;
                while ((st = br.readLine()) != null) {
                    out.writeBytes(st+ "\n");
                }
               
                socket.shutdownOutput();
                // close the file
                br.close();

                // flush the output stream
                // out.flush();

                // wait for the response
                System.out.println("Response from SERVER for PUT");
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // close all the opened streams & sockets.
            input.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public static void main(String args[]) {

        if (args.length < 4) {
            System.out.println("Please Enter Valid Arguments :");
            System.out.println("args[0]: Server IP");
            System.out.println("args[1]: Port");
            System.out.println("args[2]: Command(GET/PUT)");
            System.out.println("args[3]: FileName");
            return ;
        }
        File file = new File(args[3]);
	if(!args[2].equals("PUT") && !args[2].equals("GET")) {
		System.out.println("Only GET/PUT Command allowed");
		return;	
	}
        // file is there ?
        if (!file.exists() && args[2].equals("PUT")) {
            System.out.println("The system cannot find the file specified");
            return;
        }
	
	
        
        HttpClient client = new HttpClient(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        return;

    }

}
