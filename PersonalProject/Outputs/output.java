@startuml
class A{
+a:int
-b:B
+sampleMethod(s:String):void
-anotherMethod(x:int,y:int):void
}
class B{
-k:int
+hello():void
}
interface I{
}
class J{
+i:I
+method():void
}
class A--class B
interface I..class J
interface I<|..class A
class J<|--class B
@enduml
