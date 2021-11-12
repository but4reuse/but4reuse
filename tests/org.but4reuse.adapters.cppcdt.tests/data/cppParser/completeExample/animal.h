/*
 * animal.h
 *
 *  Created on: 24 juil. 2017
 *      Author: sandu.postaru
 */

#ifndef ANIMAL_H_
#define ANIMAL_H_
#include <iostream>

class Animal {

public:

	Animal(char const* name);

	virtual void sound() = 0;
	virtual void sleep() = 0;

protected:
	std::string m_name;

};

#endif /* ANIMAL_H_ */
