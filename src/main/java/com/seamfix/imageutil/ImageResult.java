package com.seamfix.imageutil;

import java.io.File;
import java.util.ArrayList;

public interface ImageResult {
    public void result(ArrayList<File> bmpFiles);
    public void error(String message);
}
