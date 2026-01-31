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

            int p1 = 61;
            int q1 = 53;
            int e1 = 17;

            int phi_n1 = (p1 - 1) * (q1 - 1);
            int n1 = p1 * q1;
            System.out.println("Calculated public key locally: (" + e1 + "," + n1 + ")");

            int d1 = 0;
            for (int d = 1; d > 0; d++) {
                if (((e1 * d) % phi_n1) == 1) {
                    d1 = d;
                    System.out.println(d1 + " is the private key\n");
                    break;
                }
            }

            BigInteger keypu = BigInteger.valueOf(e1);
            BigInteger keypr = BigInteger.valueOf(d1);
            BigInteger n = BigInteger.valueOf(n1);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(n1);
            out.println(e1);

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

            // Start chat
            while (true) {
                try {
                    System.out.print("ME: ");
                    String kbstr = scanner.nextLine();
                    String encryptedMessage = encryptMessage(kbstr, serverPublicKey, serverModulus);
                    out.println(encryptedMessage);

                    String encryptedResponse = in.readLine();
                    String decryptedResponse = decryptMessage(encryptedResponse, keypr, n);
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
