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
   } _class_A;

_class_A *new_A(void);

void _A_m1( _class_A *this ) {
   printf("%d ", 1);
}

void _A_m2( _class_A *this, int _n ) {
   printf("%d ", _n);
}

Func VTclass_A[] = {
   ( void (*)() ) _A_m1, 
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
   } _class_B;

_class_B *new_B(void);

void _B_m2( _class_B *this, int _n ) {
   printf("%d ", _n);
   _A_m2( (_class_A *) this, _n + 1);
}

Func VTclass_B[] = {
   ( void (*)() ) _A_m1, 
   ( void (*)() ) _A_m2, 
   ( void (*)() ) _B_m2
};

_class_B *new_B()
{
   _class_B *t;

   if ( (t = malloc(sizeof(_class_B))) != NULL )
      t->vt = VTclass_B;
   return t;
}

typedef
   struct _St_C {
      Func *vt;
   } _class_C;

_class_C *new_C(void);

void _C_m1( _class_C *this ) {
   _A_m1( (_class_B *) this);
   printf("%d ", 2);
}

void _C_m3( _class_C *this ) {
   ( (void (*)(_class_C *)) this->vt[3] ) ( (_class_C *) this);
   printf("%d ", 1);
   printf("%d ", 2);
}

Func VTclass_C[] = {
   ( void (*)() ) _A_m1, 
   ( void (*)() ) _A_m2, 
   ( void (*)() ) _B_m2, 
   ( void (*)() ) _C_m1, 
   ( void (*)() ) _C_m3
};

_class_C *new_C()
{
   _class_C *t;

   if ( (t = malloc(sizeof(_class_C))) != NULL )
      t->vt = VTclass_C;
   return t;
}

typedef
   struct _St_Program {
      Func *vt;
   } _class_Program;

_class_Program *new_Program(void);

void _Program_run( _class_Program *this ) {
   _class_A *_a;
   _class_B *_b;
   _class_C *_c;
   puts("");
   printf("\n");
   puts("Ok-ger12");
   printf("\n");
   puts("The output should be :");
   printf("\n");
   puts("1 2 1 2 1 2 1 2");
   printf("\n");
   _b = new_B();
   ( (void (*)(_class_B *, int)) _b->vt[2] ) (_b, 1);
   _c = new_C();
   ( (void (*)(_class_C *)) _c->vt[3] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[4] ) (_c);
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
