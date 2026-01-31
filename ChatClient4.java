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

            int p2 = 59;
            int q2 = 67;
            int e2 = 19;

            int phi_n2 = (p2 - 1) * (q2 - 1);
            int n2 = p2 * q2;
            System.out.println("Calculated public key locally: (" + e2 + "," + n2 + ")");

            int d2 = 0;
            for (int d = 1; d > 0; d++) {
                if (((e2 * d) % phi_n2) == 1) {
                    d2 = d;
                    System.out.println(d2 + " is the private key\n");
                    break;
                }
            }

            BigInteger keypu = BigInteger.valueOf(e2);
            BigInteger keypr = BigInteger.valueOf(d2);
            BigInteger n = BigInteger.valueOf(n2);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(n2);
            out.println(e2);

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
                    String decryptedMessage = decryptMessage(encryptedMessage, keypr, n);
                    System.out.println("CLIENT 1: " + decryptedMessage);

                    System.out.print("ME: ");
                    String kbstr = scanner.nextLine();
                    String encryptedResponse = encryptMessage(kbstr, client1PublicKey, client1Modulus);
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

    private static String encryptMessage(String message, BigInteger e, BigInteger n) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            int charValue = (int) message.charAt(i);
            BigInteger ch = BigInteger.valueOf(charValue);
            BigInteger encryptedCh = ch.modPow(e, n);
            encryptedMessage.append(encryptedCh).append(" ");
        }
        return encryptedMessage.toString().trim();
    }

    private static String decryptMessage(String encryptedMessage, BigInteger d, BigInteger n) {
        StringBuilder decryptedMessage = new StringBuilder();
        String[] encryptedValues = encryptedMessage.split(" ");
        for (String cipher : encryptedValues) {
            BigInteger encryptedCh = new BigInteger(cipher);
            BigInteger decryptedCh = encryptedCh.modPow(d, n);
            decryptedMessage.append((char) decryptedCh.intValue());
        }
        return decryptedMessage.toString();
    }
}
