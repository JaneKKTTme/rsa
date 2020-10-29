import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

public class Bob {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8080;

    private static boolean isConnected_;
    private static Socket socket_;

    public Bob(){
        isConnected_ = false;
    }

    public static void connectToServer() throws IOException {
        socket_ = new Socket(IP_ADDRESS,  PORT);
        isConnected_ = true;
    }

    public static void closeConnection() {
        if (!isConnected_) {
            return;
        }
        try {
            socket_.close();
        } catch (IOException e) {
            /* cannot happen */
        }
        isConnected_ = false;
    }

    public static void main(String[] args) {
        try {
            // Open connection to Alice
            connectToServer();

            // DataInputStream and DataOutputStream created
            DataInputStream in = new DataInputStream(socket_.getInputStream());
            DataOutputStream out = new DataOutputStream(socket_.getOutputStream());

            // Generate two simple numbers
            String[] simpleOddNumbers = RSA.doGenerationOfSimpleOddRandomNumbers().split(" "); // p & q
            System.out.println("Two simple numbers :\np = " + simpleOddNumbers[0]);
            System.out.println("q = " + simpleOddNumbers[1]);

            // Canculate module of these two simple numbers : multiply two simple numbers
            int module = Integer.parseInt(simpleOddNumbers[0]) * Integer.parseInt(simpleOddNumbers[1]);
            System.out.println("Module of p and q: n = p * q = " + module);

            // Canculate value of Euler function
            int eulerFunction = (Integer.parseInt(simpleOddNumbers[0]) - 1) * (Integer.parseInt(simpleOddNumbers[1]) - 1);
            System.out.println("Euler function : f = (p - 1) * (q - 1) = " + eulerFunction);

            // Pick up random open exponent to value of Euler function
            int exponent = RSA.findOpenExponent(eulerFunction);
            System.out.println("Exponent : e = " + exponent);

            // Send the open key : { e, n } - to Alice
            out.writeUTF(exponent + " " + module);

            /*
             Calculate exponential reverse number on module of Euler function value
             As a result, secret key : { d, n }
             */
            int d = RSA.calculateExponentialReverseNumberOnModule(exponent, eulerFunction, module);
            System.out.println("Exponential reverse number on module Euler function value : d = " + d);

            // Receive message from Alice
            BigInteger messageB = BigInteger.valueOf(Long.parseLong(in.readUTF()));
            System.out.println("Response from Alice : " + messageB);

            // Decode message
            messageB = messageB.modPow(new BigInteger(String.valueOf(d)), new BigInteger(String.valueOf(module)));
            System.out.println("Decoded message : " + messageB);

            // Close connection with Alice
            in.close();
            out.close();
            closeConnection();
        }
        catch (IOException ignored) {
            /* cannot happen */
        }
    }
}