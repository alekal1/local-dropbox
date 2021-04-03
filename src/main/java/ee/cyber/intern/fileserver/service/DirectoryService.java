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

import static ee.cyber.intern.fileserver.helper.Companion.updateDirAccessedDay;


/***
 * Service class for business logic
 */
@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;
    private final FileService fileService;
    private final FileStorageMapper mapper = Mappers.getMapper(FileStorageMapper.class);
    private final String localStoragePath = "src/main/resources/localStorage/";

    @Autowired
    public DirectoryService(
            DirectoryRepository directoryRepository,
            FileService fileService) {
        this.directoryRepository = directoryRepository;
        this.fileService = fileService;
    }

    public List<DirectoryDto> getAllDirectories() {
        List<DirectoryEntity> allDirectories = directoryRepository.findAll();
        return allDirectories.stream()
                .map(mapper::dirEntityToDto)
                .collect(Collectors.toList());
    }

    public DirectoryDto getDirectoryById(Long id) {
        if (directoryExists(id)) {
            DirectoryEntity directory = directoryRepository.findById(id).get();
            return mapper.dirEntityToDto(directory);
        }
        return null;
    }

    @SneakyThrows
    public DirectoryDto createDirectory(DirectoryDto directoryDto){
        /* Top level directory will have local storage path */
        Path dirPath = Paths.get(localStoragePath + directoryDto.getName());

        if (createLocalDirectory(dirPath)) {
            DirectoryEntity entity = mapper.dirDtoToEntity(directoryDto);
            entity.setDirectoryPath(dirPath.toString()); // Set entity path in db

            DirectoryEntity directory = directoryRepository.save(entity);

            directoryDto.setId(directory.getId()); // return directory should have id

            return directoryDto;
        }
        throw new DirectoryAlreadyExistsException(); // Throw custom exception
    }

    @SneakyThrows
    public void deleteDirectory(Long id) {
        if (directoryExists(id) && directoryIsEmpty(id)) {
            /* Since already checked directory can use get() method since Optional will be always present */
            DirectoryEntity entity = directoryRepository.findById(id).get();

            deleteDirFromLocalStorage(entity);

            directoryRepository.deleteById(id);
        }
    }


    public DirectoryDto createSubDirectory(DirectoryDto directoryDto, Long id) {
        if (directoryExists(id)) {
            /* Since already checked directory can use get() method since Optional will be always present */
            DirectoryEntity parentDirectory = directoryRepository.findById(id).get();

            /* Generate directory path */
            Path newDirPath = Paths.get(parentDirectory.getDirectoryPath() + "/" + directoryDto.getName());

            if (createLocalDirectory(newDirPath)) {
                DirectoryEntity entity = mapper.dirDtoToEntity(directoryDto);
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

    @SneakyThrows
    private Boolean createLocalDirectory(Path directoryPath) {
        if (!Files.exists(directoryPath)) {
            Files.createDirectory(directoryPath);
            return true;
        }
        return false;
    }

    @SneakyThrows
    private void deleteDirFromLocalStorage(DirectoryEntity entity) {
        Path dirPath = Paths.get(entity.getDirectoryPath());
        Files.delete(dirPath);
    }

    private Boolean directoryIsEmpty(Long id) {
        DirectoryEntity directory = directoryRepository.findById(id).get();
        File dir = new File(localStoragePath + directory.getName());
        String[] files = dir.list();
        if ( files == null || files.length == 0) {
            return true;
        } throw new DirectoryIsNotEmptyException();
    }

    private Boolean directoryExists(Long id) {
        if (directoryRepository.existsById(id)) {
            return true;
        }
        throw new DirectoryNotFoundException();
    }

    private Boolean checkDirectorySpace(DirectoryEntity directoryEntity, MultipartFile file) {
        if (file.getSize() < directoryEntity.getSize().longValue()) {
            long newDirectorySpace = directoryEntity.getSize().longValue() - file.getSize();

            DirectoryDto directoryDto = mapper.dirEntityToDto(directoryEntity);
            directoryDto.setSize(BigInteger.valueOf(newDirectorySpace));
            directoryRepository.save(updateDirAccessedDay(directoryDto));
            return true;
        }
        throw new DirectorySpaceFullException();
    }
}
