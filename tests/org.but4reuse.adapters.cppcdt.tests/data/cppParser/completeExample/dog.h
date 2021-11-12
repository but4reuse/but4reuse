/*
 * dog.h
 *
 *  Created on: 24 juil. 2017
 *      Author: sandu.postaru
 */

#ifndef DOG_H_
#define DOG_H_
#include "animal.h"

class Dog: public Animal {

public:

	Dog(char const* name);

	void sound();
	void sleep();

};

#endif /* DOG_H_ */
