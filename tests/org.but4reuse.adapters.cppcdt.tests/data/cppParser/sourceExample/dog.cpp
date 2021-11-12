/*
 * dog.cpp
 *
 *  Created on: 24 juil. 2017
 *      Author: sandu.postaru
 */

#include "dog.h"
#include <iostream>
using namespace std;


Dog::Dog(char const* name): Animal(name){};

void Dog::sound(){
	cout << "Woof Woof" << endl;
}

void Dog::sleep(){
	cout << "Snore Woof Snore" << endl;
}
