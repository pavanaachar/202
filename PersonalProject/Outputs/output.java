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
class A--class B
class B--"*"class A
interface I..class J
interface I<|..class A
class J<|--class B
@enduml
