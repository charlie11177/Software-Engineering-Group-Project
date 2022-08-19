### University of Southampton
## Software Engineering Group Project - Runway Declaration

Commercial airports are busy places. Ideally the runway space should be completely used 100% of the time, but realistically this is not the case. This is due to 
obstructions on such as broken down airplanes or other vehicles. However parts of the runway can still be used with reduced or redefined distances for landing and
taking off. Each runway has a set of parameters which must be recalculated when an obstacle is present on the runway. A decision must then be made as to whether 
the new recalculated runway parameters are safe for limited operations to continue. Finally once these new parameters are published a final decision must be made 
by the pilot as to whether they feel safe landing/taking off.

### Finished Software
![Final Product](https://i.imgur.com/vmPmQck.png?raw=true)

### Authors
- Charles Gilbert
- Maram Al Yahyai
- James Maund
- Tomas Mrkva
- Velina Valcheva
- Isaac Whale

### Setup
1. Clone the project onto your machine using
```
git clone https://github.com/charlie11177/Software-Engineering-Group-Project.git
```
2. Ensure you have the right version of Java
```
~$ java --version
java 15.0.2 2021-01-19
```
3. Ensure you have the right version of Maven
```
~$ mvn -v
Apache Maven 3.8.3 (ff8e977a158738155dc465c6a97ffaf31982d739)
```
4. Perform a clean install whilst skipping tests (Certain UI tests are outdated and take a while)
```
~$ mvn clean install -DskipTests
```
5. Finally run the jar with dependencies within the target folder

### Scrum
This group project was carried out using the Scrum framework and consisted of three increments
