@startuml
class A{
-x:int
-y:int[]
-b:Collection<B>
-c:C
-d:Collection<D>
}
class B{
-a:A
}
class C{
-a:A
}
class D{
-a:A
}
class A--class C
class B--class A
class C--class A
class D--class A
@enduml
