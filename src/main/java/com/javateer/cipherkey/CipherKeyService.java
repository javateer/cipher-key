package com.javateer.cipherkey;

public interface CipherKeyService {

    String getCipherKey();

    void reloadCipherKey();

    String getDecoyKey();

    void reloadDecoyKey();
}
