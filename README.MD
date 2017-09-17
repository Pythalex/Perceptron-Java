# Perceptron Java

------------

This is an example of implementation of the perceptron algorithm to classify datas.

---------

#### Perceptron for points

---------

Blue points are from a class 0, the green ones from the class 1 :

![alt](https://i.gyazo.com/4dc94614d3316054cfbd0b812c36eb72.png)

After program running, we get from the weights and bias : y = ax + b with a = -1.38 and b = 9.09

![alt](https://i.gyazo.com/d2e50bf88dc8f27299bede3c1225b37f.png)

Which works :) !
Be free to use this source code to understand where yours fail, or to send me criticism and advice to increase performance and/or effectiveness.

---------

#### Perceptron for characters

---------

The program uses a "Picture Decomposer" which basically just "cut" the images and creates vectors of [0-1] representing pixels white (1) or black (0), and adds the corresponding class at the end of the vector.

Training set of 400 hand written characters :

![alt](https://i.gyazo.com/8c9b5db2256d19d0b28c5e78a94f7fcb.png)

And a testing set of 36 similar hand written characters :

![alt](https://i.gyazo.com/dc476564c4c96e7c7a6a994d1347d47c.png)

With a success rate of 86% after training.


---------


#### Compilation

Just compile one folder, as the codes use their own main function.