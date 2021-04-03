package ee.cyber.intern.fileserver.exceptions.file;

import static ee.cyber.intern.fileserver.constant.C.FILE_IS_NOT_EXISTS;

/**
 * Custom exception if file not found
 */
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super(FILE_IS_NOT_EXISTS);
    }
}
