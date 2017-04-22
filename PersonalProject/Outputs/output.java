@startuml
class A{
+num:int
-anotherMethod(x:int,y:int):void
}
class B{
+hello():void
}
interface I{
}
class J{
+method():void
}
class A "*" -- "1" class B
class J  -- "1" interface I
interface I<|..class A
class J<|--class B
@enduml
