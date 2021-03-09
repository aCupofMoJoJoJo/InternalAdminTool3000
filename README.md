# InternalAdminTool3000

**Installation and Running the Project**
1. Clone project
2. Open CMD at project
3. Package the project with the following command `mvn package`
4. Run the project with the following command `java -jar target/InternalAdminTool3000-1.0.0-SNAPSHOT.jar`

**How I spent my time:**
- [2/21/2021 Revision:](https://gist.github.com/jeffling/2dd661ff8398726883cff09839dc316c/d6ddd20109c4f3cbca2d161046f8c4b91fea0afd)
  - I read the requirements, viewed the APIs on the assignment page.
  - I set up a maven project, added my gitIgnore
  - Created a HttpClientWrapper to execute the APIs
  - Wrote tests
  - Created Boilerplate Pojos
  - Wrote tests
  - Created a Operation Class to handle the responses of HttpClientWrapper
  - Wrote tests
  - Created some more Boilerplate code
  - Wrote tests
  - Added documentation and looked for ways to make sure my tests were dry, also doublechecked my testing scenarios

- [3/8/2021 Revision (**Additional README and POM requirements added after original submission on Feb 21st.**)](https://gist.github.com/jeffling/2dd661ff8398726883cff09839dc316c/f70a6042bb2e51e978cc90c3f130787c18a13cf2)
  - Update pom to produce a jar file with maven
  - README update 

**My process:**
- I have a main driver class, then have multiple classes that the main driver class uses. 
- I used GSON to translate JSONs into objects. 
- I like designing classes to do one thing and one thing well. My DebtCalculator and my DateCalculator are an example of this. 
- My first goal is to get it to work, then I use my self-review to make things more efficient and more DRY

**What I could have done better with more time:**
- Error handling could had been better. I catch errors and print out the exception message but I think it should be more detailed and handled in a better way. If I had more time I would log tasks to clean this up. 
- I missed a few edge case scenarios when testing
- I would implement a front-end view vs command prompt... following a MVC pattern