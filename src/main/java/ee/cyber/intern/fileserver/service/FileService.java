package ee.cyber.intern.fileserver.service;

import ee.cyber.intern.fileserver.dao.FileRepository;
import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import ee.cyber.intern.fileserver.entities.FileEntity;
import ee.cyber.intern.fileserver.entities.dto.FileDto;
import ee.cyber.intern.fileserver.exceptions.file.FileAlreadyExistsException;
import ee.cyber.intern.fileserver.exceptions.file.FileNotFoundException;
import ee.cyber.intern.fileserver.mapper.FileStorageMapper;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ee.cyber.intern.fileserver.helper.Companion.*;

/***
 * Service class for file business logic
 */
@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileStorageMapper mapper = Mappers.getMapper(FileStorageMapper.class);
    private final ServletContext servletContext;

    @Autowired
    public FileService(FileRepository fileRepository, ServletContext servletContext) {
        this.fileRepository = fileRepository;
        this.servletContext = servletContext;
    }

    /** Method to save file into database */
    protected void saveFileToDataBase(MultipartFile multipartFile, Path filePath, DirectoryEntity parentDirectory) {
        /* Create new file DTO */
        FileDto fileDto = new FileDto();
        fileDto.setName(multipartFile.getOriginalFilename());
        fileDto.setSize(BigInteger.valueOf(multipartFile.getSize()));
        fileDto.setCreatedOn(getTodayDate());
        fileDto.setLastAccessedOn(null);
        fileDto.setParentDirectory(parentDirectory);
        fileDto.setFilePath(filePath.toString());

        /* Map dto into entity and save it */
        FileEntity entity = mapper.fileDtoToEntity(fileDto);
        fileRepository.save(entity);
    }

    /** Download file from local storage */
    @SneakyThrows
    public ResponseEntity<InputStreamResource> downloadFile(Long id) {
        if (checkFileExistInDb(id)) {
            FileEntity entity = fileRepository.findById(id).get();
            File file =  new File(entity.getFilePath());
            MediaType mediaType = getMediaTypeForFileName(servletContext, file.getName());
            InputStreamResource res = new InputStreamResource(new FileInputStream(file));

            /* Update file accessed day */
            fileRepository.save(updateFileAccessedDay(mapper.fileEntityToDto(entity)));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                    .contentType(mediaType)
                    .contentLength(file.length())
                    .body(res);
        }
        return null;
    }

    /** Delete file from db and local storage */
    public void deleteFile(Long id) {
        if (checkFileExistInDb(id)) {
            FileEntity entity = fileRepository.findById(id).get();

            /* Delete from local storage */
            deleteFileFromLocalStorage(entity);
            fileRepository.deleteById(id);
        }
    }

    /** Helper method to check if file exists */
    private Boolean checkFileExistInDb(Long id) {
        if (fileRepository.existsById(id)) {
            return true;
        }
        throw new FileNotFoundException();
    }

    /** Helper method to check if file is not exist */
    protected Boolean checkFileNotExists(Path path) {
        File file = new File(String.valueOf(path));
        if (!file.exists()) {
            return true;
        }
        throw new FileAlreadyExistsException();
    }

    /** Helper message to delete file from local storage */
    @SneakyThrows
    private void deleteFileFromLocalStorage(FileEntity fileEntity) {
        Path filePath = Paths.get(fileEntity.getFilePath());
        Files.delete(filePath);
    }

}
