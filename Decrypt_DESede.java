import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Decrypt_DESede {

	public static void main(String[ ] args) {
		
		if (args.length != 3) {
			System.err.println("Usage: java Decrypt_DESede infile outfile 24CharPassword");  return;
		}
		
		String infile = args[0];
		String outfile = args[1];
		String password = args[2];
		
		if (password.length() != 24 ) 
			System.err.println("Password must be 24 characters long");
		try {
			FileInputStream fin = new FileInputStream(infile);
			FileOutputStream fout = new FileOutputStream(outfile);
			
			// First create a key specification from the password, then the key.
			// Note DESede expects 3 keys of 8 bytes each, a total of 24 bytes.
			byte[ ] desedeKeyData = password.getBytes( );
			DESedeKeySpec desedeKeySpec =
					new DESedeKeySpec(desedeKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey desedeKey = keyFactory.generateSecret(desedeKeySpec);  //****
			
			// Read the initialization vector
			DataInputStream din = new DataInputStream(fin);
			int ivSize = din.readInt( );  // Retrieve initialization vector.
			byte[ ] iv = new byte[ivSize];
			din.readFully(iv);
			IvParameterSpec ivps = new IvParameterSpec(iv);
			
			// Use Triple DES (DESede) - Data Encryption Standard
			Cipher desede = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			desede.init(Cipher.DECRYPT_MODE, desedeKey, ivps);
			
			byte[ ] input = new byte[64];
			while (true) {
				int bytesRead = fin.read(input);
				if (bytesRead == -1) break;
				byte[ ] output = desede.update(input, 0, bytesRead);
				if (output != null) fout.write(output);
			}
			
			byte[ ] output = desede.doFinal( );
			if (output != null) fout.write(output);
			fin.close( );
			fout.flush( );
			fout.close( );	
		}
		catch (InvalidKeySpecException e) {
			System.err.println(e);
		}
		catch (InvalidKeyException e) {
			System.err.println(e);
		}
		catch (InvalidAlgorithmParameterException e) {
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
