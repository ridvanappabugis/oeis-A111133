# A recursive, memoized implementation of the series A111133

Implementation based on the series "Number of partitions of n into at least two distinct parts." http://oeis.org/A111133

### Mathematical definition

Let 𝑓(𝑛,𝑘)
 be the number of sets of 𝑘
 natural numbers summing to 𝑛
. You want to find ∑2𝑛√𝑘=2𝑓(𝑛,𝑘)
. The reason for the 2𝑛‾‾‾√
 in the upper bound is that any set of 𝑘
 numbers will have sum at least 𝑘(𝑘+1)/2
; therefore, there is no need to consider sets of size 2𝑛‾‾‾√
 or larger.

I claim that
𝑓(𝑛,𝑘)=𝑓(𝑛−𝑘,𝑘)+𝑓(𝑛−𝑘,𝑘−1)
To see this, let 𝑥𝑘>𝑥𝑘−1>⋯>𝑥1
 be 𝑘
 numbers summing to 𝑛
. If 𝑥1>1
, then
𝑥𝑘−1,𝑥𝑘−1−1,…,𝑥2−1,𝑥1−1
is a set of 𝑘
 numbers summing to 𝑛−𝑘
. If 𝑥1=1
, then the above list with 𝑥1−1
 removed is a list of 𝑘−1
 numbers summing to 𝑛−𝑘
.

This recursion gives you an algorithm to compute 𝑓(𝑛,𝑘)
 for all 2≤𝑘≤2𝑛‾‾‾√
 via dynamic programming using 𝑂(𝑛3/2)
 additions.
