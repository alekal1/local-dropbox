package ee.cyber.intern.fileserver.mapper;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import ee.cyber.intern.fileserver.entities.FileEntity;
import ee.cyber.intern.fileserver.entities.dto.DirectoryDto;
import ee.cyber.intern.fileserver.entities.dto.FileDto;
import org.mapstruct.Mapper;

/***
 * Mapper class for mapping between objects and their DTOs
 */
@Mapper
public interface FileStorageMapper {
    FileDto fileEntityToDto(FileEntity file);
    DirectoryDto dirEntityToDto(DirectoryEntity directory);

    FileEntity fileDtoToEntity(FileDto fileDto);
    DirectoryEntity dirDtoToEntity(DirectoryDto directoryDto);
}
