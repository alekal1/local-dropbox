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

@RestController
@RequestMapping(path = FILE_API_PATH)
public class FileController {

    private final ServletContext servletContext;

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService, ServletContext servletContext) {
        this.fileService = fileService;
        this.servletContext = servletContext;
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) {
        File file = fileService.getFile(id);
        MediaType mediaType = getMediaTypeForFileName(servletContext, file.getName());
        InputStreamResource res = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(res);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
    }

}
