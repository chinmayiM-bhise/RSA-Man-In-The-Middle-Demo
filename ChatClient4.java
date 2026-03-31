import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

class ChatClient4 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Connected to server");

            Scanner scanner = new Scanner(System.in);

            // Generate weak keys dynamically
            // For demonstration, using a small range for primes
            RSAUtils.RSAKeyPair clientKeyPair = RSAUtils.generateWeakKeyPair(50, 100, 19); // Using 19 for e, similar to original

            System.out.println("Calculated public key locally: (" + clientKeyPair.publicKeyE() + "," + clientKeyPair.modulusN() + ")");
            System.out.println(clientKeyPair.privateKeyD() + " is the private key\n");
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(clientKeyPair.modulusN());
            out.println(clientKeyPair.publicKeyE());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String cStr3 = in.readLine();
            int n1 = Integer.parseInt(cStr3);

            String cStr4 = in.readLine();
            int e1 = Integer.parseInt(cStr4);

            System.out.println("Received public key: (" + e1 + "," + n1 + ")");
            BigInteger client1PublicKey = BigInteger.valueOf(e1);
            BigInteger client1Modulus = BigInteger.valueOf(n1);

            // Receive start chat message from server
            String message = in.readLine();
            System.out.println("SERVER: " + message);

            // Start chat
            while (true) {
                try {
                    String encryptedMessage = in.readLine();
                    String decryptedMessage = RSAUtils.decryptMessage(encryptedMessage, clientKeyPair.privateKeyD(), clientKeyPair.modulusN());
                    System.out.println("CLIENT 1: " + decryptedMessage);

                    System.out.print("ME: ");
                    String kbstr = scanner.nextLine();
                    String encryptedResponse = RSAUtils.encryptMessage(kbstr, client1PublicKey, client1Modulus);
                    out.println(encryptedResponse);
                } catch (IOException e) {
                    System.out.println("Connection lost: " + e.getMessage());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
