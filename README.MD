## Quantexa Transaction Assignment

### application information
1) Scala Version: 2.12.1
2) Sbt: as package tool
3) Spark: 2.4.5
4) Dependency include:  
"org.apache.spark" %% "spark-core" % "2.4.5"   
"org.apache.spark" %% "spark-streaming" % "2.4.5" % "provided"  
"org.apache.spark" %% "spark-sql" % "2.4.5"  

### solution
1) For question 1 and question 2 and each of them I have provided two solution  
  a. use scala to calculate
  b. use spark DataFrame to calculate
    
2) For question 3, I have provided three solutions
  a. using scala to calculate via constructing multiple DTOs
  b. using scala to maintain a result map as database, which is more effective from my own understanding
  By each entry flow in, I choose to update the result map accordingly
  For example: transaction day-1 of account id-A1 record, will contribute to the transaction day (2 to 6)
  c. using spark to do the calculation
  
### Finally
1) Thank you very much for your time and I sincerely enjoy the chatting with all of you.
2) I really appreciate that I can have this opportunity to learn more about the functional programming 
   and more then happy to learn more from Quantexa to see what is the best practise in this industry.
3) For the sources code I will upload them into my private github, please do ask me for the access.
  
4) In terms of Spark, it might need to change the filepath inorder to run it properly(in local machine).
5) The reason why I try to provide multiple solution is to illustrate how I learn the new things  
I usually start from the basic and the try to solve the problem in other ways. 
Although, I am entirely new to scala and spark, which you can tell from how I write the code,  
but I am definitely willing to learn, able to learn quickly and more important want feedbacks that help me improve!


### Thanks again!
Jinglei Zhu          