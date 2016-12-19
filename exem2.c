#include <malloc.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

typedef int boolean;
#define true 1
#define false 0

typedef
   void (*Func)();

typedef
   struct _St_A {
      Func *vt;
      int _A_i;
   } _class_A;

_class_A *new_A(void);

int _A_get( _class_A *this ) {
   return (this->_A_i);
}

void _A_put( _class_A *this, int _p_i ) {
   this->_A_i = _p_i;
}

Func VTclass_A[] = {
   ( void (*)() ) _A_get, 
   ( void (*)() ) _A_put
};

_class_A *new_A()
{
   _class_A *t;

   if ( (t = malloc(sizeof(_class_A))) != NULL )
      t->vt = VTclass_A;
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run( _class_Program *this ) {
   _class_A *_a;
   int _k;
   _a = new_A();
   ( (void (*)(_class_A *, int)) _a->vt[1] ) (_a, 5);
   _k = ( (int (*)(_class_A *)) _a->vt[0] ) (_a);
   printf("%d ", _k);
}

Func VTclass_Program[] = {
   ( void (*)() ) _Program_run
};

_class_Program *new_Program()
{
   _class_Program *t;

   if ( (t = malloc(sizeof(_class_Program))) != NULL )
      t->vt = VTclass_Program;
   return t;
}

int main() {
   _class_Program *program;
   program = new_Program();

   ( ( void (*)(_class_Program *) ) program->vt[0] )(program);
   return 0;
}
