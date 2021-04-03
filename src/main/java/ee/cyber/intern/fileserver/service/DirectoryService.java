package ee.cyber.intern.fileserver.service;

import ee.cyber.intern.fileserver.dao.DirectoryRepository;
import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import ee.cyber.intern.fileserver.entities.dto.DirectoryDto;
import ee.cyber.intern.fileserver.exceptions.directory.DirectoryAlreadyExistsException;
import ee.cyber.intern.fileserver.exceptions.directory.DirectoryIsNotEmptyException;
import ee.cyber.intern.fileserver.exceptions.directory.DirectoryNotFoundException;
import ee.cyber.intern.fileserver.exceptions.directory.DirectorySpaceFullException;
import ee.cyber.intern.fileserver.mapper.FileStorageMapper;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static ee.cyber.intern.fileserver.constant.C.LOCAL_STORAGE_PATH;
import static ee.cyber.intern.fileserver.constant.C.MAPPER;
import static ee.cyber.intern.fileserver.helper.Companion.updateDirAccessedDay;


/***
 * Service class for directory business logic
 */
@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;
    private final FileService fileService;

    @Autowired
    public DirectoryService(
            DirectoryRepository directoryRepository,
            FileService fileService) {
        this.directoryRepository = directoryRepository;
        this.fileService = fileService;
    }

    /** Gets all directories from database */
    public List<DirectoryDto> getAllDirectories() {
        List<DirectoryEntity> allDirectories = directoryRepository.findAll();
        return allDirectories.stream()
                .map(MAPPER::dirEntityToDto)
                .collect(Collectors.toList());
    }

    /** Get directory by id */
    public DirectoryDto getDirectoryById(Long id) {
        if (directoryExists(id)) {
            DirectoryEntity directory = directoryRepository.findById(id).get();
            return MAPPER.dirEntityToDto(directory);
        }
        return null;
    }

    /** Create directory from given directoryDto */
    @SneakyThrows
    public DirectoryDto createDirectory(DirectoryDto directoryDto){
        /* Top level directory will have local storage path */
        Path dirPath = Paths.get(LOCAL_STORAGE_PATH + directoryDto.getName());

        if (createLocalDirectory(dirPath)) {
            DirectoryEntity entity = MAPPER.dirDtoToEntity(directoryDto);
            entity.setDirectoryPath(dirPath.toString()); // Set entity path in db

            DirectoryEntity directory = directoryRepository.save(entity);

            directoryDto.setId(directory.getId()); // return directory should have id

            return directoryDto;
        }
        throw new DirectoryAlreadyExistsException(); // Throw custom exception
    }

    /** Delete directory by it's id */
    @SneakyThrows
    public void deleteDirectory(Long id) {
        if (directoryExists(id) && directoryIsEmpty(id)) {
            /* Since already checked directory can use get() method since Optional will be always present */
            DirectoryEntity entity = directoryRepository.findById(id).get();

            /* Delete also from localStorage */
            deleteDirFromLocalStorage(entity);

            directoryRepository.deleteById(id);
        }
    }


    /** Creates sub directory if given directory */
    public DirectoryDto createSubDirectory(DirectoryDto directoryDto, Long id) {
        if (directoryExists(id)) {
            /* Since already checked directory can use get() method since Optional will be always present */
            DirectoryEntity parentDirectory = directoryRepository.findById(id).get();

            /* Generate directory path */
            Path newDirPath = Paths.get(parentDirectory.getDirectoryPath() + "/" + directoryDto.getName());

            if (createLocalDirectory(newDirPath)) {
                DirectoryEntity entity = MAPPER.dirDtoToEntity(directoryDto);
                /* Set directory parent and directory path to entity in db */
                entity.setParentDirectory(parentDirectory);
                entity.setDirectoryPath(newDirPath.toString());

                DirectoryEntity directory = directoryRepository.save(entity);
                directoryDto.setId(directory.getId());

                return directoryDto;
            }
        }
        return null;
    }

    /** Uploads file to directory */
    public void uploadFile(MultipartFile multipartFile, Long id) throws IOException {
        if (directoryExists(id)) {
            DirectoryEntity dir = directoryRepository.findById(id).get();
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(dir.getDirectoryPath() + "\\" + multipartFile.getOriginalFilename());

            if (checkDirectorySpace(dir, multipartFile) && fileService.checkFileNotExists(path)) {
                Files.write(path, bytes);
                fileService.saveFileToDataBase(multipartFile, path, dir);
            }
        }
    }

    /** Helper method to create directory in local storage */
    @SneakyThrows
    private Boolean createLocalDirectory(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            Files.createDirectory(directoryPath);
            return true;
        }
        return false;
    }

    /** Helper method to delete directory from local storage */
    @SneakyThrows
    private void deleteDirFromLocalStorage(DirectoryEntity entity) {
        Path dirPath = Paths.get(entity.getDirectoryPath());
        Files.delete(dirPath);
    }

    /** Helper method to check if directory is empty */
    private Boolean directoryIsEmpty(Long id) {
        DirectoryEntity directory = directoryRepository.findById(id).get();
        File dir = new File(LOCAL_STORAGE_PATH + directory.getName());
        String[] files = dir.list();
        if ( files == null || files.length == 0) {
            return true;
        } throw new DirectoryIsNotEmptyException();
    }

    /** Helper method to check if directory exists */
    private Boolean directoryExists(Long id) {
        if (directoryRepository.existsById(id)) {
            return true;
        }
        throw new DirectoryNotFoundException();
    }

    /** Helper method to check if directory space is lower than file size */
    private Boolean checkDirectorySpace(DirectoryEntity directoryEntity, MultipartFile file) {
        if (file.getSize() < directoryEntity.getSize().longValue()) {
            long newDirectorySpace = directoryEntity.getSize().longValue() - file.getSize();

            DirectoryDto directoryDto = MAPPER.dirEntityToDto(directoryEntity);
            directoryDto.setSize(BigInteger.valueOf(newDirectorySpace));
            directoryRepository.save(updateDirAccessedDay(directoryDto));
            return true;
        }
        throw new DirectorySpaceFullException();
    }
}
