package storage.web;

import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import storage.domain.Storage;
import storage.domain.StorageService;

import java.util.List;

@Controller
public class StorageController {

    private static Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/storage/overview")
    public String overview (Model model) {
        List<Storage> allStorages = storageService.getStorage();

        if (allStorages.isEmpty()) {
            createSampleData();
            allStorages = storageService.getStorage();
        }

        model.addAttribute("storages", allStorages);
        return "overview-storage";
    }

    private void createSampleData() {
        StorageDto igor = new StorageDto();
        igor.setName("D18");
        igor.setPostalcode(3000);
        igor.setCapacity(20);
        igor.setHeight(10);

        StorageDto ernest = new StorageDto();
        ernest.setName("Q05");
        ernest.setPostalcode(3000);
        ernest.setCapacity(19);
        ernest.setHeight(7);

        storageService.createStorage(igor);
        storageService.createStorage(ernest);
    }

    @GetMapping("/storage/add")
    public String add(Model model) {
        model.addAttribute("storageDto", new StorageDto());
        return "add-storage";
    }

    @PostMapping("/storage/add")
    public String add(@Valid StorageDto storage, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("storageDto", storage);
            return "add-storage";
        }

        storageService.createStorage(storage);
        return "redirect:/overview";
    }

    @GetMapping("/storage/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Storage storage = storageService.getStorage(id);
            model.addAttribute("storageDto", toDto(storage));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "update-patient";
    }

    private StorageDto toDto(Storage storage) {
        StorageDto dto = new StorageDto();

        dto.setName(storage.getName());
        dto.setPostalcode(storage.getPostalcode());
        dto.setCapacity(storage.getCapacity());
        dto.setHeight(storage.getHeight());
        return dto;
    }

    @PostMapping("/storage/update/{id}")
    public String update (@PathVariable("id") long id, @Valid StorageDto storage, BindingResult result, Model model) {
        if (result.hasErrors()) {
            LOGGER.error("ERRORS UPDATING");

            storage.setId(id);
            model.addAttribute("storageDto", storage);
            return "update-patient";
        }

        storageService.updateStorage(id, storage);
        return "redirect:/overview";
    }

}