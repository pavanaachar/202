@startuml
class A{
+a:int
-b:B
+sampleMethod(s:String):void
-anotherMethod(x:int,y:int):void
}
class B{
-k:ArrayList<A>
+hello():void
}
interface I{
}
class J{
+i:I
+method():void
}
class B--"*"class A
interface I..class J
interface I<|..class A
class J<|--class B
@enduml
