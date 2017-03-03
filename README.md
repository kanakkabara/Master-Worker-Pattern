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

3. Once concrete implementations are ready, we are ready to run our Distributed platform. There are two ways to run:
    * Using RMI - To use RMI, we need to create an RMI server. This has been included as the ServerImplementation (which is an implementation of MasterServer). You need to simply create a ServerImplementation with a Master object and a String to bind the Server. Running runServer on this object will start an RMI service. (Note: Start RMI Registry before running ServerImplementation).
    
        Once the server is up, using the Naming lookup, we can get a remote reference to our MasterServer, create a Job object and run the Job using ```remoteHandleJob()```. A sample of this form of this use case is given in the Tester folder, with the names RMITest and ServerRunner. 
    
    * Using an input queue - Using the JMSHelper, we can get access to the MasterQueue, where we can simply push Job objects to the queue, then run ```waitFor(job)``` to get the result. This is the more hands-on approach of the two, and I'd recommend using the RMI to create a truly Distributed system.
