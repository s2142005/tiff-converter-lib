package com.seamfix.imageutil;

import com.seamfix.imageutil.ImageResult;
import com.seamfix.imageutil.Images;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;
//Source: https://stackoverflow.com/questions/15429011/how-to-convert-tiff-to-jpeg-png-in-java

public class Util {

    private static ArrayList<File> bmpFiles = new ArrayList<File>();

    public static void createBMPFromTIFF(@NotNull String tiffFilePath, ImageResult imageResult) {


        //create a file representing the tiff image:
        File tiffInputFile = new File(tiffFilePath);

        //check if the tiff file exists:
        if(!tiffInputFile.exists()){
            imageResult.error("The input file provided does not exist.");
            return;
        }

        //check if the file extension is correct:
        if( !tiffFilePath.endsWith(".tif") && !tiffFilePath.endsWith(".tiff") && !tiffFilePath.endsWith(".TIF")
                && !tiffFilePath.endsWith(".TIFF")){
            imageResult.error("The input file does not have any of the TIFF file extensions.");
            return;
        }

        try{

            try (InputStream is = new FileInputStream(tiffInputFile)) {
                try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(is)) {
                    Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
                    if (iterator == null || !iterator.hasNext()) {
                        throw new RuntimeException("Image file format not supported by ImageIO: " + tiffInputFile.getAbsolutePath());
                    }

                    // We are just looking for the first reader compatible:
                    ImageReader reader = iterator.next();
                    reader.setInput(imageInputStream);

                    //get the number of pages this tiff has:
                    int numPage = reader.getNumImages(true);
                    String tiffInputFilePath = tiffInputFile.getParentFile().getAbsolutePath();

                    //loop through each page, get the tiff and generate a bitmap:
                    IntStream.range(0, numPage).forEach(v -> {
                        try {
                            BufferedImage tiff = reader.read(v);

                            //convert the tiff to png:
                            //First get the desired destination path:
                            File destinationPNGFile = new File(tiffInputFilePath + "/result"+ v +".png");

                            //convert:
                            ImageIO.write(tiff, "png", destinationPNGFile);

                            //check if the png was created successfully:
                            if(destinationPNGFile.exists() && destinationPNGFile.length() > 0){

                                //convert the png to bmp:
                                //First get the desired destination path:
                                String destinationBMPPath = tiffInputFilePath + "/result"+ v +".bmp";

                                //convert:
                                Images.Companion.createBMPFromPGN(destinationPNGFile.getAbsolutePath(), destinationBMPPath);

                                //check if the bmp was created successfully:
                                File destinationBMPFile = new File(destinationBMPPath);

                                if(destinationBMPFile.exists()){
                                    //now delete the png:
                                    destinationPNGFile.delete();

                                    //save the bmp file:
                                    bmpFiles.add(destinationBMPFile);

                                }else{
                                    //Ive noticed that some tiff files (.tiff) will experience this problem of not being able
                                    //to convert from png to bmp. So a workaround is to convert the tiff directly:

                                    //prepare variables:
                                    tiff = ImageIO.read(tiffInputFile);

                                    File outputFile = new File(tiffInputFilePath + "/result"+ v +".bmp");

                                    //convert:
                                    ImageIO.write(tiff, "png", outputFile);//notice how *png* String is used instead of bmp.
                                    //For some strange reasons, it works. If u change it, it won't.

                                    //check if the png was created successfully:
                                    if(outputFile.exists() && outputFile.length() > 0){

                                        //now delete the png:
                                        destinationPNGFile.delete();

                                        //save the bmp file:
                                        bmpFiles.add(outputFile);
                                    }else{
                                        imageResult.error("Unknown error. Could not create bmp image.");
                                    }
                                }

                            }else{
                                imageResult.error("Unknown error. Could not an image for this file.");
                            }

                        } catch (IOException e) {
                            imageResult.error(e.getMessage());
                            e.printStackTrace();
                        }
                    });

                    //return the result:
                    imageResult.result(bmpFiles);

                }catch (Exception e) {
                    imageResult.error(e.getMessage());
                    e.printStackTrace();
                }

            }catch (Exception e) {
                imageResult.error(e.getMessage());
                e.printStackTrace();
            }

        }catch (Exception e){
            imageResult.error(e.getMessage());
        }
    }
}
