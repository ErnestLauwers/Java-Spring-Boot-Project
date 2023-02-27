package be.ucll.ip.minor.groep5610.storage.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import be.ucll.ip.minor.groep5610.storage.web.StorageDto;

import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    public List<Storage> getStorage() {
        return storageRepository.findAll();
    }

    public Storage getStorage(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new RuntimeException("Storage with id " + id + " not found"));
    }

    public Storage createStorage(StorageDto dto) {
        Storage storage = new Storage();

        storage.setName(dto.getName());
        storage.setPostalcode(dto.getPostalcode());
        storage.setCapacity(dto.getCapacity());
        storage.setHeight(dto.getHeight());

        return storageRepository.save(storage);
    }

    public Storage updateStorage(Long id, StorageDto dto) {
        Storage storage = getStorage(id);

        storage.setName(dto.getName());
        storage.setPostalcode(dto.getPostalcode());
        storage.setCapacity(dto.getCapacity());
        storage.setHeight(dto.getHeight());

        return storageRepository.save(storage);
    }

}