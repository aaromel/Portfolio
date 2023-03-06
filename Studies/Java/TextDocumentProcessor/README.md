This project is a program that processes text documents and stores the documents on a linked list. The text documents contain tags. For information retrieval purposes, insignificant words are provided in a so-called stop word list. The stop words are passed to the program in a text file, with one stop word per line.

The root directory includes .txt-files for the program. For example the file "jokes_oldies.txt" is a text document collection file and "stop_words.txt" is a file for stop word list.

To run the program, the files need to be compiled first. This can be done in command prompt using the command "javac *. java" in the root directory. After this the program can be run with the command "java Oope2HT jokes_oldies.txt stop_words.txt". The file names are passed to the program as command-line parameters, where the first parameter specifies the document collection file name and the second parameter specifies the stop word list.

After the program opens, it waits for user input. The user can give different commands to the program, which are listed below. If an invalid command is given, the program gives an error message.

Available commands:
- print
- add
- find
- remove
- reset
- echo
- quit

The "print" command prints all text documents line by line. The command can take a parameter for the document ID (e.g., "print 6"), in which case only the document with the specified ID is printed.

The "add" command adds a new document to the file. The document to be added is given as a string parameter, in the same format as in the file (e.g., add 13///computing...).

The "find" command searches for documents containing certain keys. The search keys are given as a parameter to the command. The command prints out, line by line, the IDs of the documents that contain all the keys given as parameters. IDs of documents that partially match the search are not printed. Lowercase and uppercase letters are considered different characters. For example, if the command is "find cat dog", both keys ("cat" and "dog") must appear in the document at least once for it to be considered a match.

The "remove" command removes a document from the collection. The parameter for the command is the ID of the document to be removed (e.g., "remove 11").

The "polish" command starts preprocessing the collection. The command works in stages, first removing the punctuation marks given as parameters. The punctuation marks to be removed are specified by the program in order (polish ,.:?"'). In the third stage, all occurrences of words in the stop word list are removed from the documents.

The "reset" command reloads the document file and removes all previous changes. The command does not take any parameters.

The "echo" command starts or stops command echoing. Echoing means that the program prints out the command it received. The command does not take any parameters.

The "quit" command exits the program. The command does not take any parameters.

Own code is located in files:
- dokumentit/Dokumentti.java
- dokumentit/Uutinen.java
- dokumentit/Vitsi.java
- kayttoliittyma/Kayttoliittyma.java
- kokoelma/Kokoelma.java
- omalista/OmaLista
- Oope2HT.java
