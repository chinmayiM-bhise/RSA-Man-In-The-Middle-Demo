import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

class ChatClient3 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Connected to server");

            Scanner scanner = new Scanner(System.in);

            // Generate weak keys dynamically
            // For demonstration, using a small range for primes
            RSAUtils.RSAKeyPair clientKeyPair = RSAUtils.generateWeakKeyPair(50, 100, 17);

            System.out.println("Calculated public key locally: (" + clientKeyPair.publicKeyE() + "," + clientKeyPair.modulusN() + ")");
            System.out.println(clientKeyPair.privateKeyD() + " is the private key\n");
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(clientKeyPair.modulusN());
            out.println(clientKeyPair.publicKeyE());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String cStr3 = in.readLine();
            int n2 = Integer.parseInt(cStr3);

            String cStr4 = in.readLine();
            int e2 = Integer.parseInt(cStr4);

            System.out.println("Received public key: (" + e2 + "," + n2 + ")");
            BigInteger serverPublicKey = BigInteger.valueOf(e2);
            BigInteger serverModulus = BigInteger.valueOf(n2);

            // Receive start chat message from server
            String message = in.readLine();
            System.out.println("SERVER: " + message);

            while (true) {
                try {
                    System.out.print("ME: ");
                    String kbstr = scanner.nextLine();
                    String encryptedMessage = RSAUtils.encryptMessage(kbstr, serverPublicKey, serverModulus);
                    out.println(encryptedMessage);

                    String encryptedResponse = in.readLine();
                    String decryptedResponse = RSAUtils.decryptMessage(encryptedResponse, clientKeyPair.privateKeyD(), clientKeyPair.modulusN());
                    System.out.println("CLIENT 2: " + decryptedResponse);
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
