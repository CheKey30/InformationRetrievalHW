ReadMe
Shuche Liu(shl174@pitt.edu)

Invert index structure:
I used a hashmap to store the index, the key of this hashmap is the token, the value of this hashmap is several pairs including the document id which includes this token and the count of the token in that document.
For example, token “abc” shows up in "doc1" 3 times, "doc2" 2times, and "doc3" 4times. Then the map would be like:
“abc;1:3,2:2,3:4”
This map would be written into the TextIndex.txt or WebIndex.txt.
The mapping between docno and doc id would be saved into TextDocMap.txt or WebDocMap.txt which look like 
“1:list-000-000
2:list-000-001
3:list-000-002”

How to run the program:
In order to run this program, fist you should make sure that the "result.trectext" and "result.trecweb" are in the "data" folder. 
Then you can run the HW2Main.java to get the result. After running this program, two index files and two mapping files would be generated in the “data//indextext//” folder or "data//indexweb//" folder.

Java version:
I used JDK1.11 to do this project

Running time:
It tooks about 1.33 minutes to finish all processes.

Retrieval result:
>> the token "acow" appeared in 3 documents and 3 times in total
       lists-092-3952951    154963         1
       lists-092-4113429    154964         1
      lists-108-11347927    186006         1

>> the token "yhoo" appeared in 5 documents and 5 times in total
        NYT19990208.0397    291085         1
        NYT19990405.0253    313384         1
        NYT20000717.0201    477373         1
        NYT20000927.0406    502701         1
        NYT20000928.0343    503146         1

