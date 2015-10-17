This is a MapReduce implementation of the ID3 (Iterative Dichotomiser 3) Decision Tree algorithm.

This program accepts 3 user inputs:

1. The input path

2. The output path


Although I get the correct outputs from this algorithm, there are still a lot of wrongdoings in the code which I will be fixed in time.


The output of this algorithm is a simple string which represents a graph (sort of, since I did not get time to write the Graph API).
Every line starts with an integer which is the node and then the connected nodes as values.
At every line, outputs are separated by "|". If an output doesn't have a ";" separated value attached to it, then it is a node
which will be split and its contents are written in the next line.

I use a LinkedHashMap to store the graph. I used the LinkedHashMap to preserve the order of all the insertions in my map.
