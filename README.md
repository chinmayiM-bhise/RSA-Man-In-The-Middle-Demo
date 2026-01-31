🔐 RSA Man-in-the-Middle Attack

A practical demonstration of how a Man-in-the-Middle (MitM) attack can break communication secured by a weak RSA implementation.
This project showcases key interception, modulus factorization, private key reconstruction, and live message decryption — all in real time.

✨ Features

🔑 RSA Key Generation (Weak Primes)

📡 Intercepted Public Key Exchange

🧮 Fermat’s Factorization Attack

🔓 Private Key Recovery From (e, n)

🗨️ Live Decryption of Messages Between Clients

🖥️ Terminal-based Chat Simulation (Alice ↔ Bob)


🔍 Technical Flow of the Attack
1. Vulnerable RSA Key Generation

The clients generate small prime numbers for RSA.
Because n = p × q is small, factorization is computationally easy.

2. Public Key Interception

When Alice sends Bob her public key, the attacker captures:

    PublicKey_A = (eA, nA)

3. Fermat’s Factorization Method

The attacker factors nA by exploiting the fact that p and q are close.

    n = p × q
    n = a² − b²
    a = ceil(√n)

4. Private Key Reconstruction

Once p and q are recovered:

    φ(n) = (p−1)(q−1)
    d = e⁻¹ mod φ(n)

5. Message Decryption

All intercepted ciphertexts:

    C = M^e mod n


are decrypted to plaintext:

    M = C^d mod n

🧪 Running the Demo
Prerequisites

Java JDK 8 or above

Compile the source
javac *.java

Start the MITM server
java ManInMiddle

Start Client 1 (Alice)
java ChatClient3

Start Client 2 (Bob)
java ChatClient4

Interact

Type messages from both clients

Observe decrypted messages in the MITM console

Watch the entire RSA exploit process happen in live view

🎓 What You Learn From This Project
🔸 Why RSA with small primes is dangerous

Small moduli can be factored in seconds.

🔸 Why public key authentication matters

Without proper certificates, MITM becomes trivial.

🔸 Why RSA must be padded

Raw RSA is deterministic and insecure without OAEP.

🔸 How MITM attacks break naive key exchanges

Trusting unauthenticated public keys = disaster.

⚠️ Disclaimer

This project is strictly for educational and research purposes.
Do not use this code for any unauthorized or malicious activity.
The goal is to understand security — not violate it.

👩‍💻 Author

Chinmayi M Bhise
B.Tech CSE (Cybersecurity) | Security Researcher | Offensive Security Learner
Passionate about cryptography, vulnerability research, and secure system design.
