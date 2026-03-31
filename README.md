# RSA Man-in-the-Middle Attack Demonstration

## Overview

This project demonstrates a **Man-in-the-Middle (MitM) attack** on a chat communication system that uses a **weak implementation of the RSA encryption algorithm**. The objective is to illustrate how improper key management and weak cryptographic parameters can allow an attacker to intercept and decrypt supposedly secure communications.

The system simulates two users communicating over a network while an attacker intercepts the key exchange and compromises the encryption process.

---

## Project Architecture

The application consists of three primary components:

### 1. Chat Clients (`ChatClient3` and `ChatClient4`)

Two clients simulate users communicating securely using RSA public-key encryption.

### 2. Attacker Server (`ManInMiddle`)

A malicious intermediary server that intercepts the public key exchange between the two clients.

### 3. RSA Utility Module (`RSAUtils`)

A helper class responsible for generating RSA keys and performing encryption and decryption operations.

---

## Attack Methodology

The attack demonstrates how weak RSA implementations can be compromised through the following steps:

1. **Weak Key Generation**

   * The clients generate RSA keys using **small prime numbers**, making the modulus `n` vulnerable to factorization.

2. **Public Key Interception**

   * During communication setup, the attacker intercepts the public key `(e, n)` transmitted between the clients.

3. **Factorization of RSA Modulus**

   * The attacker applies **Fermat's Factorization Method** to break the modulus `n` into its prime factors `p` and `q`.

4. **Private Key Reconstruction**

   * Using the recovered values of `p` and `q`, the attacker computes:
   * Euler’s Totient: `φ(n)`
   * Private key exponent: `d`

5. **Message Decryption**

   * With the private key derived, the attacker can decrypt intercepted ciphertext messages in real time.

---

## Requirements

* **Java Development Kit (JDK) 8 or later**
* Basic understanding of RSA cryptography and networking

---

## Running the Demonstration

### Step 1: Compile the Source Code

```bash
javac *.java
```

### Step 2: Start the Attacker Server

```bash
java ManInMiddle
```

### Step 3: Launch Client 1

```bash
java ChatClient3
```

### Step 4: Launch Client 2

```bash
java ChatClient4
```

### Step 5: Observe the Attack

Send messages between the two clients.
The **ManInMiddle server console** will display intercepted messages and their decrypted contents.

---

## Key Learning Outcomes

This project highlights important cryptographic security principles:

* The risks of **using small RSA key sizes**
* The importance of **secure public key distribution**
* The necessity of **digital certificates and PKI**
* The role of **secure padding schemes (e.g., OAEP)**
* The vulnerability of cryptographic systems to **Man-in-the-Middle attacks**

---

## Disclaimer

This project is created strictly for **educational and research purposes** to demonstrate cryptographic vulnerabilities and attack techniques.
It should **not be used for malicious activities**.

---

## Author

Chinmayi
B.Tech – Computer Science (Cybersecurity)
Interest Areas: Ethical Hacking, Cryptography, and Security Research
