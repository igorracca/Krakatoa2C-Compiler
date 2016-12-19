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
      int _A_j;
   } _class_A;

_class_A *new_A(void);

void _A_init_A( _class_A *this ) {
   this->_A_i = 1;
   this->_A_j = 2;
}

void _A_call_p( _class_A *this ) {
   _A_p(this);
}

void _A_call_q( _class_A *this ) {
   _A_q(this);
}

void _A_r( _class_A *this ) {
   printf("%d ", this->_A_i);
}

void _A_s( _class_A *this ) {
   printf("%d ", this->_A_j);
}

void _A_p( _class_A *this ) {
   printf("%d ", this->_A_i);
}

void _A_q( _class_A *this ) {
   printf("%d ", this->_A_j);
}

Func VTclass_A[] = {
   ( void (*)() ) _A_init_A, 
   ( void (*)() ) _A_call_p, 
   ( void (*)() ) _A_call_q, 
   ( void (*)() ) _A_r, 
   ( void (*)() ) _A_s
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
      int _A_j;
      int _B_i;
      int _B_j;
   } _class_B;

_class_B *new_B(void);

void _B_init_B( _class_B *this ) {
   this->_B_i = 3;
   this->_B_j = 4;
}

void _B_call_p( _class_B *this ) {
   _B_p(this);
}

void _B_call_q( _class_B *this ) {
   _B_q(this);
}

void _B_r( _class_B *this ) {
   printf("%d ", this->_B_i);
}

void _B_s( _class_B *this ) {
   printf("%d ", this->_B_j);
}

void _B_p( _class_B *this ) {
   printf("%d ", this->_B_i);
}

void _B_q( _class_B *this ) {
   printf("%d ", this->_B_j);
}

Func VTclass_B[] = {
   ( void (*)() ) _A_init_A, 
   ( void (*)() ) _A_call_p, 
   ( void (*)() ) _A_call_q, 
   ( void (*)() ) _A_r, 
   ( void (*)() ) _A_s, 
   ( void (*)() ) _B_init_B, 
   ( void (*)() ) _B_call_p, 
   ( void (*)() ) _B_call_q, 
   ( void (*)() ) _B_r, 
   ( void (*)() ) _B_s
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
      int _A_i;
      int _A_j;
      int _C_i;
      int _C_j;
   } _class_C;

_class_C *new_C(void);

void _C_init_C( _class_C *this ) {
   this->_C_i = 5;
   this->_C_j = 6;
}

void _C_call_p( _class_C *this ) {
   _C_p(this);
}

void _C_call_q( _class_C *this ) {
   _C_q(this);
}

void _C_r( _class_C *this ) {
   printf("%d ", this->_C_i);
}

void _C_s( _class_C *this ) {
   printf("%d ", this->_C_j);
}

void _C_p( _class_C *this ) {
   printf("%d ", this->_C_i);
}

void _C_q( _class_C *this ) {
   printf("%d ", this->_C_j);
}

Func VTclass_C[] = {
   ( void (*)() ) _A_init_A, 
   ( void (*)() ) _A_call_p, 
   ( void (*)() ) _A_call_q, 
   ( void (*)() ) _A_r, 
   ( void (*)() ) _A_s, 
   ( void (*)() ) _C_init_C, 
   ( void (*)() ) _C_call_p, 
   ( void (*)() ) _C_call_q, 
   ( void (*)() ) _C_r, 
   ( void (*)() ) _C_s
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
   puts("Ok-ger15");
   printf("\n");
   puts("The output should be :");
   printf("\n");
   puts("1 2 1 2 3 4 3 4 5 6 5 6");
   printf("\n");
   _a = new_A();
   ( (void (*)(_class_A *)) _a->vt[0] ) (_a);
   ( (void (*)(_class_A *)) _a->vt[1] ) (_a);
   ( (void (*)(_class_A *)) _a->vt[2] ) (_a);
   ( (void (*)(_class_A *)) _a->vt[3] ) (_a);
   ( (void (*)(_class_A *)) _a->vt[4] ) (_a);
   _b = new_B();
   ( (void (*)(_class_B *)) _b->vt[5] ) (_b);
   ( (void (*)(_class_B *)) _b->vt[9] ) (_b);
   ( (void (*)(_class_B *)) _b->vt[6] ) (_b);
   ( (void (*)(_class_B *)) _b->vt[7] ) (_b);
   ( (void (*)(_class_B *)) _b->vt[8] ) (_b);
   ( (void (*)(_class_B *)) _b->vt[9] ) (_b);
   _c = new_C();
   ( (void (*)(_class_C *)) _c->vt[5] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[9] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[5] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[6] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[7] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[8] ) (_c);
   ( (void (*)(_class_C *)) _c->vt[9] ) (_c);
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
