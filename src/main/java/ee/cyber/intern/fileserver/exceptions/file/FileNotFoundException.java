package ee.cyber.intern.fileserver.exceptions.file;

import static ee.cyber.intern.fileserver.constant.C.FILE_IS_NOT_EXISTS;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super(FILE_IS_NOT_EXISTS);
    }
}
