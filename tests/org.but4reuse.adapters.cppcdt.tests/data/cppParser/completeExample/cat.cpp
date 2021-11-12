/*
 * dog.cpp
 *
 *  Created on: 24 juil. 2017
 *      Author: sandu.postaru
 */

#include "cat.h"
#include <iostream>
using namespace std;


Cat::Cat(char const* name): Animal(name){};

void Cat::sound(){
	cout << "Meow Meow" << endl;
}

void Cat::sleep(){
	cout << "Snore Meow Snore" << endl;
}



