#!/usr/bin/python

from collections import defaultdict
import random
import argparse
import re

def getOriginals(wordlistfilename):
  originals = []
  with open(wordlistfilename) as f:
    originals = [w.strip() for w in f.readlines()]
  return originals

def getwordsbylen():
  wordsbylen = defaultdict(list)
  with open("british/brit-a-z.txt") as dictionary:
    for word in dictionary.readlines():
      trimmed = word.strip()
      if bool(re.search("[^a-z]", trimmed)):
        continue
      wordsbylen[len(trimmed)].append(trimmed)
  for wordlist in wordsbylen.values():
    random.shuffle(wordlist)
  return wordsbylen

def main():
  
  parser = argparse.ArgumentParser(description="Pad a wordlist with random words")
  parser.add_argument("wordlist")
  parser.add_argument("length", type=int)
  args = parser.parse_args()
 
  originals = getOriginals(args.wordlist)
  wordsbylen = getwordsbylen()

  lengths = defaultdict(int)
  for word in originals:
    lengths[len(word)] += 1

  padlength = args.length - len(originals)
 
  pad = []
  if args.length > len(originals):
    for length in lengths:
      many = int(padlength * (float(lengths[length]) / len(originals)))
      pad.extend(wordsbylen[length][:many])

  newlist = list(set(pad + originals))
  newlist.sort()
  newlist.sort(key=len)

  for word in newlist:
    print word

if __name__ == "__main__":
  main()
