🔐 RSA Man-in-the-Middle Attack Demo

This project demonstrates how a Man-in-the-Middle (MitM) attack can successfully break a chat system that uses a weakened implementation of RSA encryption.
It highlights why modern cryptographic best practices are essential for secure communication.

🚀 Project Overview

The system consists of three core components:

1. Chat Clients (Alice & Bob)

Implemented in Java (ChatClient3.java and ChatClient4.java)

Exchange messages encrypted using RSA public keys

Communicate assuming a secure channel

2. Man-in-the-Middle Attacker (ManInMiddle.java)

Intercepts public keys exchanged between clients

Performs RSA modulus factorization

Decrypts intercepted messages in real time

3. Weak RSA Implementation

Uses small prime numbers, making RSA vulnerable

Enables the attacker to factor the modulus n efficiently

🛑 How the Attack Works
Step-by-step Breakdown

Weak RSA Key Generation
Clients generate RSA keys using small primes — making factorization trivial.

Interception of Public Keys
The attacker sits between the clients during key exchange and captures (e, n).

Fermat’s Factorization Attack
Using Fermat’s method, the attacker computes:

n = p × q


and recovers p and q.

Private Key Reconstruction
With p and q, the attacker calculates:

φ(n) = (p−1)(q−1)
d = e^{-1} mod φ(n)


Message Decryption
The attacker now has the private key and decrypts all messages between the clients.

🧪 Running the Demo
Prerequisites

Java Development Kit (JDK 8+)

1️⃣ Compile the project
javac *.java

2️⃣ Start the Man-In-The-Middle server
java ManInMiddle

3️⃣ Start Client 1
java ChatClient3

4️⃣ Start Client 2
java ChatClient4

5️⃣ Observe the attack

Chat normally between clients

Watch the attacker's terminal to see:

intercepted keys

factorization

decrypted messages

🎓 Educational Purpose

This project highlights why modern RSA implementations must include:

✔ Large prime numbers (2048+ bits)

Prevents feasible factorization.

✔ Authentic public key verification

Digital certificates prevent MitM interception.

✔ Secure padding schemes (OAEP)

Mitigates deterministic RSA vulnerabilities.

⚠️ Disclaimer

This project is strictly for:

Educational use

Cryptography learning

Security demonstrations

Do NOT use this code for any malicious activities.

👩‍💻 Author

Chinmayi M Bhise
B.Tech CSE (Cybersecurity) | Security Researcher | Developer
