# Master-Worker-Pattern
A Java implementation of the Master/Worker Design Pattern using JMS and RMI. 

---

## Steps to use

1. Setup Glassfish.
   1. Install [Glassfish](http://glassfish.java.net/download.html).
   2. Run the command ```asadmin start-domain``` in the bin folder of Glassfish.
   3. Access the Glassfish console on [localhost](http://localhost:4848).
   4. Create a Connection Factory named jms/TestConnectionFactory, and Destination Resources named jms/MasterQueue, jms/WorkerQueue and jms/ResultQueue.

2. Create concrete implementations of Job, Master, and Worker. 
   1. ```doWork()``` in Job defines the work that needs to be done on each Worker Node. This is the method that will be executed on the Worker once it receives a task. 
   2. ```handleNewJob(job)``` in Master defines what happens when a Job object is received in the MasterQueue. This is the method that will be executed on the Master once it receives a new Job.
   3. ```handleResult(object)``` in Master defines what happens when a Job is executed on the Worker, and the result is placed on the ResultQueue. This is the method that will be executed on the Master once it receives a result on the ResultQueue.
   4. ```handleNewJob(newJob)``` in Worker defines what happens when a Job object is received in the WorkerQueue. This is the method that will be executed on the Worker once it receives a new Job. Most likely will consist of ```return newJob.doWork();```
   5. ```handleResult(object)``` in Worker defines what happens when a Job is executed on the Worker, and the result is available. This is the method that will be executed on the Worker once it a job has completed execution. Most likely will consist of ```this.addToResultQueue(result);```

3. Once concrete implementations are ready, start as many Masters and Workers, and everything should be working as expected. 
