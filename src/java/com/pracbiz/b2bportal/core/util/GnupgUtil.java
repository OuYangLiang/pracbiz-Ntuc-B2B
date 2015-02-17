//*****************************************************************************
//
// File Name       :  GnupgUtil.java
// Date Created    :  Oct 8, 2012
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.exception.GnupgException;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public final class GnupgUtil implements GnupgHelper
{
    private static final Logger log = LoggerFactory.getLogger(GnupgUtil.class);
    
    private static String GNUPG_CMD = "gpg";
    private static String TRUST_ALWAYS = "always";
    private static String LINE_SEPARATOR = System.getProperty("line.separator");
    private static String FILE_SEPARATOR = System.getProperty("file.separator");
    
    private static final String GNUPG_PARAM_GENKEY = "--gen-key";
    private static final String GNUPG_PARAM_DELKEY = "--delete-secret-and-public-key";
    private static final String GNUPG_PARAM_IMPORT = "--import";
    private static final String GNUPG_PARAM_EXPORT = "--export";
    private static final String GNUPG_PARAM_EDITKEY = "--edit-key";
    private static final String GNUPG_PARAM_RECVKEYS = "--recv-keys";
    private static final String GNUPG_PARAM_SEARCHKEYS = "--search-keys";
    private static final String GNUPG_PARAM_SENDKEYS = "--send-keys";
    private static final String GNUPG_PARAM_KEYSERVER = "--keyserver";
    private static final String GNUPG_PARAM_DECRYPT = "--decrypt";
    private static final String GNUPG_PARAM_STATUSFD = "--status-fd";
    private static final String GNUPG_PARAM_ENCRYPT = "--encrypt";
    private static final String GNUPG_PARAM_SIGN = "--sign";
    private static final String GNUPG_PARAM_TRUSTMODEL = "--trust-model";
    private static final String GNUPG_PARAM_LOCALUSER = "--local-user";
    private static final String GNUPG_PARAM_RECIPIENT = "--recipient";
    private static final String GNUPG_PARAM_OUTPUT = "--output";
    private static final String GNUPG_PARAM_ARMOR = "--armor";
    private static final String GNUPG_PARAM_FINGERPRINT = "--fingerprint";
    private static final String GNUPG_PARAM_LIST_KEY = "--list-key";
    private static final String GNUPG_PARAM_COMMANDFD = "--command-fd";
    private static final String GNUPG_PARAM_PASSPHRASE = "--passphrase";
    private static final String GNUPG_PARAM_NOTTY = "--no-tty";
    private static final String GNUPG_PARAM_BATCH = "--batch";
    private static final String GNUPG_PARAM_HOMEDIR = "--homedir";
    
    private String commandPath;
    private String homedir;
    private String defaultKeyServer;
    private Boolean useKeyServer;
    
    /*public static void main(String[] args) throws GnupgException
    {
        GnupgUtil util = new GnupgUtil();
        util.setCommandPath("/usr/local/bin");
        util.setHomedir("/Users/ouyang/.gnupg");
        
        System.out.println(util.isKeyExistInServer("S_52261_S@pracbiz.com"));
    }*/
    
    
    //*****************************************************
    // public methods
    //*****************************************************
    
    @Override
    public void generateKey(String userId, String passphrase, String expiryDate)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_GENKEY, GNUPG_PARAM_BATCH};
        StringBuilder sb = new StringBuilder();
        sb.append("%echo Generating a standard keypair ....").append(
            LINE_SEPARATOR);
        sb.append("Key-Type: ").append(KEYALGO_RSA).append(LINE_SEPARATOR);
        sb.append("Key-Length: ").append(KEYSIZE_4096).append(LINE_SEPARATOR);
        sb.append("Subkey-Type: ").append(KEYALGO_RSA).append(LINE_SEPARATOR);
        sb.append("Subkey-Length: ").append(KEYSIZE_4096)
            .append(LINE_SEPARATOR);
        sb.append("Name-Real: ").append(userId).append(LINE_SEPARATOR);
        sb.append("Name-Email: ").append(userId).append(LINE_SEPARATOR);
        if(!StringUtils.isEmpty(expiryDate))
        {
            sb.append("Expire-Date: ").append(expiryDate)
                .append(LINE_SEPARATOR);
        }
        sb.append("Passphrase: ").append(passphrase).append(LINE_SEPARATOR);
        sb.append("%commit").append(LINE_SEPARATOR);
        sb.append("%echo done").append(LINE_SEPARATOR);

        runGnuPG(cmdArgs, sb.toString());
    }

    
    @Override
    public void removeKeyById(String userId) throws GnupgException, UnsupportedEncodingException
    {
        String fingerprint = extractKeyDetailFromServer(userId).get(KEY_FINGERPRINT);
        
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_DELKEY, fingerprint};
        
        runGnuPG(cmdArgs, null);
    }
    
    
    @Override
    public boolean isKeyExistInServer(String keyId)
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_LIST_KEY, keyId};
        try
        {
            runGnuPG(cmdArgs, null);
            return true;
        }
        catch(Exception exception)
        {
            return false;
        }
    }

    
    @Override
    public Map<String, String> extractKeyDetailFromServer(String keyId)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_LIST_KEY, GNUPG_PARAM_FINGERPRINT, keyId};
        String result = runGnuPG(cmdArgs, null);
        
        log.info("extractKeyDetailFromServer : " + result);

        String[] details = result.split(LINE_SEPARATOR);

        int length = details.length;

        String pub = null;
        String fin = null;
        String uid = details[length - 2];

        if(uid.startsWith("uid"))
        {
            pub = details[length - 4];
            fin = details[length - 3];
        }
        else
        {
            uid = details[length - 1];
            pub = details[length - 3];
            fin = details[length - 2];
        }
        log.info("uid of key server is " + uid + " for " + keyId);
        log.info("pub of key server is " + pub + " for " + keyId);

        String expireDate = extractExpireDate(pub);
        String fingerprint = extractFingerprint(fin);

        String key = uid.substring(3).trim();
        String[] parsedKeys = extractKeyId(key);
        String userId = parsedKeys[0];
        String comment = parsedKeys[1];
        String email = parsedKeys[2];

        if(log.isInfoEnabled())
        {
            log.info("UserID : " + userId);
            log.info("comment : " + comment);
            log.info("Email : " + email);
            log.info("Expire Date : " + expireDate);
        }

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put(KEY_USER_ID, userId);
        resultMap.put(KEY_COMMENT, comment);
        resultMap.put(KEY_EMAIL, email);
        resultMap.put(KEY_EXPIRY_DATE, expireDate);
        resultMap.put(KEY_FINGERPRINT, fingerprint);

        return resultMap;
    }

    
    @Override
    public void importKey(String keyFileName) throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_IMPORT, keyFileName};
        runGnuPG(cmdArgs, null);
    }

    
    @Override
    public void exportKey(String outFileName, String keyId)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_ARMOR, GNUPG_PARAM_OUTPUT, outFileName,
            GNUPG_PARAM_EXPORT, keyId};

        runGnuPG(cmdArgs, null);
    }

    
    @Override
    public void encryptAndSign(String signerKeyId, String recipientKeyId,
        String passphrase, File inputFile, File outputFile)
        throws GnupgException, UnsupportedEncodingException
    {
        String inputFileName = inputFile.getAbsolutePath();
        String outputFileName = outputFile.getAbsolutePath();

        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_LOCALUSER, signerKeyId, GNUPG_PARAM_RECIPIENT,
            recipientKeyId, GNUPG_PARAM_OUTPUT, outputFileName,
            GNUPG_PARAM_TRUSTMODEL, TRUST_ALWAYS, GNUPG_PARAM_PASSPHRASE,
            passphrase, GNUPG_PARAM_ARMOR, GNUPG_PARAM_SIGN,
            GNUPG_PARAM_ENCRYPT, inputFileName};

        runGnuPG(cmdArgs, null);
    }
    
    
    @Override
    public String encryptAndSignString(String signerKeyId,
        String recipientKeyId, String passphrase, String inputStr)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_STATUSFD, "1", GNUPG_PARAM_COMMANDFD, "0",
            GNUPG_PARAM_LOCALUSER, signerKeyId, GNUPG_PARAM_RECIPIENT,
            recipientKeyId, GNUPG_PARAM_TRUSTMODEL, TRUST_ALWAYS,
            GNUPG_PARAM_PASSPHRASE, passphrase, GNUPG_PARAM_ARMOR,
            GNUPG_PARAM_SIGN, GNUPG_PARAM_ENCRYPT,};
        
        String rlt = runGnuPG(cmdArgs, inputStr);
        int index1 = rlt.indexOf("-----BEGIN PGP MESSAGE-----");
        int index2 = rlt.indexOf("-----END PGP MESSAGE-----");
        
        return rlt.substring(index1, index2 + 25);
    }
    
    
    @Override
    public String encryptAndSignByte(String signerKeyId,
        String recipientKeyId, String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_STATUSFD, "1", GNUPG_PARAM_COMMANDFD, "0",
            GNUPG_PARAM_LOCALUSER, signerKeyId, GNUPG_PARAM_RECIPIENT,
            recipientKeyId, GNUPG_PARAM_TRUSTMODEL, TRUST_ALWAYS,
            GNUPG_PARAM_PASSPHRASE, passphrase, GNUPG_PARAM_ARMOR,
            GNUPG_PARAM_SIGN, GNUPG_PARAM_ENCRYPT,};
        
        String rlt = runGnuPGForByte(cmdArgs, input);
        int index1 = rlt.indexOf("-----BEGIN PGP MESSAGE-----");
        int index2 = rlt.indexOf("-----END PGP MESSAGE-----");
        
        return rlt.substring(index1, index2 + 25);
    }

    
    @Override
    public void decrypt(String passphrase, File inputFile, File outputFile)
        throws GnupgException, UnsupportedEncodingException
    {
        String inputFileName = inputFile.getAbsolutePath();
        String outputFileName = outputFile.getAbsolutePath();
        
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_PASSPHRASE, passphrase, GNUPG_PARAM_OUTPUT,
            outputFileName, GNUPG_PARAM_DECRYPT, inputFileName};

        runGnuPG(cmdArgs, null);
    }
    
    
    @Override
    public String decryptString(String passphrase, String inputStr)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_COMMANDFD, "0", GNUPG_PARAM_PASSPHRASE, passphrase,
            GNUPG_PARAM_DECRYPT};

        String rlt = runGnuPG(cmdArgs, inputStr);
        
        // remove the 1st prompt line;
        int index = rlt.indexOf(LINE_SEPARATOR);
        rlt = rlt.substring(index + 1);
        
        // remove the 2nd prompt line.
        index = rlt.indexOf(LINE_SEPARATOR);
        rlt = rlt.substring(index + 1);
        
        // remove the prompt info from the last few lines.
        return rlt.substring(0, rlt.lastIndexOf("gpg: Signature made"));
    }
    
    
    @Override
    public String decryptByte(String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_COMMANDFD, "0", GNUPG_PARAM_PASSPHRASE, passphrase,
            GNUPG_PARAM_DECRYPT};

        String rlt = runGnuPGForByte(cmdArgs, input);
        
        // remove the 1st prompt line;
        int index = rlt.indexOf(LINE_SEPARATOR);
        rlt = rlt.substring(index + 1);
        
        // remove the 2nd prompt line.
        index = rlt.indexOf(LINE_SEPARATOR);
        rlt = rlt.substring(index + 1);
        
        // remove the prompt info from the last few lines.
        return rlt.substring(0, rlt.lastIndexOf("gpg: Signature made"));
    }
    
    
    @Override
    public void sendKeyToKeyserver(String userId, String keyserver)
        throws GnupgException, UnsupportedEncodingException
    {
        String ks = keyserver;
        
        if (null == ks)
        {
            ks = this.defaultKeyServer;
        }
        
        String fingerprint = extractKeyDetailFromServer(userId).get(
            KEY_FINGERPRINT);

        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_KEYSERVER, ks, GNUPG_PARAM_SENDKEYS,
            fingerprint};

        runGnuPG(cmdArgs, null);
    }
    
    
    @Override
    public void retrieveKeyFromKeyserver(String userId, String keyserver)
        throws GnupgException, UnsupportedEncodingException
    {
        String ks = keyserver;
        
        if (null == ks)
        {
            ks = this.defaultKeyServer;
        }
        
        
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_STATUSFD, "1", GNUPG_PARAM_COMMANDFD, "0",
            GNUPG_PARAM_KEYSERVER, ks, GNUPG_PARAM_SEARCHKEYS, userId};

        StringBuilder sb = new StringBuilder();
        sb.append("1").append(LINE_SEPARATOR);

        runGnuPG(cmdArgs, sb.toString());
        
    }
    
    
    @Override
    public void downloadKeyFromKeyserver(String fingerprint, String keyserver)
        throws GnupgException, UnsupportedEncodingException
    {
        String ks = keyserver;
        
        if (null == ks)
        {
            ks = this.defaultKeyServer;
        }
        
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_KEYSERVER, ks, GNUPG_PARAM_RECVKEYS,
            fingerprint};

        runGnuPG(cmdArgs, null);
    }
    
    
    @Override
    public void revokeKey(String userId, String passphrase)
        throws GnupgException, UnsupportedEncodingException
    {
        String[] cmdArgs = {obtainFullPathGnupgCmd(), GNUPG_PARAM_HOMEDIR,
            homedir, GNUPG_PARAM_BATCH, GNUPG_PARAM_NOTTY,
            GNUPG_PARAM_STATUSFD, "1", GNUPG_PARAM_COMMANDFD, "0",
            GNUPG_PARAM_PASSPHRASE, passphrase, GNUPG_PARAM_EDITKEY, userId};

        StringBuilder sb = new StringBuilder();
        sb.append("revkey").append(LINE_SEPARATOR);
        sb.append("y").append(LINE_SEPARATOR);
        sb.append("0").append(LINE_SEPARATOR);
        sb.append(LINE_SEPARATOR);
        sb.append("y").append(LINE_SEPARATOR);
        sb.append("save").append(LINE_SEPARATOR);
        
        runGnuPG(cmdArgs, sb.toString());
    }
    
    
    //*****************************************************
    // private methods
    //*****************************************************
    private String runGnuPGForByte(String[] cmdArgs, byte[] input)
        throws GnupgException, UnsupportedEncodingException
    {
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < cmdArgs.length; i++)
        {
            if(null == cmdArgs[i])
            {
                cmdArgs[i] = "";
            }
            strBuilder.append(cmdArgs[i]).append(" ");
        }
        
        String gpgCmd = strBuilder.toString();
        int gpgExitCode = 1; // 0 - success, 1 or others - failure
        String gpgCmdResult = "";

        if(log.isDebugEnabled())
        {
            log.debug("GnuPG Command Line : " + gpgCmd);
            //LOG.info("GnuPG Command Line (Input String) : " + inputStr);
        }

        Process process = null;
        ProcessStreamReaderThread outputThread = null;

        try
        {
            try
            {
                ProcessBuilder builder = new ProcessBuilder(cmdArgs);
                builder.redirectErrorStream(true);//To merge errorstream into inputstream.
                process = builder.start();
            }
            catch(IOException exception)
            {
                ErrorHelper.getInstance().logError(log, exception);
                gpgCmdResult = "Error executing GnuPG: "
                    + exception.getMessage();
                String processedMessage = genProcessedMessage(gpgCmd,
                    gpgExitCode, gpgCmdResult);
                throw new GnupgException(processedMessage, exception);
            }


            outputThread = new ProcessStreamReaderThread(
                process.getInputStream());

            if (input != null)
            {
                OutputStream out = null;
                try
                {
                    out = process.getOutputStream();
                    out.write(input);
                    out.flush();
                }
                catch(IOException exception)
                {
                    ErrorHelper.getInstance().logError(log, exception);
                    gpgCmdResult = "Error writing to GnuPG stdin: "
                        + exception.getMessage();
                    String processedMessage = genProcessedMessage(gpgCmd,
                        gpgExitCode, gpgCmdResult);
                    throw new GnupgException(processedMessage, exception);
                }
                finally
                {
                    if(out != null)
                    {
                        try
                        {
                            out.close();
                            out = null;
                        }
                        catch(IOException exception)
                        {
                            log.error("Error closing GnuPG output stream",
                                exception);
                        }
                    }
                }
            }

            try
            {
                process.waitFor();
                outputThread.join();
            }
            catch(InterruptedException exception)
            {
                ErrorHelper.getInstance().logError(log, exception);
                gpgCmdResult = "Error waiting for GnuPG process to end: "
                    + exception.getMessage();
                String processedMessage = genProcessedMessage(gpgCmd,
                    gpgExitCode, gpgCmdResult);
                throw new GnupgException(processedMessage, exception);
            }
            
            gpgExitCode = process.exitValue();
            gpgCmdResult = outputThread.getString();
        }
        finally
        {
            if(process != null)
            {
                process.destroy();
                process = null;
            }

            outputThread = null;
        }
        
        if (gpgExitCode != 0)
        {
            String processedMessage = genProcessedMessage(gpgCmd,
                gpgExitCode, gpgCmdResult);
            throw new GnupgException(processedMessage);
        }
        
        return gpgCmdResult;
    }
    
    
    private String runGnuPG(String[] cmdArgs, String inputStr)
        throws GnupgException, UnsupportedEncodingException
    {
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < cmdArgs.length; i++)
        {
            if(null == cmdArgs[i])
            {
                cmdArgs[i] = "";
            }
            strBuilder.append(cmdArgs[i]).append(" ");
        }
        
        String gpgCmd = strBuilder.toString();
        int gpgExitCode = 1; // 0 - success, 1 or others - failure
        String gpgCmdResult = "";

        if(log.isDebugEnabled())
        {
            log.debug("GnuPG Command Line : " + gpgCmd);
            log.debug("GnuPG Command Line (Input String) : " + inputStr);
        }

        Process process = null;
        ProcessStreamReaderThread outputThread = null;

        try
        {
            try
            {
                ProcessBuilder builder = new ProcessBuilder(cmdArgs);
                builder.redirectErrorStream(true);//To merge errorstream into inputstream.
                process = builder.start();
            }
            catch(IOException exception)
            {
                //ErrorHelper.getInstance().logError(log, exception);
                gpgCmdResult = "Error executing GnuPG: "
                    + exception.getMessage();
                String processedMessage = genProcessedMessage(gpgCmd,
                    gpgExitCode, gpgCmdResult);
                throw new GnupgException(processedMessage, exception);
            }


            outputThread = new ProcessStreamReaderThread(
                process.getInputStream());

            if (inputStr != null)
            {
                BufferedWriter out = null;
                try
                {
                    out = new BufferedWriter(new OutputStreamWriter(
                        process.getOutputStream(), CommonConstants.ENCODING_UTF8));
                    out.write(inputStr);
                    out.flush();
                }
                catch(IOException exception)
                {
                    ErrorHelper.getInstance().logError(log, exception);
                    gpgCmdResult = "Error writing to GnuPG stdin: "
                        + exception.getMessage();
                    String processedMessage = genProcessedMessage(gpgCmd,
                        gpgExitCode, gpgCmdResult);
                    throw new GnupgException(processedMessage, exception);
                }
                finally
                {
                    if(out != null)
                    {
                        try
                        {
                            out.close();
                            out = null;
                        }
                        catch(IOException exception)
                        {
                            log.error("Error closing GnuPG output stream",
                                exception);
                        }
                    }
                }
            }

            try
            {
                process.waitFor();
                outputThread.join();
            }
            catch(InterruptedException exception)
            {
                ErrorHelper.getInstance().logError(log, exception);
                gpgCmdResult = "Error waiting for GnuPG process to end: "
                    + exception.getMessage();
                String processedMessage = genProcessedMessage(gpgCmd,
                    gpgExitCode, gpgCmdResult);
                throw new GnupgException(processedMessage, exception);
            }
            
            gpgExitCode = process.exitValue();
            gpgCmdResult = outputThread.getString();
        }
        finally
        {
            if(process != null)
            {
                process.destroy();
                process = null;
            }

            outputThread = null;
        }
        
        if (gpgExitCode != 0)
        {
            String processedMessage = genProcessedMessage(gpgCmd,
                gpgExitCode, gpgCmdResult);
            throw new GnupgException(processedMessage);
        }
        
        return gpgCmdResult;
    }
    
    
    private String genProcessedMessage(String gpgCmd, int gpgExitCode,
        String gpgResult)
    {
        String rlt = new StringBuilder().append("GNUPGCMD : ").append(gpgCmd)
            .append(LINE_SEPARATOR).append("EXITCODE : ").append(gpgExitCode)
            .append(LINE_SEPARATOR).append("RESULT : ").append(gpgResult)
            .toString();

        return rlt;
    }

    
    private String extractExpireDate(String inputStr)
    {
        String expireDate = null;
        int expireDataEnd = inputStr.indexOf(']');

        if(expireDataEnd > -1)
        {
            int expireDateIndex = expireDataEnd - 10;
            expireDate = inputStr.substring(expireDateIndex, expireDataEnd)
                .trim();
        }
        return expireDate;
    }
    
    
    private String extractFingerprint(String inputStr)
    {
        return inputStr.trim().split("=")[1].replaceAll(" ", "");
    }
    
    
    private String[] extractKeyId(String key)
    {
        String userId = "";
        String comment = "";
        String email = "";

        int commentIndex = key.indexOf(" (");
        int commentEnd = key.lastIndexOf(')');
        int emailIndex = key.indexOf(" <");
        int emailEnd = key.lastIndexOf('>');

        if(commentIndex > -1 && commentEnd > commentIndex)
        {
            userId = key.substring(0, commentIndex).trim();
        }
        else if(emailIndex > -1 && emailEnd > emailIndex)
        {
            userId = key.substring(0, emailIndex).trim();
        }
        else
        {
            userId = key;
        }

        if(commentIndex > -1 && commentEnd > commentIndex)
        {
            comment = key.substring(commentIndex + 2, commentEnd).trim();
        }

        if(emailIndex > -1 && emailEnd > emailIndex)
        {
            email = key.substring(emailIndex + 2, emailEnd).trim();
        }
        String[] keyIds = new String[3];
        keyIds[0] = userId;
        keyIds[1] = comment;
        keyIds[2] = email;

        return keyIds;
    }
    
    
    private String obtainFullPathGnupgCmd()
    {
        return commandPath.concat(FILE_SEPARATOR).concat(GNUPG_CMD);
    }
    

    public void setCommandPath(String commandPath)
    {
        this.commandPath = commandPath;
    }


    public void setDefaultKeyServer(String defaultKeyServer)
    {
        this.defaultKeyServer = defaultKeyServer;
    }


    @Override
    public byte[] encryptAndSign(String signerKeyId, String recipientKeyId,
        String passphrase, File inputFile) throws GnupgException
    {
        try
        {
            String inputStr = FileUtils.readFileToString(inputFile);
            String contents = this.encryptAndSignString(signerKeyId, recipientKeyId, passphrase, inputStr);
            
            return contents.getBytes(CommonConstants.ENCODING_UTF8);
        }
        catch(IOException exception)
        {
            ErrorHelper.getInstance().logError(log, exception);
            throw new GnupgException(exception);
        }
    }
    
    
    public byte[] encryptAndSign(String signerKeyId, String recipientKeyId,
        String passphrase, byte[] input) throws GnupgException, UnsupportedEncodingException
    {
        String contents = this.encryptAndSignByte(signerKeyId, recipientKeyId, passphrase, input);
        
        return contents.getBytes(CommonConstants.ENCODING_UTF8);
    }


    @Override
    public byte[] decrypt(String passphrase, File inputFile)
        throws GnupgException
    {
        try
        {
            String inputStr = FileUtils.readFileToString(inputFile);
            String contents = this.decryptString(passphrase, inputStr);
            
            return contents.getBytes(CommonConstants.ENCODING_UTF8);
        }
        catch(IOException exception)
        {
            ErrorHelper.getInstance().logError(log, exception);
            throw new GnupgException(exception);
        }
    }


    @Override
    public byte[] decrypt(String passphrase, byte[] input)
        throws GnupgException, UnsupportedEncodingException
    {
        String contents = this.decryptByte(passphrase, input);
        
        return contents.getBytes(CommonConstants.ENCODING_UTF8);
    }


    public Boolean getUseKeyServer()
    {
        return useKeyServer;
    }


    public void setUseKeyServer(Boolean useKeyServer)
    {
        this.useKeyServer = useKeyServer;
    }


    public String getHomedir()
    {
        return homedir;
    }


    public void setHomedir(String homedir)
    {
        this.homedir = homedir;
    }

}
