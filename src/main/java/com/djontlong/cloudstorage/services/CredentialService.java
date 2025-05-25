package com.djontlong.cloudstorage.services;


import com.djontlong.cloudstorage.mapper.CredentialMapper;
import com.djontlong.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private List<Credential> listCredentials;
    private final CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

  

    
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating CredentialService bean");
    }

    public void addCredential(Credential credential) { 
    	SecureRandom random = new SecureRandom();
    	byte[] key = new byte[16];
    	random.nextBytes(key);
    	String encodedKey = Base64.getEncoder().encodeToString(key);
    	String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

		// установка значений в объект
    	credential.setPassword(encryptedPassword);
    	credential.setKey(encodedKey);

		// добавление учетных данных в базу данных (объект Credential)
        credentialMapper.insert(credential);
     
    }

    public List<Credential> getCredentials() {
        return credentialMapper.getCredentials();
    }
    
    public List<Credential> getAllCredentials(Integer userId){
        List<Credential> credentialList = credentialMapper.getCredentialsByUser(userId);
        return credentialList;
    }

	public Credential getCredential(int credentialId) {
		Credential updateCredential = credentialMapper.findCredential(credentialId);
		return updateCredential;
	}

	public void updateCredential(Credential credential) {
		SecureRandom random = new SecureRandom();
    	byte[] key = new byte[16];
    	random.nextBytes(key);
    	String encodedKey = Base64.getEncoder().encodeToString(key);
    	String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

		// установка значений в объект
    	credential.setPassword(encryptedPassword);
    	
    	credential.setKey(encodedKey);




		// обновление в базе данных
		credentialMapper.updateCredential(credential);
		
	}

	public void deleteCredential(Integer noteId) {
		// удаление из базы данных
		credentialMapper.delete(noteId);
		
	}
}
