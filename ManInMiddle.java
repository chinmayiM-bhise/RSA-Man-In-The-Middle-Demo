import java.net.*;
import java.io.*;
import java.math.BigInteger;

class ManInMiddle {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started. Waiting for clients...");

            // Accept two clients
            Socket client1Socket = serverSocket.accept();
            System.out.println("Client 1 connected");
            Socket client2Socket = serverSocket.accept();
            System.out.println("Client 2 connected");

            // Create input and output streams for both clients
            PrintWriter out1 = new PrintWriter(client1Socket.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
            PrintWriter out2 = new PrintWriter(client2Socket.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));

            // Receive key from client 1
            String cStr = in1.readLine();
            int n1 = Integer.parseInt(cStr);
            System.out.println("Received n1 from client 1: " + n1);

            String cStr2 = in1.readLine();
            int e1 = Integer.parseInt(cStr2);
            System.out.println("Received e1 from client 1: " + e1);

            // Send e/n to client 2
            out2.println(n1);
            out2.println(e1);

            // Breaking of public keys of client 1
            BigInteger n1BigInt = BigInteger.valueOf(n1);
            BigInteger e1BigInt = BigInteger.valueOf(e1);
            BigInteger d1 = calculatePrivateKey(n1BigInt, e1BigInt);
            System.out.println("Calculated private key for client 1: " + d1);

            // Receive key from client 2
            String eStr = in2.readLine();
            int n2 = Integer.parseInt(eStr);
            System.out.println("Received n2 from client 2: " + n2);

            String eStr2 = in2.readLine();
            int e2 = Integer.parseInt(eStr2);
            System.out.println("Received e2 from client 2: " + e2);

            // Send e/n to client 1
            out1.println(n2);
            out1.println(e2);

            // Breaking of public keys of client 2
            BigInteger n2BigInt = BigInteger.valueOf(n2);
            BigInteger e2BigInt = BigInteger.valueOf(e2);
            BigInteger d2 = calculatePrivateKey(n2BigInt, e2BigInt);
            System.out.println("Calculated private key for client 2: " + d2);

            // Start chat between clients
            out1.println("Start chatting!");
            out2.println("Start chatting!");

            // Forward messages between clients
            while (true) {
                try {
                    if (in1.ready()) {
                        String encryptedMessage1 = in1.readLine();
                        String decryptedMessage1 = decryptMessage(encryptedMessage1, d1, n1BigInt);
                        System.out.println("Decrypted message from client 1: " + decryptedMessage1);
                        String encryptedMessageForClient2 = encryptMessage(decryptedMessage1, e2BigInt, n2BigInt);
                        out2.println(encryptedMessageForClient2);
                    }
                    
                    if (in2.ready()) {
                        String encryptedMessage2 = in2.readLine();
                        String decryptedMessage2 = decryptMessage(encryptedMessage2, d2, n2BigInt);
                        System.out.println("Decrypted message from client 2: " + decryptedMessage2);
                        String encryptedMessageForClient1 = encryptMessage(decryptedMessage2, e1BigInt, n1BigInt);
                        out1.println(encryptedMessageForClient1);
                    }
                } catch (IOException e) {
                    System.out.println("Error in forwarding messages: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BigInteger calculatePrivateKey(BigInteger n, BigInteger e) throws Exception {
        BigInteger sqrtN = n.sqrt();
        BigInteger p = BigInteger.ZERO, q = BigInteger.ZERO;
        boolean found = false;
        for (BigInteger a = sqrtN; a.compareTo(n) < 0; a = a.add(BigInteger.ONE)) {
            BigInteger a2 = a.pow(2);
            BigInteger b2 = a2.subtract(n);
            if (b2.signum() < 0) continue; // Ensure b2 is non-negative
            BigInteger b = b2.sqrt();
            if (b.pow(2).equals(b2)) {
                p = a.add(b);
                q = a.subtract(b);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new Exception("Failed to find factors p and q.");
        }

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return e.modInverse(phi);
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
}
