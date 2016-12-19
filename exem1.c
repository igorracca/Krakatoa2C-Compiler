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

void _A_put( _class_A *this ) {
}

void _A_m2( _class_A *this ) {
   ( (void (*)(_class_A *)) this->vt[1] ) ( (_class_A *) this);
}

Func VTclass_A[] = {
   ( void (*)() ) _A_get, 
   ( void (*)() ) _A_put, 
   ( void (*)() ) _A_m2
};

_class_A *new_A()
{
   _class_A *t;

   if ( (t = malloc(sizeof(_class_A))) != NULL )
      t->vt = VTclass_A;
   return t;
}

typedef
   struct _St_B {
      Func *vt;
      int _A_i;
      int _B_lastInc;
   } _class_B;

_class_B *new_B(void);

void _B_m1( _class_B *this, int _a ) {
   puts("olar");
   _A_put( (_class_A *) this);
}

Func VTclass_B[] = {
   ( void (*)() ) _A_get, 
   ( void (*)() ) _A_put, 
   ( void (*)() ) _A_m2, 
   ( void (*)() ) _B_m1
};

_class_B *new_B()
{
   _class_B *t;

   if ( (t = malloc(sizeof(_class_B))) != NULL )
      t->vt = VTclass_B;
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
   ( (void (*)(_class_A *)) _a->vt[1] ) (_a);
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
