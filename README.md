# Systems Engineering - Assignment #2 #

## Getting Started ##
In order to participate and start working on this assignment, you should do the following:
* Register on se-gitlab.inf.tu-dresden.de .
* Setup your account credentials (ssh-keys).
* **FORK** the SE2W 2020 Assignment2.
* Clone the repository on your local machine.

Having done the aforementioned, there are two options of working with the provided repository:
1. using the eclipse project file provided in the top-level directory or,
2. using your favorite editor coupled with the Dockerfile in the toplevel directory
Since option 1 is trivial, the below provided instructions are to help you work with the docker file.

    * Install docker in your machine
    * In the toplevel directory with the Dockefile, run 
        * `docker image build -t se_assignment:2 .`   
        the above command will build the image, download all the data needed for the assignment, run ant build command and then runAllExamples.
        * ``docker run --privileged --rm -it -v `realpath ./Source`:/Source se_assignment:2  bash``   
        will create a docker container which one could use to execute the code.   
        Ensure that the `./Source path` is where your code is.
        Enter the container by searching for it using `docker image ps` and then `docker attach 'image_id'`.   
        * After attachning, only execute `ant runSolution1` or, `ant runSolution2`  and/or `ant runAllSolutions` to test the solution you have developed.   
        The output of the tasks should inform you of success or failure of the given/your solution.

**Noteworthy Points**
1. Push your code to se-inf.gitlab whenever you make and commit changes.
2. We run/evaluate your solutions everyday and you will be sent a link by email showing your score based on the output of your program.
3. The tasks are described below

In order to complete the tasks below, please fill the gaps code wise in the src/solutions package. Note: You can use Eclipse or any favorite Java IDE to accomplish those tasks.

## Task #1 Page view frequency ##
### Problem Description ###
You are provided with an apache log showing links that have been accessed by clients.
The task is to create a MapReduce program that counts the total number of times a given url has been accessed.
If the url you get does not start with a valid hostname, you should prepend it with *http://localhost/* (see general notes).

**Expected Output:** URL → frequency

```
#!csv

http://localhost/tikiwiki-2.1/css/admin.css 7
http://localhost/tikiwiki-2.1/tiki-admin.php 308
…
```

### Task #2 Frequency of NYC taxi rides within a 1 hour window ###
###Task Description###
You have been provided csv data that describes taxi services in the city of Newyork.   
For each hourly period, or hour, of the day, determine the number of taxis in operation during the period.  
The hourly period is defined as the period between the start of an hour and the last second of that hour e.g. from 12.00.00 to 12.59.59.   
Some taxi services can span more than one hour period. In that case, consider the operation in each different time period as unique/different operation.  

**Expected Output:** timeslot/window → frequency

```
#!csv

1am 301935
1pm 548485
10am 504387
…
…
```
### General Notes ###
* Solutions must be turned in no later than **11:59pm AOE, 8th of Jan‘20!** No late days or other excuses.
* Commit & PUSH!!! to your bitbucket repository before the deadline. Don't forget the push.
* No team work. We check for plagarism and will let you fail if there is an indication given.
* Ask questions at [auditorium](https://auditorium.inf.tu-dresden.de) if there are any.


### Notes for Windows users ###
* You need to set the JAVA_HOME environment variable using the short 8.3 path notation: ```C:\PROGRA~1\Java\JDK18~1.0_1```
* Your project should reside in a folder structure/subfolder without spaces.
* Your project should not reside on a Windows share otherwise you get exceptions such as: ```org.apache.hadoop.io.nativeio.NativeIO$POSIX.chmod(NativeIO.java:241)```
