/*

    Alexandre BONIN
 */

import java.io.*;
import java.util.Vector;
import java.lang.Math;

public class Main {

    /**
     * Prints elements of a vector.
     *
     * @param vector Vector<Float>
     */
    public static void printVector(Vector<Float> vector){
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
    public static int threshold( Vector<Float> weights, Vector<Float> x, float bias){

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
    public static Vector<Float> perceptron(Vector<Vector<Float>> trainingSet, Vector<Float> weights,
                                           float bias, float learningRate){

        System.out.println("Perceptron algorithm begins");

        Vector<Float> weightAndBias = new Vector<>(3);

        boolean predictionMissed = false;

        int yPredicted;
        int y; // Classes of training set's points
        int update = 0;

        do { // while (predictionMissed)

            predictionMissed = false;

            // Predict class of each point of the training set
            for (Vector<Float> point: trainingSet){
                y = Math.round(point.get(2)); // get 1 or 0 from file
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
                    System.out.println("w1 = " + Float.toString(weights.get(0)) + " , w2 = "
                            + Float.toString(weights.get(1)) + " , bias = " + Float.toString(bias)
                            + " , update = " + Integer.toString(update));
                }
            }

        } while (predictionMissed);


        // Returns result
        weightAndBias.add(0, weights.get(0)); // w1
        weightAndBias.add(1, weights.get(1)); // w2
        weightAndBias.add(2, bias);           // w0
        return weightAndBias;
    }

    public static void main(String[] args){

        BufferedReader reader = getFile("out/production/PerceptronJava/points.txt");

        Vector<Vector<Float>> trainingSet = getPoints(reader); // Perceptron training set

        float bias = 0f; // w0
        Vector<Float> weights = new Vector<>(2);
        weights = initialize(weights, 0, 2);
        float learningRate = 0.1f; // alpha

        Vector<Float> weightAndBias = perceptron(trainingSet, weights, bias, learningRate);

        printVector(weightAndBias);

    }

}
