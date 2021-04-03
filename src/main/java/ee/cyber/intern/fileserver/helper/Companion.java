package ee.cyber.intern.fileserver.helper;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import ee.cyber.intern.fileserver.entities.FileEntity;
import ee.cyber.intern.fileserver.entities.dto.DirectoryDto;
import ee.cyber.intern.fileserver.entities.dto.FileDto;
import ee.cyber.intern.fileserver.mapper.FileStorageMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.Calendar;

public class Companion {
    private static final FileStorageMapper mapper = Mappers.getMapper(FileStorageMapper.class);

    public static LocalDate getTodayDate() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return LocalDate.of(year, month, day);
    }

    public static DirectoryEntity updateDirAccessedDay(DirectoryDto directoryDto) {
        directoryDto.setLastAccessedOn(getTodayDate());

        return mapper.dirDtoToEntity(directoryDto);
    }

    public static FileEntity updateFileAccessedDay(FileDto fileDto) {
        fileDto.setLastAccessedOn(getTodayDate());
        return mapper.fileDtoToEntity(fileDto);
    }

    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
