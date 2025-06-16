# Triple DES (DESede) Encryption & Decryption in Java

This project demonstrates symmetric encryption and decryption using the **Triple DES (3DES / DESede)** algorithm in Java. The program encrypts a plaintext string using a DESede key and then decrypts it to validate correctness.

---

## 🔐 About Triple DES (DESede)

Triple DES, also known as **DESede**, is a symmetric block cipher that applies the DES algorithm three times to each data block. It was designed to increase security over standard DES (56-bit), offering **112-bit or 168-bit key options**.

Though now considered deprecated for modern use due to known vulnerabilities and performance concerns, 3DES remains a valuable learning tool for understanding the evolution of encryption standards.

---

## 📁 File Included

- `Encrypt_Decrypt_TripleDES.java`  
  Java class for both encrypting and decrypting a string using Triple DES.

---

## 🛠️ How It Works

1. A plaintext string is specified for encryption.
2. A DESede key is defined or generated.
3. The plaintext is encrypted using `Cipher` initialized with `DESede`.
4. The ciphertext is Base64-encoded for readability.
5. The same key is used to decrypt the ciphertext back to the original string.

---

## ▶️ How to Run

```bash
javac Encrypt_Decrypt_TripleDES.java
java Encrypt_Decrypt_TripleDES

java Encrypt_DESede filename 24CharPassword
java Decrypt_DESede infile outfile 24CharPassword
