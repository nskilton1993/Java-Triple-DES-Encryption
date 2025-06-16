import java.io.*;            // Based on an example in "Java I/O" by
import java.security.*;      // Elliotte Harold, O'Reilly, page 244.
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

// Sample usage: C:\> java JavaFileEncryptionDESede roses.plain 
// A234567890b234567890c234.  The encrypted file will be in roses.plain.desede.
// Triple DES uses the first 8 bytes of the key to encrypt (56 bits), then the next eight
// bytes as a key 56 (bits) to decrypt, and finally the last 8 bytes (56 bits) as a key to
// encrypt.  The result is roughly equivalent to 168 bit encryption.

public class Encrypt_DESede {

	public static void main(String[ ] args) {
		if (args.length != 2) {
			System.err.println("Usage: java Encrypt_DESede filename 24CharPassword");
			return;
		}
		
		String password = args[1];
		if (password.length() != 24 )
			System.err.println("Encryption password must be 24 bytes.");
		try {
			FileInputStream fin = new FileInputStream(args[0]);
			FileOutputStream fout = new FileOutputStream( args[0] + ".desede");
			
			// First create a key specification from the password, then the secret key.
			byte[ ] desedeKeyData = password.getBytes( );
			DESedeKeySpec desKeySpec = new DESedeKeySpec(desedeKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey desedeKey = keyFactory.generateSecret(desKeySpec); //***
			
			// DES algorithm in CBC mode with PKCS5PPadding and random initialization vector.
			Cipher desede = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			
			// Create initialization vector.
			desede.init(Cipher.ENCRYPT_MODE, desedeKey); 
			
			// Write the length & initialization vector onto the output stream, as plain text.
			byte[ ] iv = desede.getIV( );
			DataOutputStream dout = new DataOutputStream(fout);
			dout.writeInt(iv.length);  // Length of initialization vector, in plain text.
			dout.write(iv);            // Actual initialization vector, in plain text.
			
			//Encrypt 64 byte blocks.  DESede uses 8 bytes (56 bits) at a time.
			byte[ ] input = new byte[64]; 
			while (true) {  //The buffer does not have to be a multiple of 8.
				int bytesRead = fin.read(input);  // Get 64 bytes if possible from plain text.
				if (bytesRead == -1) break;         // Check EOF.
				byte[ ] output = desede.update(input, 0, bytesRead); 
				if (output != null) dout.write(output);  //Write encrypted info to file.
			}
			
			byte[ ] output = desede.doFinal( );  // Pad and flush any remaining data.
			if (output != null) dout.write(output);  // Write remaining encrypted data to file.
			fin.close( );
			dout.flush( );
			dout.close( );	
		}
		catch (InvalidKeySpecException e) {
			System.err.println(e);
		}
		catch (InvalidKeyException e) {
			System.err.println(e);
		}
		catch (NoSuchAlgorithmException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) {
			System.err.println(e);
		}
		catch (BadPaddingException e) {
			System.err.println(e);
		}
		catch (IllegalBlockSizeException e) {
			System.err.println(e);
		}
		catch (IOException e) {
			System.err.println(e);
		}
	}
}
