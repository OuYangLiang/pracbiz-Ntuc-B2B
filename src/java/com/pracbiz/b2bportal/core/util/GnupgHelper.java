//*****************************************************************************
//
// File Name       :  GnupgHelper.java
// Date Created    :  Mar 18, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Oct 8, 2012 8:58:45 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.pracbiz.b2bportal.core.exception.GnupgException;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public interface GnupgHelper
{
    public static String KEYALGO_RSA = "RSA";
    public static int KEYSIZE_4096 = 4096;
    
    public static String ENCRYPTED_FILE_SUFFIX = ".pgp";
    public static String DECRYPTED_FILE_SUFFIX = ".clear";

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CREATE_DATE = "createDate";
    public static final String KEY_EXPIRY_DATE = "expiryDate";
    public static final String KEY_FINGERPRINT = "fingerprint";

    /**
     * Generate a standard key pair.
     * 
     * @author ouyang
     * @param userId - User ID of PGP key <br>
     * @param passphrase - Passphrase of PGP key <br>
     * @param expiryDate - Expiry Date of PGP key, null means key does not expire <br>
     * @throws GnupgException - Throw GnupgException if generating failed.
     * @throws UnsupportedEncodingException 
     */
    public void generateKey(String userId, String passphrase, String expiryDate)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    /**
     * Remove the key from the server
     * 
     * @author ouyang
     * @param userId - User ID of PGP key <br>
     * @throws GnupgException - Throw GnupgException if generating failed.
     * @throws UnsupportedEncodingException 
     */
    public void removeKeyById(String userId) throws GnupgException, UnsupportedEncodingException;

    
    
    /**
     * Check the key whether exist in PGP server
     * 
     * @author ouyang
     * @param keyId - ID of PGP key, it may User ID, Comment or Email address <br>
     * @return true if exist in server, false if not exist in server.
     */
    public boolean isKeyExistInServer(String keyId);

    
    
    /**
     * Extract key detail from PGP server.
     * 
     * @author ouyang
     * @param keyId - ID of PGP key, it may User ID, Comment or Email address
     * @return Map - it has 4 keys <br>
     *         GnupgHelper.KEY_USER_ID - User ID of PGP key. <br>
     *         GnupgHelper.KEY_COMMENT - Comment of PGP key. <br>
     *         GnupgHelper.KEY_EMAIL - Email Address of PGP key. <br>
     *         GnupgHelper.KEY_EXPIRY_DATE - Expiry Date of PGP key. <br>
     *         GnupgHelper.KEY_FINGERPRINT - Fingerprint of PGP key. <br>
     * @throws GnupgException - Throw GnupgException if the input key not in server.
     * @throws UnsupportedEncodingException 
     */
    public Map<String, String> extractKeyDetailFromServer(String keyId)
        throws GnupgException, UnsupportedEncodingException;

    
    
    /**
     * Import external PGP key to PGP server.
     * 
     * @author ouyang
     * @param keyFileName - file name of PGP key.
     * @throws GnupgException - Throw GnupgException if failed importing.
     * @throws UnsupportedEncodingException 
     */
    public void importKey(String keyFileName) throws GnupgException, UnsupportedEncodingException;

    
    
    /**
     * Export PGP key to a external file.
     * 
     * @author OuYangLiang
     * @param outFileName - output file name to store the exported key.
     * @param keyId - Key which to be exported.
     * @throws GnupgException - Throw GnupgException if failed exporting.
     * @throws UnsupportedEncodingException 
     */
    public void exportKey(String outFileName, String keyId) throws GnupgException, UnsupportedEncodingException;

    
    
    /**
     * Encrypt and sign file.
     * 
     * @author ouyang
     * @param signerKeyId - key ID used to sign, it is local key. <br>
     * @param recipientKeyID - Key ID used to encrypt, it is recipient key. <br>
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param inputFile - Clear file to be encrypted. <br>
     * @param outputFile - Target encrypted file. <br>
     * @throws GnupgException - Throw GnupgException if failed encrypting.
     * @throws UnsupportedEncodingException 
     */
    public void encryptAndSign(String signerKeyId, String recipientKeyId,
        String passphrase, File inputFile, File outputFile)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    /**
     * Encrypt and sign file.
     * 
     * @author ouyang
     * @param signerKeyId - key ID used to sign, it is local key. <br>
     * @param recipientKeyID - Key ID used to encrypt, it is recipient key. <br>
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param inputFile - Clear file to be encrypted. <br>
     * @return byte[] - Target encrypted byte stream. <br>
     * @throws GnupgException - Throw GnupgException if failed encrypting.
     */
    public byte[] encryptAndSign(String signerKeyId, String recipientKeyId,
        String passphrase, File inputFile)
        throws GnupgException;
    
    
    
    /**
     * Encrypt and sign byte[].
     * 
     * @author ouyang
     * @param signerKeyId - key ID used to sign, it is local key. <br>
     * @param recipientKeyID - Key ID used to encrypt, it is recipient key. <br>
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param input - Clear byte[] to be encrypted. <br>
     * @return byte[] - Target encrypted byte stream. <br>
     * @throws GnupgException - Throw GnupgException if failed encrypting.
     * @throws UnsupportedEncodingException 
     */
    public byte[] encryptAndSign(String signerKeyId, String recipientKeyId,
        String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    /**
     * Encrypt and sign string.
     * 
     * @author ouyang
     * @param signerKeyId - key ID used to sign, it is local key. <br>
     * @param recipientKeyID - Key ID used to encrypt, it is recipient key. <br>
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param inputStr - Clear string to be encrypted. <br>
     * @return String - Encrypted string. <br>
     * @throws GnupgException - Throw GnupgException if failed encrypting.
     * @throws UnsupportedEncodingException 
     */
    public String encryptAndSignString(String signerKeyId,
        String recipientKeyId, String passphrase, String inputStr)
        throws GnupgException, UnsupportedEncodingException;
    
   
    
    /**
     * Encrypt and sign byte.
     * 
     * @author ouyang
     * @param signerKeyId - key ID used to sign, it is local key. <br>
     * @param recipientKeyID - Key ID used to encrypt, it is recipient key. <br>
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param input - Clear byte to be encrypted. <br>
     * @return String - Encrypted string. <br>
     * @throws GnupgException - Throw GnupgException if failed encrypting.
     * @throws UnsupportedEncodingException 
     */
    public String encryptAndSignByte(String signerKeyId,
        String recipientKeyId, String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException;
    
    

    
    
    /**
     * Decrypt file.
     * 
     * @author ouyang
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param inputFile - Encrypted file to be clear. <br>
     * @param outputFile - Target decrypted file. <br>
     * @throws GnupgException - Throw GnupgException if failed decrypting.
     * @throws UnsupportedEncodingException 
     */
    public void decrypt(String passphrase, File inputFile, File outputFile)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    
    
    
    /**
     * Decrypt string.
     * 
     * @author ouyang
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param inputStr - Encrypted string to be clear. <br>
     * @return String - Decrypted string. <br>
     * @throws GnupgException - Throw GnupgException if failed decrypting.
     * @throws UnsupportedEncodingException 
     */
    public String decryptString(String passphrase, String inputStr)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    /**
     * Decrypt byte[].
     * 
     * @author ouyang
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param input - Encrypted byte to be clear. <br>
     * @return String - Decrypted string. <br>
     * @throws GnupgException - Throw GnupgException if failed decrypting.
     * @throws UnsupportedEncodingException 
     */
    public String decryptByte(String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    
    /**
     * Decrypt file.
     * 
     * @author ouyang
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param inputFile - Encrypted file to be clear. <br>
     * @return byte[] - Target decrypted byte stream. <br>
     * @throws GnupgException - Throw GnupgException if failed decrypting.
     */
    public byte[] decrypt(String passphrase, File inputFile)
        throws GnupgException;
    
    
    
    /**
     * Decrypt byte[].
     * 
     * @author ouyang
     * @param passphrase - Passphrase used to sign, it is of local key. <br>
     * @param input - Encrypted byte[] to be clear. <br>
     * @return byte[] - Target decrypted byte stream. <br>
     * @throws GnupgException - Throw GnupgException if failed decrypting.
     * @throws UnsupportedEncodingException 
     */
    public byte[] decrypt(String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    /**
     * Send the key to the keyserver.
     * 
     * @author ouyang
     * @param userId - User ID of PGP key <br>
     * @param keyserver - url of keyserver <br>
     * @throws GnupgException - Throw GnupgException if failed to send the key.
     * @throws UnsupportedEncodingException 
     */
    public void sendKeyToKeyserver(String userId, String keyserver)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    
    /**
     * Retrieve and download key from keyserver.
     * 
     * @author ouyang
     * @param userId - User ID of PGP key <br>
     * @param keyserver - url of keyserver <br>
     * @throws GnupgException - Throw GnupgException if failed to retrieve the key.
     * @throws UnsupportedEncodingException 
     */
    public void retrieveKeyFromKeyserver(String userId, String keyserver)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    
    /**
     * Download key from keyserver.
     * 
     * @author ouyang
     * @param fingerprint - Fingerprint of PGP key <br>
     * @param keyserver - url of keyserver <br>
     * @throws GnupgException - Throw GnupgException if failed to download the key.
     * @throws UnsupportedEncodingException 
     */
    public void downloadKeyFromKeyserver(String fingerprint, String keyserver)
        throws GnupgException, UnsupportedEncodingException;
    
    
    
    /**
     * Revoke a key.
     *
     * @author ouyang
     * @param userId - user id of the PGP key <br>
     * @param passphrase - Passphrase of PGP key <br>
     * @throws GnupgException - Throw GnupgException if failed to revoke the key.
     * @throws UnsupportedEncodingException 
     */
    public void revokeKey(String userId, String passphrase)
        throws GnupgException, UnsupportedEncodingException;
}
