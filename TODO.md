# TODO List

## Refactor Project
- ~~Overarching Maven project~~
- ~~ mvn module for library code ~~
  - ~~rename package~~
- add mvn module for authoritative server as submodule
- add mvn module for basic client as submodule

## basic client


## Library Code
- ~~make DNSMessage, DNSMessageQuestion, DNSMessageAnswer immutable, only mutate Builders, then build~~
  - validate bytes when build() is called (OPCode Query matches QR flag, and others)
- allow to transform DNSMessage to respective Builders (based on QR flag)
- ~~get DNSMessageQuestion, DNSMessageAnswer from bytes~~
- ~~implement DNSQuestion as a view~~
  - ~~scan for messages once, then use cached objects~~
  - ~~write more tests for DNSQuestion as view~~
  - provide iterator for questions
  - provide iterator for labels in question name
  - NEXT UP: add more DNS Types than A, AAAA
- implement adding DNSQuestion in the Builder
- ~~use factory method getQuestion (int questionIndex)~~
- (DONT KNOW) add classes for
  - Labels (maybe not? but i think "yes" - for iteration)
  - Names (maybe not? but i think "yes" - for iteration)
  - RR (definitely)
  - RRSet (definitely)

### various
- NEXT UP: add message text to ALL throw statements

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
  