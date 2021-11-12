/*
 * dog.h
 *
 *  Created on: 24 juil. 2017
 *      Author: sandu.postaru
 */

#ifndef CAT_H_
#define CAT_H_
#include "animal.h"

class Cat: public Animal {

public:

	Cat(char const* name);

	void sound();
	void sleep();

};

#endif /* CAT_H_ */
