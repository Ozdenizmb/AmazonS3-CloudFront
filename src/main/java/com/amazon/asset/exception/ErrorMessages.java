package com.amazon.asset.exception;

public class ErrorMessages {

    private ErrorMessages(){}

    public static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred! Please contact the api support!";

    public static final String FILE_NOT_FOUND = "File Not Found!";

    public static final String FILE_CANNOT_DELETE = "An error occurred while deleting the file!";

    public static final String FILE_CANNOT_WRITE = "An error occurred while uploading the file!";

    public static final String UNSUPPORTED_FILE_TYPE = "Unsupported file. Only PNG, JPEG, JPG, MP3, MP4, PDF and CSV supported";

}
