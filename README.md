# XWord

A fast brute-force crossword solver. 

## Usage

Takes an ascii representation of the crossword grid and a set of words as input. See resources/ for examples.

### Build

    ant build package

### Run

    java -jar dist/XWord-0.1.jar -v resources/guardian25580_grid.txt resources/guardian25580_wordlist_1000.txt
