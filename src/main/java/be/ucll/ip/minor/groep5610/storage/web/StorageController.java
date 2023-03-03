package be.ucll.ip.minor.groep5610.storage.web;

import be.ucll.ip.minor.groep5610.regatta.domain.Regatta;
import be.ucll.ip.minor.groep5610.storage.domain.Storage;
import be.ucll.ip.minor.groep5610.storage.domain.StorageService;
import org.h2.engine.Mode;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Storage> allStorages = storageService.getStorages();

        if (allStorages.isEmpty()) {
            createSampleData();
            allStorages = storageService.getStorages();
        }

        model.addAttribute("storages", allStorages);
        return "/storage/overview";
    }

    @GetMapping("/storage/add")
    public String add(Model model) {
        model.addAttribute("storageDto", new StorageDto());
        return "/storage/add";
    }

    @PostMapping("/storage/add")
    public String add(@Valid StorageDto storage, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("storageDto", storage);
                return "/storage/add";
            }
            storageService.createStorage(storage);
            return "redirect:/storage/overview";
        } catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return "/storage/add";
        }
    }

    @GetMapping("/storage/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Storage storage = storageService.getStorage(id);
        model.addAttribute("storage", toDto(storage));
        return "/storage/delete";
    }

    @PostMapping("/storage/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        storageService.deleteStorage(id);
        return "redirect:/storage/overview";
    }

    @GetMapping("/storage/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        Storage storage = storageService.getStorage(id);
        model.addAttribute("storageDto", toDto(storage));
        return "/storage/update";
    }

    @PostMapping("/storage/update/{id}")
    public String update (@PathVariable("id") long id, @Valid StorageDto dto, BindingResult result, Model model) {
        Storage storage = storageService.getStorage(id);
        try {
            if(result.hasErrors()) {
                model.addAttribute("storage", toDto(storage));
                return "/storage/update";
            }
            storageService.updateStorage(dto, storage);
            return "redirect:/storage/overview";
        } catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
            return "regatta/update";
        }
    }

    @GetMapping("/storage/sort/name")
    public String sortByName(Model model) {
        List<Storage> storages = storageService.sortByName();
        model.addAttribute("storages", storages);
        return "/storage/overview";
    }

    @GetMapping("/storage/sort/height")
    public String sortByHeight(Model model) {
        List<Storage> storages = storageService.sortByHeight();
        model.addAttribute("storages", storages);
        return "/storage/overview";
    }

    @GetMapping("/storage/search")
    public String search(@RequestParam(value = "searchValue") String keyword, Model model) {
        List<Storage> storages = storageService.findStoragesBySearch(keyword);
        model.addAttribute("storages", storages);
        return "/storage/overview";
    }

    public StorageDto toDto(Storage storage) {
        StorageDto storageDto = new StorageDto();
        storageDto.setId(storage.getId());
        storageDto.setName(storage.getName());
        storageDto.setPostalCode(storage.getPostalCode());
        storageDto.setSpace(storage.getSpace());
        storageDto.setHeight(storage.getHeight());
        return storageDto;
    }

    private void createSampleData() {
        StorageDto storage1 = new StorageDto();
        storage1.setName("D18Q3");
        storage1.setPostalCode(3000);
        storage1.setSpace(20);
        storage1.setHeight(10);

        StorageDto storage2 = new StorageDto();
        storage2.setName("Q0E435");
        storage2.setPostalCode(3000);
        storage2.setSpace(19);
        storage2.setHeight(14);

        storageService.createStorage(storage1);
        storageService.createStorage(storage2);
    }

}