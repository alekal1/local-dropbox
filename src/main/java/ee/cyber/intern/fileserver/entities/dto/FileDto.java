package ee.cyber.intern.fileserver.entities.dto;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDate;

/***
 * Data transfer object represent prototype of object in table file of database
 */
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
