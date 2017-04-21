@startuml
interface A1{
}
interface A2{
}
class B1{
}
class B2{
}
class C1{
+test(a1:A1):void
}
class C2{
+test(a2:A2):void
}
class P{
}
class C1"uses"..interface A1
class C2"uses"..interface A2
interface A1<|..class B2
interface A2<|..class B2
interface A1<|..class B1
class P<|--class B2
class P<|--class B1
@enduml
