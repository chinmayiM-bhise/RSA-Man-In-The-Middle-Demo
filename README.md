# RSA Man-in-the-Middle Attack Demo

This project demonstrates a **Man-in-the-Middle (MitM) attack** on a chat application secured by a weakened implementation of the **RSA encryption algorithm**.

## Project Overview

The application consists of three main components:
1.  **Chat Clients (`ChatClient`):** Two clients (Alice and Bob) that attempt to communicate securely using RSA public-key encryption.
2.  **Attacker Server (`ManInMiddle`):** A server that sits between the clients, intercepting their public key exchange.

## How the Attack Works

1.  **Weak Key Generation:** The clients use small prime numbers to generate their RSA keys.
2.  **Key Interception:** When a client sends its Public Key `(e, n)` to the other party, the `ManInMiddle` server intercepts it.
3.  **Factorization Attack:** The attacker uses **Fermat's Factorization Method** to factor the modulus `n` back into its primes `p` and `q`.
4.  **Private Key Derivation:** With `p` and `q` recovered, the attacker calculates the Private Key `d`.
5.  **Decryption:** The attacker can now decrypt any message sent with the intercepted Public Key.

## Usage

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Running the Demo

1.  **Compile the code:**
    ```bash
    javac *.java
    ```

2.  **Start the Attacker Server:**
    ```bash
    java ManInMiddle
    ```

3.  **Start Client 1:**
    ```bash
    java ChatClient3
    ```

4.  **Start Client 2:**
    ```bash
    java ChatClient4
    ```

5.  **Chat:** Type messages in the client windows. Watch the `ManInMiddle` console to see the messages being decrypted in real-time.

## Educational Purpose

**WARNING:** This code is for **educational purposes only**. It demonstrates why:
- Using small key sizes in RSA is insecure.
- Authenticating public keys (e.g., via Certificates) is crucial to prevent MitM attacks.
- Padding schemes (like OAEP) are necessary for RSA security.

## Author

[Your Name]
