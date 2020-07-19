# tiff-converter-lib

## Installation
Get the jar file and add it to your project. You can find the file here:
https://github.com/seamfix/tiff-converter-lib/blob/master/out/artifacts/ImageUtil_jar/ImageUtil.jar


## Usage

        //path to your tiff image file:
        String tiffPath = "/Users/jeffemuveyan/Desktop/images/example.tiff";

        Images.Companion.createBMPFromTIFF(tiffPath, new ImageResult(){

            @Override
            public void result(ArrayList<File> bmpFiles) {

                //use result
                for(File bmpFile : bmpFiles){
                    System.out.println("File name: "+bmpFile.getName()+", size: "+bmpFile.length());
                }
            }

            @Override
            public void error(String message) {

                //handle error
                System.out.println(message);
            }
        });
        
        
## Result
The bmp file(s) will be found in the same directory as the tiff file you provided.
