
package com.pracbiz.b2bportal.base.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public final class EncodeUtil
{
    private final static Log LOG = LogFactory.getLog(EncodeUtil.class);
    private static byte[] xorKey = {123, 51, 17, 31, 63, 23, 117, 83};
    
    private static EncodeUtil instance;
    
    private EncodeUtil(){}
    
    public static EncodeUtil getInstance()
    {
        synchronized(EncodeUtil.class)
        {
            if (instance == null)
            {
                instance = new EncodeUtil();
            }
        }
        
        return instance;
    }

    
    public String encodePassword(String password, String algorithm) throws UnsupportedEncodingException
    {
        byte[] unencodedPassword = password.getBytes(CommonConstants.ENCODING_UTF8);

        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance(algorithm);
        }
        catch (Exception e)
        {
            LOG.error("EncodeUtil ", e.getCause());

            return password;
        }

        md.reset();
        md.update(unencodedPassword);

        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < encodedPassword.length; i++)
        {
            if ((encodedPassword[i] & 0xff) < 0x10)
            {
                buf.append('0');
            }

            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
        }

        return buf.toString();
    }
    

    public String encodeString(String str) throws UnsupportedEncodingException
    {
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encodeBuffer(str.getBytes(CommonConstants.ENCODING_UTF8)).trim();
    }
    

    public String decodeString(String str) throws IOException
    {
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        
        return new String(dec.decodeBuffer(str), CommonConstants.ENCODING_UTF8);
    }
    
    
    public String encodeStr(String inStr) throws UnsupportedEncodingException
    {
        byte[] inStrBytes = inStr.getBytes(CommonConstants.ENCODING_UTF8);

        int keyLength = xorKey.length;
        for (int i = 0; i < inStrBytes.length; i++)
        {
            int j = i % keyLength;
            inStrBytes[i] = (byte) (inStrBytes[i] ^ xorKey[j]);
        }

        StringBuffer result = new StringBuffer();
        String str = Base64.encode(inStrBytes);
        String magicStr = "/01234ZYXWVUTSRQPONMLKJIHGFEDCBA98765abcdefghijklmnopqrstuvwxyz";
        int magicLen = magicStr.length();
        int strLen = str.length();
        char[] charArr = str.toCharArray();
        int i;
        for (i = 0; i < (strLen / 4) - 1; i++)
        {
            result.append(str.substring(i * 4, i * 4 + 4)
                    + magicStr.charAt(((charArr[i * 4] - '/') + 31) % magicLen)
                    + magicStr.charAt(((charArr[i * 4 + 1] - '/') + 7)
                            % magicLen)
                    + magicStr.charAt(((charArr[i * 4 + 2] - '/') + 23)
                            % magicLen)
                    + magicStr.charAt(((charArr[i * 4 + 3] - '/') + 11)
                            % magicLen));
        }
        return (result.toString() + str.substring(i * 4, i * 4 + 4));
    }

     
    public String decodeStr(String inStr) throws UnsupportedEncodingException
    {
        StringBuffer result = new StringBuffer();
        int strLen = inStr.length();
        for (int i = 0; i < (strLen / 4); i = i + 2)
        {
            result.append(inStr.substring(i * 4, i * 4 + 4));
        }

        byte[] decodeBytes = Base64.decode(result.toString());

        int keyLength = xorKey.length;
        for (int i = 0; i < decodeBytes.length; i++)
        {
            int j = i % keyLength;
            decodeBytes[i] = (byte) (decodeBytes[i] ^ xorKey[j]);
        }

        return new String(decodeBytes, CommonConstants.ENCODING_UTF8);
    }


    public String computeSha2Digest(byte[] plainTextBytes)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        return encodePassword(new String(plainTextBytes, CommonConstants.ENCODING_UTF8), "sha-512");

    }
     

    public String computeSha2Digest(String inStr)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        return encodePassword(inStr, "sha-512");

    }
    
    
    public String computePwd(String pwd, BigDecimal userOid) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        return this.computeSha2Digest("password(" + pwd + userOid + ")");
    }
     
    
    public byte[] generateSecureBytes(int length)
    {
        SecureRandom sr = new SecureRandom();
        byte[] nonceBytes = new byte[length];
        sr.nextBytes(nonceBytes);
        return nonceBytes;
    }
    
     
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        // Testing Code...
        
        /*String source = "password(password1)";
        
        System.out.println(EncodeUtil.getInstance().encodeString(source));
        System.out.println(EncodeUtil.getInstance().encodePassword(source, "SHA-512"));
        
        System.out.println(EncodeUtil.getInstance().computeSha2Digest(source));
        System.out.println(EncodeUtil.getInstance().computeSha2Digest(source.getBytes()));*/
    }
}
