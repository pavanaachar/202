@startuml
class ConcreteObserver{
#observerState:String
#subject:ConcreteSubject
+update():void
+showState():void
}
class ConcreteSubject{
-subjectState:String
-observers = new ArrayList<Observer>():ArrayList<Observer>
+getState():String
+setState(status:String):void
+attach(obj:Observer):void
+detach(obj:Observer):void
+notifyObservers():void
+showState():void
}
class Main{
+main(args:String[]):void
}
interface Observer{
+update():void
}
class Optimist{
+update():void
}
class Pessimist{
+update():void
}
interface Subject{
+attach(obj:Observer):void
+detach(obj:Observer):void
+notifyObservers():void
}
class TheEconomy{
}
class ConcreteObserver--class ConcreteSubject
interface Subject<|..class ConcreteSubject
interface Observer<|..class ConcreteObserver
class ConcreteObserver<|--class Optimist
class ConcreteSubject<|--class TheEconomy
class ConcreteObserver<|--class Pessimist
@enduml
