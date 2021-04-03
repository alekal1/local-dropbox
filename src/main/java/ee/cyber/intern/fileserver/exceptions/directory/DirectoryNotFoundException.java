package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_IS_NOT_EXISTS;

public class DirectoryNotFoundException extends RuntimeException {

    public DirectoryNotFoundException() {
        super(DIR_IS_NOT_EXISTS);
    }
}
