import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Alice {
    private static ServerSocket serverSocket_;
    private static Socket socket_;
    private static final Integer port_ = 8080;

    public static void closeConnection() {
        try {
            serverSocket_.close();
        } catch (IOException e) { /* cannot happen */ }
    }

    public static void start() {
        try {
            socket_ = serverSocket_.accept();
        } catch (IOException e) { /* cannot happen */ }
    }

    public static void main(String[] args) throws Exception {
        // Server started ...
        serverSocket_ = new ServerSocket(port_);

        // Server connected ...
        start();

        try {
            // DataInputStream and DataOutputStream created
            DataInputStream in = new DataInputStream(socket_.getInputStream());
            DataOutputStream out = new DataOutputStream(socket_.getOutputStream());

            // Receive open key from Bob
            String[] openKey = in.readUTF().split(" ");
            System.out.println("OPEN KEY : { " + openKey[0] + ", " + openKey[1] + " }");

            // Enter message
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter message :");
            BigInteger messageB = BigInteger.valueOf(Long.parseLong(sc.nextLine()));

            // Encrypt message by open key and send it to Bob
            messageB = messageB.modPow(BigInteger.valueOf(Long.parseLong(openKey[0])), BigInteger.valueOf(Long.parseLong(openKey[1])));
            out.writeUTF(String.valueOf(messageB.intValue()));
            System.out.println("Send encoded message : " + messageB);

            // Close connection with Bob
            in.close();
            out.close();
            closeConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
