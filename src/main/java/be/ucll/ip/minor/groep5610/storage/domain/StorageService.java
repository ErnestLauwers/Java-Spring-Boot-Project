package be.ucll.ip.minor.groep5610.storage.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import be.ucll.ip.minor.groep5610.storage.web.StorageDto;
import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private MessageSource messageSource;

    public List<Storage> getStorages() {
        return storageRepository.findAll();
    }

    public Storage getStorage(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new RuntimeException("Stalling met id " + id + " niet gevonden"));
    }

    public Storage createStorage(StorageDto dto) {
        Storage existingStorages = storageRepository.findByNameAndPostalCode(dto.getName(), dto.getPostalCode());
        if (existingStorages != null) {
            String message = messageSource.getMessage("combination.not.unique", null, null);
            throw new IllegalArgumentException(message);
        }
        Storage storage = new Storage();
        storage.setName(dto.getName());
        storage.setPostalCode(dto.getPostalCode());
        storage.setSpace(dto.getSpace());
        storage.setHeight(dto.getHeight());
        return storageRepository.save(storage);
    }

    public void deleteStorage(Long id) {
        storageRepository.deleteById(id);
    }

    public void updateStorage(StorageDto dto, Storage storage) {
        storage.setName(dto.getName());
        storage.setPostalCode(dto.getPostalCode());
        storage.setSpace(dto.getSpace());
        storage.setHeight(dto.getHeight());
        storageRepository.save(storage);
    }

    public List<Storage> sortByNameAsc() {
        return storageRepository.findByOrderByNameAsc();
    }

    public List<Storage> sortByNameDesc() {
        return storageRepository.findByOrderByNameDesc();
    }

    public List<Storage> sortByHeightAsc() {
        return storageRepository.findByOrderByHeightAsc();
    }

    public List<Storage> sortByHeightDesc() {
        return storageRepository.findByOrderByHeightDesc();
    }

    public List<Storage> findStoragesBySearch(String keyword) {
        return storageRepository.findStoragesBySearch(keyword);
    }

}