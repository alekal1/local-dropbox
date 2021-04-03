package ee.cyber.intern.fileserver.entities.dto;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;


@Data
@RequiredArgsConstructor
public class DirectoryDto {
    private Long id;
    private String name;
    private LocalDate createdOn;
    private LocalDate lastAccessedOn;
    private BigInteger size;
    private String directoryPath;
    private DirectoryEntity parentDirectory;
}
