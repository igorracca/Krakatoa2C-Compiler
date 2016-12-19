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
      int _A_k;
   } _class_A;

_class_A *new_A(void);

int _A_get_A( _class_A *this ) {
   return (this->_A_k);
}

void _A_init( _class_A *this ) {
   this->_A_k = 1;
}

Func VTclass_A[] = {
   ( void (*)() ) _A_get_A, 
   ( void (*)() ) _A_init
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
      int _A_k;
      int _B_k;
   } _class_B;

_class_B *new_B(void);

int _B_get_B( _class_B *this ) {
   return (this->_B_k);
}

void _B_init( _class_B *this ) {
   _A_init( (_class_A *) this);
   this->_B_k = 2;
}

Func VTclass_B[] = {
   ( void (*)() ) _A_get_A, 
   ( void (*)() ) _A_init, 
   ( void (*)() ) _B_get_B, 
   ( void (*)() ) _B_init
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
      int _A_k;
      int _B_k;
      int _C_k;
   } _class_C;

_class_C *new_C(void);

int _C_get_C( _class_C *this ) {
   return (this->_C_k);
}

void _C_init( _class_C *this ) {
   _B_init( (_class_B *) this);
   this->_C_k = 3;
}

Func VTclass_C[] = {
   ( void (*)() ) _A_get_A, 
   ( void (*)() ) _A_init, 
   ( void (*)() ) _B_get_B, 
   ( void (*)() ) _B_init, 
   ( void (*)() ) _C_get_C, 
   ( void (*)() ) _C_init
};

_class_C *new_C()
{
   _class_C *t;

   if ( (t = malloc(sizeof(_class_C))) != NULL )
      t->vt = VTclass_C;
   return t;
}

typedef
   struct _St_D {
      Func *vt;
      int _A_k;
      int _B_k;
      int _C_k;
      int _D_k;
   } _class_D;

_class_D *new_D(void);

int _D_get_D( _class_D *this ) {
   return (this->_D_k);
}

void _D_init( _class_D *this ) {
   _C_init( (_class_C *) this);
   this->_D_k = 4;
}

Func VTclass_D[] = {
   ( void (*)() ) _A_get_A, 
   ( void (*)() ) _A_init, 
   ( void (*)() ) _B_get_B, 
   ( void (*)() ) _B_init, 
   ( void (*)() ) _C_get_C, 
   ( void (*)() ) _C_init, 
   ( void (*)() ) _D_get_D, 
   ( void (*)() ) _D_init
};

_class_D *new_D()
{
   _class_D *t;

   if ( (t = malloc(sizeof(_class_D))) != NULL )
      t->vt = VTclass_D;
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
   _class_D *_d;
   puts("");
   printf("\n");
   puts("Ok-ger14");
   printf("\n");
   puts("The output should be :");
   printf("\n");
   puts("4 3 2 1");
   printf("\n");
   _d = new_D();
   ( (void (*)(_class_D *)) _d->vt[7] ) (_d);
   printf("%d ", ( (int (*)(_class_D *)) _d->vt[6] ) (_d));
   _c = _d;
   printf("%d ", ( (int (*)(_class_C *)) _c->vt[4] ) (_c));
   _b = _c;
   printf("%d ", ( (int (*)(_class_B *)) _b->vt[2] ) (_b));
   _a = _b;
   printf("%d ", ( (int (*)(_class_A *)) _a->vt[0] ) (_a));
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
