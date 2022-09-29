JC = javac
.SUFFIXES: .java .class

default:
	$(JC) -g ./src/com/company/*.java -d ./out

run:
	java -classpath out com.company.Main

clean:
	$(RM) *.class
