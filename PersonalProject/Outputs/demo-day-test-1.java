@startuml
class A{
-x:int
-y:int[]
}
class B{
}
class C{
}
class D{
}
class A "1" -- "*" class B
class A "1" -- "1" class C
class A "1" -- "*" class D
@enduml
