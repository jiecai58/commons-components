package com.util.encrypt.asymmetric;

import java.security.PublicKey;

public class RsaUtilTest {

    public static void main(String[] args) throws Exception {
        String publickey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIQ4DArMpGlxiqA1aOcTSTbE8Jh8eBNCEpgUp1fGaPaDk9bmV38NMehmky7nhCp+5hA5ple4EdswhaSU9hrfkIkCAwEAAQ==";
        String encryptedPassword = "fZcDBA4qhUw6etPs5eQGOMJWRpTCDEFPLXpP1GXB7bPDu0e5IT7SflK2Xsy4BYbQqYEpscrNFIGsLjtJQHwZ+Q==";

        PublicKey publicKey =  com.util.encrypt.asymmetric.RsaUtil.getPublicKey(publickey);
        String password = com.util.encrypt.asymmetric.RsaUtil.decrypt(publicKey, encryptedPassword);
        System.out.println(password);

        com.util.encrypt.asymmetric.RsaUtil configTools = new com.util.encrypt.asymmetric.RsaUtil();
        configTools.main(new String[] {"kUdB!Xb[3$A6"});

    }
}
