package com.amazon.asset.exception;

public class ErrorMessages {

    private ErrorMessages(){}

    public static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred! Please contact the api support!";

    public static final String IMAGES_NOT_FOUND = "Images Not Found!";

    public static final String IMAGE_CANNOT_DELETE = "An error occurred while deleting the file!";

    public static final String IMAGE_CANNOT_WRITE = "An error occurred while uploading the file!";

    public static final String UNSUPPORTED_FILE_TYPE = "Unsupported file. Only PGN, JPEG and JPG supported";

}
