# A recursive, memoized implementation of the series A111133

Implementation based on the series "Number of partitions of n into at least two distinct parts." http://oeis.org/A111133

Based on the solution from my brother's coding challenge he did, this repo is an optimization in Java for larger number computations due to the original codes rather large space complexity.

### Mathematical definition

The sum of partitions for ð equals to â(ð=2, sqrt(2ð))=ð(ð,ð). The upper bound of sqrt(2ð) is due to the fact that any set of ð numbers will have sum at least ð(ð+1)/2; therefore, there is no need to consider sets of size sqrt(2n) or larger.

Let ð(ð,ð)=ð(ðâð,ð)+ð(ðâð,ðâ1) be the recursive algorithm to calculate the series.

This recursion gives you an algorithm to compute ð(ð,ð) for all 2â¤ðâ¤sqrt(2n) via dynamic programming in ð(3/2ð) time.

### Java implementation

##### Recursive tree

Effectively, this algorithm constructs a binary tree of (ð,ð) values for each step-down. Due to the nature of ð(ðâð,ð) converging to 0, for >> numbers this results in a rather deep recursion. Due to this, a rather optimistic stack size is needed with ~20m of stack needed to fully recurse the algorithm for 300000.

##### Memoization

As stated above, memoization is the integral part of this implementation, with processing being in ð(3/2ð) time. With each subsequent iteration of ð, a cache matrix is updated with the ð=i; => ð(ð,ð) combinations.
The computed numbers as recursion is computed, grow exponentially, as it can be observed in the graph below.

<img width="550" alt="Screenshot 2023-02-01 at 21 29 55" src="https://user-images.githubusercontent.com/37189321/216156717-613d24fa-c36f-4d37-abfa-c329e329f05d.png">

For only n>150, the numbers grow exponential, making them not fit the 'long' primitive even as soon as for ~ n=800, introducing the need for dynamic integers such as BigInteger and Huldra's BigInt (https://github.com/bwakell/Huldra), which is used here with minimal changes for more optimised additions and space consumptions.

Effectively, we have an optimal compute time complexity, but the tradeof is the exponentially increasing space complexity. 
Java's util libraries for collection and number objects do not really do us a favor when the problem is space complexity (time has more priority than space), because of that, Huldra's BigInt and Carrotsearch's HPPC library for primitive-backed maps and small-footprint BigIntegers where used for the memoization data.

Even with this, the HeapSize tends to grow to GBs for larger numbers - due to the need to cache each and every (ð,ð) combination.
This is one of the type of computations that perfectly illustrate the notion - "for large data, each byte is important". 

<img width="393" alt="Screenshot 2023-02-01 at 21 40 52" src="https://user-images.githubusercontent.com/37189321/216158639-0796bf6e-d95c-4658-a01c-c267c8ef66b0.png"> <img width="600" alt="Screenshot 2023-02-01 at 21 40 59" src="https://user-images.githubusercontent.com/37189321/216158660-c82bbc7b-a124-4267-a768-a0054d7af1b1.png">

Examples above - space consumption for n=100k, which produces the following number:
`42494159403332317292526619504218136903700576932083624292980870857936616016516019121515022089648672327193383380680571759727227416036821183744674051457194041711141429085626371124196057902283995836976239181670821800000403741232325992196887134172549`

With numbers as these, cached for each (ð,ð), the space requirements grow exponentially. There should exist a (ð,ð)-eviction formula for subsequent ð  iterations, due to the nature the series converges to 0.

Of course, there are optimization mechanisms to handle both stack-size, heap-size and gc performance, but this is just an interesting small-tweaking-to-run thought-optimization experiment based on my brother's own-implementation of this algorithm for a coding challenge.


### Hot to Run
Requires:
- Java 8
- Maven (for Java 8)
I always recomend Jenv(with mvn plugin) for both maven and java.

Build `jar` with `mvn clean install`. 

Run from the source dir with `java -Xss${StackSize} -Xmx${HeapSize} -XX:+UseG1GC -jar target/untitled1-1.0-SNAPSHOT-jar-with-dependencies.jar n`.

### Example runs

 - n=30 (`java -XX:+UseG1GC -jar algo.jar 30`) execTime < 0s
 
 `295`
 
 - n=300 (`java -XX:+UseG1GC -jar algo.jar 300`) execTime < 0s
 
 `114872472063`
 
 - n=3k (`java -XX:+UseG1GC -jar algo.jar 3000`) execTime < 0s
 
 `6528822291254875226798745765109274957311`
 
 - n=30k (`java -Xss3m -Xmx500m -XX:+UseG1GC -jar algo.jar 30000`) execTime ~2s
 
 `2280495429685307410285127149614617420323397876626261673669342386904544041248139597528319024597884807009441686523360116682536825407999`
 
  - n=300k (for systems that can handle) (`java -Xss20m -Xmx28g -XX:+UseG1GC -jar algo.jar 300000`) execTime ~106s
 
 `4210862274208599320132457681361205631766332745531366097251182523840271778374041776996777544268557708791556972634240204931182275636709667458901634960056418648460211352341024814062465102985788332978802711358792090676627722144184552304071441127403307150949156714152542445860792515401696279114099266905863851501599751992437947170596759659111476740607340695732652666851170071652280194048602757002299800991344524743320843401058188959`
