package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_SPACE_FULL_MSG;

public class DirectorySpaceFullException extends RuntimeException {
    public DirectorySpaceFullException() {
        super(DIR_SPACE_FULL_MSG);
    }
}
