# mimir
Mimir is a set of tools for managing bibliographies and supporting SLRs

## BibliographyComparator
Main class: `com.ratelware.science.bibliography.apps.BibliographyComparator`

### Command line arguments:

 - `--inDir` - directory to load publications from
 - `--outDir` - directory to put segregated publications to
 - `--csvFormat` - flat to enable writing CSV output
 - `--bibtexFormat` - flag to enable writing BibTeX output
 
 each of outputs is put into its own directory, both may be enabled at the same time. 
 
### How does it work?

All files in `--inDir` are loaded as BibTeX files. Then, for each publication the tool verifies 
in which of the files is it present. By default "same publication" means same DOI or, if DOI is not available, 
same title, same sequence of authors and same volume. Right now it's not configurable from command line, only from code.

In output directory, for each format a new directory is created. In this directory, a new file is created for each set
of source files that have at least one common element. Common elements are put into files.
Right now publication that is being printed out is any of the versions (it's possible that they differ, e.g. one of the files
contains abstracts or something like that).

For example, if there are 3 files: A (containing P1, P2 and P3), B (containing P1, P3 and P5) and C (containing P1, P2, P3, P4 and P5),
files created will be:

 - `A_B_C_2` (containing P1 and P3)
 - `A_C_1` (containing P2)
 - `B_C_1` (containing P5)
 - `C_1` (containing P4)

number in the end of file name is number of publications in the file. 

Additionally a CSV with all publications with headers is created in `--inDir`.
Written fields are journal, DOI, list of authors, title and abstract

If any stage fails, whole application crashes. No reasonable exception handling is in place.
 
## What do I need?

### To run

 - Java 8
 - Command line

### To compile

 - SBT 1.0.3 or higher
 - Internet connection (dump of Maven Central is sufficent)

##Compilation

`sbt package` should do. For an uberjar use `sbt assembly`. Published releases are uberjars.
