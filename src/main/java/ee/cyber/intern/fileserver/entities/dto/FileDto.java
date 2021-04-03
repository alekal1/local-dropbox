package ee.cyber.intern.fileserver.entities.dto;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
public class FileDto {
    private Long id;
    private String name;
    private LocalDate createdOn;
    private LocalDate lastAccessedOn;
    private BigInteger size;
    private String filePath;
    private DirectoryEntity parentDirectory;
}
