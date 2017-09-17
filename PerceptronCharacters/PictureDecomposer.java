import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;
import java.lang.Math;


/** The picture decomposer decomposes a picture into a given number of subdivision
 *  and give them classes.
 */
public class PictureDecomposer {


    public PictureDecomposer(BufferedImage image){

        _image = image;

    }

    /**
     * Decomposes the square image into numberOfSubPicturePerRow^2 subdivisions
     * The picture have to be classified into "even order".
     *
     * example :
     *
     * |A|B|A|B|A|B| ...
     *
     * The argument classe defined the class (0 - 1) of the first element, the
     * next are classified with even order.
     *
     * @param numberOfSubPicturesPerRow int : the number of subdivision in one row
     * @param classe int : class of the first subdivision
     * @return Vector of vectors who contain the B/W pixels values and class as last element.
     */
    public Vector<Vector<Integer>> decompose(int numberOfSubPicturesPerRow, int classe){

        Vector<Vector<Integer>> vectors = new Vector<>(0); // Training set

        int nbPictures = numberOfSubPicturesPerRow * numberOfSubPicturesPerRow;

        int w = _image.getWidth();
        int h = _image.getHeight();
        int picW = w / numberOfSubPicturesPerRow; // assuming square picture
        int picH = picW;
        int picPixNb = picH * picW; // Number of pixels in sub picture

        int x, y, picClass;
        int pixel;

        // For each sub picture
        for (int i = 0; i < nbPictures; i++){

            // Gets its pixel values and store them
            Vector<Integer> pic = new Vector<>(picPixNb);

            // Gets each pixel value // each vector x_i
            for (int j = 0; j < picPixNb; j++){
                x = (i * picW) % w + j % picW;
                y = (i / numberOfSubPicturesPerRow) * picH + j / picW;
                pixel = _image.getRGB(x, y);
                if (pixel == -1) pixel = 1; // white
                else             pixel = 0; // black

                pic.add(pixel);
            }

            if (i % 2 == 0) picClass = classe; // a
            else            picClass = Math.abs(classe - 1); // b
            pic.add(picClass);

            vectors.add(pic);

        }

        return vectors;

    }

    private BufferedImage _image;

}
