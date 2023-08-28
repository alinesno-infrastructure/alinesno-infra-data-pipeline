package com.alinesno.infra.data.pipeline.datasource.exception;

import java.io.IOException;

public class ReaderSourceException extends RuntimeException {

    public ReaderSourceException(IOException e) {
        super(e);
    }
}
