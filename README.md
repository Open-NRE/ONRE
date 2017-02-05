# ONRE
Open Numerical Relation Extractrion

Examples:

India has population of 1.2 billion.
(India ; has population of ; 1.2 billion)

Microsoft has 10,000 employees.
(Microsoft ; has ; 10000 employees)
(Microsoft ; has [number of] employees ; 10000)

John is 6 feet tall.
(John ; has height of ; 6 feet)

Building and Running:

This is a sbt project, written in Java. Here are the basic building steps:

1. Download scala eclipse and install sbt. Read about sbt, it's a scala build tool(if you don't know already).
2. Clone the repo and import it in eclipse.
3. Run sbt eclipse, which should download all the required dependencies.
4. Make sure you have Wordnet downloaded parallel to the project.
5. There are 2 main files: 
     a) Onre_runMe.java - Takes in a single sentence, and gives numerical extractions from it.
     b) Onre_runMe_file.java - Takes an input file and an output file as arguments and gives extractions. Note that the input file must contain one sentence per line.

## Google Group

* [knowitall_openie](https://groups.google.com/forum/#!forum/knowitall_openie)
