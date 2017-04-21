@startuml
class A{
+a:int
+sampleMethod(s:String):void
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
class A "2" -- "*" class B
class J "1" --  interface I
interface I<|..class A
class J<|--class B
@enduml
