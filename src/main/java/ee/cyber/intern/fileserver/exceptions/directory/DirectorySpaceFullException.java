package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_SPACE_FULL_MSG;

/**
 * Custom exception if file's size more than directory space
 */
public class DirectorySpaceFullException extends RuntimeException {
    public DirectorySpaceFullException() {
        super(DIR_SPACE_FULL_MSG);
    }
}
