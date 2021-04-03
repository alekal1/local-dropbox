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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ee.cyber.intern.fileserver.helper.Companion.getTodayDate;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileStorageMapper mapper = Mappers.getMapper(FileStorageMapper.class);

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    protected void saveFileToDataBase(MultipartFile multipartFile, Path filePath, DirectoryEntity parentDirectory) {
        FileDto fileDto = new FileDto();
        fileDto.setName(multipartFile.getOriginalFilename());
        fileDto.setSize(BigInteger.valueOf(multipartFile.getSize()));
        fileDto.setCreatedOn(getTodayDate());
        fileDto.setLastAccessedOn(null);
        fileDto.setParentDirectory(parentDirectory);
        fileDto.setFilePath(filePath.toString());

        FileEntity entity = mapper.fileDtoToEntity(fileDto);
        fileRepository.save(entity);
    }

    public File getFile(Long id) {
        if (checkFileExist(id)) {
            FileEntity entity = fileRepository.findById(id).get();
            return new File(entity.getFilePath());
        }
        return null;
    }

    public void deleteFile(Long id) {
        if (checkFileExist(id)) {
            FileEntity entity = fileRepository.findById(id).get();
            deleteFileFromLocalStorage(entity);
            fileRepository.deleteById(id);
        }
    }

    private Boolean checkFileExist(Long id) {
        if (fileRepository.existsById(id)) {
            return true;
        }
        throw new FileNotFoundException();
    }

    protected Boolean checkFileNotExists(Path path) {
        File file = new File(String.valueOf(path));
        if (!file.exists()) {
            return true;
        }
        throw new FileAlreadyExistsException();
    }

    @SneakyThrows
    private void deleteFileFromLocalStorage(FileEntity fileEntity) {
        Path filePath = Paths.get(fileEntity.getFilePath());
        Files.delete(filePath);
    }

}
