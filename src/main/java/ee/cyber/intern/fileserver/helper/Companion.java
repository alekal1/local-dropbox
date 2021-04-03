package ee.cyber.intern.fileserver.helper;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import ee.cyber.intern.fileserver.entities.FileEntity;
import ee.cyber.intern.fileserver.entities.dto.DirectoryDto;
import ee.cyber.intern.fileserver.entities.dto.FileDto;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.Calendar;

import static ee.cyber.intern.fileserver.constant.C.MAPPER;


/***
 * Companion class for holding methods that are used across all application
 */
public class Companion {


    /* Get today date in format year-month-day */
    public static LocalDate getTodayDate() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return LocalDate.of(year, month, day);
    }

    /* Update directory accessed date */
    public static DirectoryEntity updateDirAccessedDay(DirectoryDto directoryDto) {
        directoryDto.setLastAccessedOn(getTodayDate());

        return MAPPER.dirDtoToEntity(directoryDto);
    }

    /* Update file accessed date */
    public static FileEntity updateFileAccessedDay(FileDto fileDto) {
        fileDto.setLastAccessedOn(getTodayDate());
        return MAPPER.fileDtoToEntity(fileDto);
    }

    /* Get media type for file */
    public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            return MediaType.parseMediaType(mineType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
