package ee.cyber.intern.fileserver.controller;

import ee.cyber.intern.fileserver.entities.dto.DirectoryDto;
import ee.cyber.intern.fileserver.service.DirectoryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static ee.cyber.intern.fileserver.constant.C.DIRECTORY_API_PATH;

/***
 * Directory controller to handle requests method for directories
 */
@RestController
@RequestMapping(path = DIRECTORY_API_PATH)
public class DirectoryController {

    private final DirectoryService directoryService;

    @Autowired
    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping
    public List<DirectoryDto> getAllDirectories() {
        return directoryService.getAllDirectories();
    }

    @GetMapping("/{id}")
    public DirectoryDto getDirectoryById(@PathVariable Long id) {
        return directoryService.getDirectoryById(id);
    }

    @SneakyThrows
    @PostMapping
    public DirectoryDto createDirectory(@RequestBody DirectoryDto directoryDto)  {
        return directoryService.createDirectory(directoryDto);
    }

    @PostMapping("/{id}")
    public DirectoryDto createSubDirectory(@RequestBody DirectoryDto directoryDto, @PathVariable Long id) {
        return directoryService.createSubDirectory(directoryDto, id);
    }

    @PostMapping("/{id}/file")
    public void uploadFile(@RequestParam(value = "file") MultipartFile multipartFile, @PathVariable Long id) throws IOException {
        directoryService.uploadFile(multipartFile, id);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectory(@PathVariable Long id) {
        directoryService.deleteDirectory(id);
    }
}
