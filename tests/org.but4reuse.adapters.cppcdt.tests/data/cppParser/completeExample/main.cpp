//============================================================================
// Name        : C++ adapter testing example
// Author      : sandu.postaru
//============================================================================

#include <iostream>
#include "dog.h"
#include "animal.h"
#include "cat.h"

using namespace std;

int main() {

	Animal *dog = new Dog("Marcel");

	dog->sound();
	dog->sleep();

	Animal *cat = new Cat("Toto");
	cat->sound();
	cat->sleep();

	return 0;
}
