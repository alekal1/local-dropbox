package ee.cyber.intern.fileserver.controller;

import ee.cyber.intern.fileserver.service.FileService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;

import static ee.cyber.intern.fileserver.constant.C.FILE_API_PATH;
import static ee.cyber.intern.fileserver.helper.Companion.getMediaTypeForFileName;

/***
 * File controller to handle requests method for files
 */
@RestController
@RequestMapping(path = FILE_API_PATH)
public class FileController {


    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) {
        return fileService.downloadFile(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
    }

}
