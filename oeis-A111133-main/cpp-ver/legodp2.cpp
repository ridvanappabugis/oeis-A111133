#include <iostream>
#include <cmath>
#include <chrono>
#include <unordered_map>
#include <boost/multiprecision/cpp_int.hpp>
#include <boost/functional/hash.hpp>                   

std::unordered_map<std::uint_least32_t, std::unordered_map<std::uint_least16_t, boost::multiprecision::cpp_int>> cache;


boost::multiprecision::cpp_int F(std::uint_least32_t n, std::uint_least16_t k){
    
    if(k == 1 && n >= 1)
        return 1;

    if(k > n || n <= 0 || k <= 0) 
        return 0;

    {
    auto it = cache.find(n);
    if(it != cache.end()){
        auto it2 = it->second.find(k);
        if(it2 != it->second.end())
            return it2->second;
    }
    }

    return cache[n][k] = F(n - k, k) + F(n-k, k-1);
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