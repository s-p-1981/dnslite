# TODO List

## Refactor Project
- ~~Overarching Maven project~~
- ~~ mvn module for library code ~~
  - ~~rename package~~
- add mvn module for authoritative server as submodule
- add mvn module for basic client as submodule

## Library Code
- ~~make DNSMessage, DNSQuestion, DNSAnswer immutable, only mutate Builders, then build~~
  - validate bytes when build() is called (OPCode Query matches QR flag, and others)
- allow to transform DNSMessage, DNSQuestion, DNSAnswer to respective Builder too
- ~~get DNSQuestion, DNSAnswer from bytes~~
- make DNSQuestion
- implement DNSQuestionPart as a view
- use factory method getQuestionPart (int questionIndex) - cache precomputed
- add classes for
  - Labels (maybe not)
  - Sections (maybe not)
  - Names
  - RR
  - RRSet
- provide iterators for the QuestionParts in a message
- provide iterators for other iterable elements of a DNSMessage (one RRSet, the Set of RRSets, the sections?)


## authoritative server
- in memory database of zones
  - programmable?
- send answers over network
- some api to update

## implement a client
  - send questions over network
  

### integration test question and question sender
- docker container with question sender
  - integration test: question sender in docker container, send questions to BIND9 Server
  