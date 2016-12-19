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

void _A_m( _class_A *this ) {
   int _a;
   int _b;
   int _c;
   int _d;
   int _e;
   int _f;
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_a);
   }
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_b);
   }
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_c);
   }
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_d);
   }
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_e);
   }
   {
      char __s[512];
      gets(__s);
      sscanf(__s, "%d", &_f);
   }
   printf("%d %d %d %d %d %d ", _a, _b, _c, _d, _e, _f);
}

Func VTclass_A[] = {
   ( void (*)() ) _A_m
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
   puts("");
   printf("\n");
   puts("Ok-ger05");
   printf("\n");
   puts("The output should be what you give as input.");
   printf("\n");
   puts("Type in six numbers");
   printf("\n");
   _a = new_A();
   ( (void (*)(_class_A *)) _a->vt[0] ) (_a);
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
