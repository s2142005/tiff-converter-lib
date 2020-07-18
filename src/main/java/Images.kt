import java.io.File
import javax.imageio.ImageIO

//Source: https://stackoverflow.com/questions/15429011/how-to-convert-tiff-to-jpeg-png-in-java
//Source: https://stackoverflow.com/questions/19378334/png-to-bitmap-file-conversion

class Images {

    companion object{


        /*** This will create a PNG from a TIFF image that contains just 1 or multiple pages ***/
        fun createBMPFromTIFF(tiffFilePath: String, imageResult: ImageResult) = Util.createBMPFromTIFF(tiffFilePath, imageResult)


        /*** This will create a PNG from a TIFF image that contains just 1 page ***/
        fun createPGNFromTIFF(tiffFilePath: String, pngFilePath: String): Boolean{
            var tiffFile = File(tiffFilePath)

            //check if the tiff file exists:
            if(tiffFile.exists()){

                //prepare variables:
                val tif = ImageIO.read(tiffFile)
                val outputFile = File(pngFilePath)

                //convert:
                ImageIO.write(tif, "png", outputFile)

                //check if the .png file has been created successfully:
                if(outputFile.exists() && outputFile.length() > 0){
                    return true
                }
            }
            return false
        }


        /*** This will create a BMP from a PNG image ***/
        fun createBMPFromPGN(pngFilePath: String, bitmapFilePath: String): Boolean{

            var pngFile = File(pngFilePath)

            //check if the png file exists:
            if(pngFile.exists()){

                //prepare variables:
                val png = ImageIO.read(pngFile)
                val  outputFile = File(bitmapFilePath)

                //convert:
                ImageIO.write(png, "bmp", outputFile)

                //check if the .bmp file has been created successfully:
                if(outputFile.exists() && outputFile.length() > 0){
                    return true
                }
            }
            return false
        }
    }

}