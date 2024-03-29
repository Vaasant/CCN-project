import java.net.*;
import java.io.*;
import java.lang.reflect.Method;


class RequestHandler implements Runnable {

    BufferedReader in = null;
    DataOutputStream out=null;
    Socket s;

    // constructor
    public RequestHandler(Socket s,BufferedReader in,DataOutputStream out) {
        
            this.s = s;
            this.in = in;
            this.out = out;
                
    }

    private String buildResponse(int respCode, String msg) {
        String response = "HTTP/1.1";

        if (respCode == 200) {
            response += " 200 OK";
        } else if (respCode == 404) {
            response += " 404 Not Found";
        }

        //response += msg;
        response += "\r\n"+"\r\n";

        return response;
    }

    @Override
    public void run() {

        try {
            String line=in.readLine();
            System.out.println("Request received:" + line);

            String[] input=line.split(" ");
            String method=input[0];
            String fileName=input[1].replace("/", "");
            System.out.println(fileName);


            switch (method) {

                case "GET": {

                    File file=new File(fileName);

                    // file is there ?
                    if (file.exists()) {

                        BufferedReader br=new BufferedReader(new FileReader(file));
                        out.writeBytes(buildResponse(200, ""));

                        //out.writeUTF(response);
                        String st="";
                        int totalBytes=0;

                        while ((st=br.readLine()) != null) {
                            totalBytes+=st.getBytes().length;
                            //out.writeUTF(st);
                            st=st+"\n";
                            out.writeBytes(st+ "\n");
                        }
                        System.out.println("Sent total bytes : " + totalBytes);
                        //out.flush();
                        br.close();


                    } else { // file is missing
                        out.writeBytes(buildResponse(404, ""));
                    }

                    break;
                }
                case "PUT": {

                    FileOutputStream fos=new FileOutputStream(fileName);

                    String st;
                    System.out.println("Headers:");

                    while (!(st=in.readLine()).isEmpty())
                        System.out.println("" + st);
                    System.out.println("Message Body:");
                    while ((st=in.readLine()) != null) {
                        System.out.println(st+"\n");
                        st=st+"\n";
                        fos.write(st.getBytes());
                    }


                    // close file output stream
                    fos.close();

                    out.writeBytes(buildResponse(200, ""));

                    break;
                }
                default: {
                    out.writeBytes(buildResponse(404, ""));
                }
            }

            // closing resources
            this.in.close();
            this.out.close();
            this.s.close();

    }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class HttpServer {

    public static int port;

    HttpServer(int port) {
        HttpServer.port= port;
    }

    public static void main(String[] args) {
        Thread t =null;
        int portArg = Integer.parseInt(args[0]);
        HttpServer httpserver = new HttpServer(portArg);

        //catching Ctrl+C to terminate the connection gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("\nserver shutting Requested. . . \n");
                try {
                    Thread.sleep((1000));
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        try {

            ServerSocket server = new ServerSocket(port);

            while (true) {

                System.out.println("waiting for connection in port :" + port+"\nPress Ctrl+C to close the server\n");
                Socket socket = server.accept();
                System.out.println("Client's IP address is: "+ socket.getInetAddress().getHostAddress());
                // DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                // DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                RequestHandler req = new RequestHandler(socket,in,out);
                t = new Thread(req);
                t.start();
            }
        }

         catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
