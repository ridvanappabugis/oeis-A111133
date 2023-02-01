#include <iostream>
#include <cmath>
#include <chrono>
#include <boost/multiprecision/cpp_int.hpp>

boost::multiprecision::cpp_int F(std::uint_least32_t n, std::uint_least16_t k){

    if(k > n || n == 0 || k == 0) 
        return 0;
    if(k == 1 && n == 1)
        return 1;


    return F(n - k, k) + F(n-k, k-1);
}


int main(){

    std::uint_least32_t n;
    boost::multiprecision::cpp_int numOfCombinations = 0;  
    std::cin >> n;

    auto begin = std::chrono::high_resolution_clock::now();

    for(std::uint_least16_t i = 2; i <= (int)std::sqrt(2*n); i++)
        numOfCombinations += F(n, i);

    auto end = std::chrono::high_resolution_clock::now();
    auto elapsed = std::chrono::duration_cast<std::chrono::nanoseconds>(end - begin);

    std::cout << numOfCombinations;

    printf("\nTime measured: %.6f seconds.\n", elapsed.count() * 1e-9);

    std::cin >> n;
    
    return 0;

}