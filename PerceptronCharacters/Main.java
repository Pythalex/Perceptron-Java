/*

    Alexandre BONIN
 */

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Vector;
import java.lang.Math;


public class Main {

    /**
     * Prints elements of a vector.
     *
     * @param vector Vector<Float>
     */
    public static void printVectorF(Vector<Float> vector){
        for (float elm: vector)
            System.out.println(elm);
    }

    /**
     * Prints elements of a vector.
     *
     * @param vector Vector<Integer>
     */
    public static void printVectorI(Vector<Integer> vector){
        for (float elm: vector)
            System.out.println(elm);
    }


    /**
     *  Returns a buffered reader of the file whose filename
     *  has been given in parameter.
     *
     *  @param filename String : name of the file to get
     *  @return reader BufferedReader
     */
    public static BufferedReader getFile(String filename){

        BufferedReader reader = null;

        try {

            FileReader filereader = new FileReader(filename);

            reader = new BufferedReader(filereader);

        }
        catch(FileNotFoundException e){
            System.out.println("Unable to open file '" + filename + "'");
        }

        return reader;
    }


    /**
     *  Returns a Vector of points from the file given in parameter.
     *  The file must absolutely be of the form :
     *      0.151614;1.5344443\n
     *      1.55584;58.2564484\n
     *      .
     *      .
     *      121.1524;14.15454\n
     *
     *  @param file String : file where are stored points
     *  @return points Vector<Vector<Float>>
     */
    public static Vector<Vector<Float>> getPoints(BufferedReader file){

        Vector<Vector<Float>> points = new Vector<Vector<Float>>(0);
        String line = null;

        int semicolonIndex = 0;
        int oldSemiColonIndex = 0;
        float x1, x2, y;

        try {
            while ((line = file.readLine()) != null) {
                while (line.charAt(semicolonIndex) != ';')
                    semicolonIndex++;
                x1 = Float.parseFloat(line.substring(0, semicolonIndex));
                oldSemiColonIndex = semicolonIndex;
                do {
                    semicolonIndex++;
                } while (line.charAt(semicolonIndex) != ';');
                x2 = Float.parseFloat(line.substring(oldSemiColonIndex + 1, semicolonIndex));
                y = Float.parseFloat(line.substring(semicolonIndex + 1));

                Vector<Float> point = new Vector<>(3);
                point.add(0, x1);
                point.add(1, x2);
                point.add(2, y);

                points.add(point);

                semicolonIndex = 0;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return points;

    }


    /**
     *  Initialize a vector with value given in argument
     *
     *  @param vector Vector<Float> : Vector to initialize
     *  @param value  float : Value to use to initialize vector
     *  @param size int : desired size of the vector
     *  @return vector Vector<Float>
     */
    public static Vector<Float> initialize(Vector<Float> vector, float value, int size){
        vector = new Vector<>(size);
        for (int i = 0; i < size; i++) vector.add(i, value);
        return vector;
    }


    /**
     *  Returns 1 if weight * x >= -bias else 0
     *
     * @param weights  Vector<Float> : weights of activation function
     * @param x Vector<Float> : vector x
     * @param bias float
     * @return 1 or 0 int
     */
    public static int threshold( Vector<Float> weights, Vector<Integer> x, float bias){

        float result = 0;
        // scalar product
        for (int i = 0; i < weights.size(); i++) {
            result += weights.get(i) * x.get(i);
        }

        // Neuron threshold
        if (result >= -bias) return 1;
        else                 return 0;
    }


    /**
     * Adjusts weight and bias tofit to the training set
     *
     * @param trainingSet Vector<Vector<Float>> : set of (x, y)
     * @param weights Vector<Float>: weights of the activation function
     * @param bias float : w0 for the activation function
     * @param learningRate float : Weight of gradient descent
     * @return weightAndBias Vector<Float>
     */
    public static Vector<Float> perceptronLearning(Vector<Vector<Integer>> trainingSet, Vector<Float> weights,
                                           float bias, float learningRate){

        Vector<Float> weightAndBias = new Vector<>(3);

        boolean predictionMissed = false;

        int yPredicted;
        int y; // Classes of training set's points
        int update = 0;

        do { // while (predictionMissed)

            predictionMissed = false;

            // Predict class of each point of the training set
            for (Vector<Integer> point: trainingSet){
                y = point.get(point.size() - 1); // get 1 or 0 from file
                yPredicted = threshold(weights, point, bias); // predict from threshold function

                // If bad prediction
                if (yPredicted != y){
                    // Adjusts weight and bias
                    for (int i = 0; i < weights.size(); i++) {
                        weights.set(i, weights.get(i) + learningRate*(y - yPredicted)*point.get(i));
                    }
                    bias += learningRate*(y - yPredicted);
                    // one more while loop after all points
                    predictionMissed = true;

                    update++;
                }
            }

        } while (predictionMissed);


        // Returns result
        weights.add(bias);
        return weights;
    }

    /**
     * Tests weights and bias and returns the number of errors
     *
     * @param testSet Vector<Vector<Float>> : set of (x, y) for testing
     * @param weights Vector<Float>: weights of the activation function
     * @param bias float : w0 for the activation function
     * @return number of errors int
     */
    public static int perceptronTesting(Vector<Vector<Integer>> testSet, Vector<Float> weights,
                                                   float bias){

        Vector<Float> weightAndBias = new Vector<>(3);

        boolean predictionMissed = false;

        int yPredicted;
        int y; // Classes of training set's points
        int errors = 0;

        do { // while (predictionMissed)

            predictionMissed = false;

            // Predict class of each point of the training set
            for (Vector<Integer> point: testSet){
                y = point.get(point.size() - 1); // get 1 or 0 from file
                yPredicted = threshold(weights, point, bias); // predict from threshold function

                // If bad prediction
                if (yPredicted != y){
                    errors++;
                }
            }

        } while (predictionMissed);


        return errors;
    }

    public static void main(String[] args){

        float bias = 0f; // w0
        Vector<Float> weights = new Vector<>(400);
        float learningRate = 0.1f; // alpha
        Vector<Vector<Integer>> trainingSet;
        Vector<Vector<Integer>> testSet;

        BufferedImage image1;
        BufferedImage image2;

        try{
            image1 = ImageIO.read(new File("trainingSet.png"));
            image2 = ImageIO.read(new File("testSet.png"));
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }

        PictureDecomposer picdec = new PictureDecomposer(image1);
        trainingSet = picdec.decompose(20, 0);
        picdec = new PictureDecomposer(image2);
        testSet = picdec.decompose(6, 1);

        weights = initialize(weights, 0, 400);


        // Learning
        Vector<Float> weightsAndBias = perceptronLearning(trainingSet, weights, bias, learningRate);
        // Testing
        bias = weightsAndBias.get(weightsAndBias.size() - 1);
        weightsAndBias.remove(weightsAndBias.size() - 1);
        weights = weightsAndBias;
        int errors = perceptronTesting(testSet, weights, bias);

        System.out.print("Precision : " + Float.toString(((36 - new Float(errors)) / 36f * 100)) + "%\n");
    }

}
