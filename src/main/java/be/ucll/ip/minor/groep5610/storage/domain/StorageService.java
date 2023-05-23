package be.ucll.ip.minor.groep5610.storage.domain;

import be.ucll.ip.minor.groep5610.boat.domain.Boat;
import be.ucll.ip.minor.groep5610.boat.domain.BoatService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import be.ucll.ip.minor.groep5610.storage.web.StorageDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BoatService boatService;

    public List<Storage> getStorages() {
        return storageRepository.findAll();
    }

    public Storage getStorage(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new ServiceException("Stalling met id " + id + " niet gevonden"));
    }

    public List<Storage> getStorage(int page, int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Storage> storagePage = storageRepository.findAll(pageable);
        return storagePage.getContent();
    }

    public int getStorageCount()
    {
        return (int)storageRepository.count();
    }

    public Storage createStorage(StorageDto dto) {
        Storage existingStorages = storageRepository.findByNameAndPostalCode(dto.getName(), dto.getPostalCode());
        if (existingStorages != null) {
            String message = messageSource.getMessage("combination.not.unique", null, LocaleContextHolder.getLocale());
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
        Storage storage = getStorage(id);
        if (storage.getBoats() != null) {
            List<Boat> toRemove = new ArrayList<>();
            for (Boat boat : storage.getBoats()) {
                toRemove.add(boat);
            }
            for (Boat boat : toRemove) {
                storage.removeBoat(boat);
            }
        }
        storageRepository.deleteById(id);
    }

    public void updateStorage(StorageDto dto, Storage storage) {
        Storage existingStorage = storageRepository.findByNameAndPostalCode(dto.getName(), dto.getPostalCode());
        if (existingStorage != null && existingStorage.getId() != storage.getId()) {
            String message = messageSource.getMessage("combination.not.unique", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(message);
        }
        for (Boat boat : storage.getBoats()) {
            if (boat.getHeight() > dto.getHeight()) {
                String message = messageSource.getMessage("updated.storage.height.smaller.than.boats.inside", null, LocaleContextHolder.getLocale());
                throw new IllegalArgumentException(message);
            }
        }
        int occupiedSpace = 0;
        for (Boat boat : storage.getBoats()) {
            occupiedSpace += boat.getLength() * boat.getWidth();
        }
        int availableSpace = (int) (dto.getSpace() * 0.8);
        if (occupiedSpace > availableSpace) {
            String message = messageSource.getMessage("updated.storage.space.smaller.than.space.occupied", null, LocaleContextHolder.getLocale());
            throw new IllegalArgumentException(message);
        }
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

    public Boat addBoatToStorage(Long boatId, Long storageId) {
        Boat boat = boatService.getBoat(boatId);
        Storage storage = getStorage(storageId);

        if (boat.getStorage() != null) {
            String message = messageSource.getMessage("boat.already.in.storage", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }

        for (Boat b : storage.getBoats()) {
            if (b.getEmail().equals(boat.getEmail())) {
                String message = messageSource.getMessage("owner.cant.have.multiple.boats.in.same.storage", null, LocaleContextHolder.getLocale());
                throw new ServiceException(message);
            }
        }

        int requiredSpace = boat.getLength() * boat.getWidth();
        int occupiedSpace = 0;
        for (Boat b : storage.getBoats()) {
            occupiedSpace += b.getLength() * b.getWidth();
        }
        int availableSpace = (int) (storage.getSpace() * 0.8) - occupiedSpace;

        if (requiredSpace > availableSpace) {
            String message = messageSource.getMessage("storage.out.of.space", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }

        if (boat.getHeight() > storage.getHeight()) {
            String message = messageSource.getMessage("boat.height.larger.than.storage.height", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }

        storage.addBoat(boat);
        storageRepository.save(storage);
        return boat;
    }

    public List<Boat> getAllBoatsInStorage(Long storageId) {
        Storage storage = getStorage(storageId);

        return storage.getBoats();
    }

    public Boat removeBoatFromStorage(Long boatId, Long storageId) {
        Boat boat = boatService.getBoat(boatId);
        Storage storage = getStorage(storageId);

        if (!storage.getBoats().contains(boat)) {
            String message = messageSource.getMessage("boat.not.stored.in.this.storage", null, LocaleContextHolder.getLocale());
            throw new ServiceException(message);
        }

        storage.removeBoat(boat);
        storageRepository.save(storage);
        return boat;
    }
}