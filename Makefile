JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

# This uses the line continuation character (\) for readability
# You can list these all on a single line, separated by a space instead.
# If your version of make can't handle the leading tabs on each
# line, just remove them (these are also just added for readability).
CLASSES = \
	Window.java \
	Screen.java \
	LoginScreen.java \
	DBTable.java \
	DatabaseProfile.java \
	DatabaseCredentials.java \
	ColumnData.java \
	ColumnName.java \
	Database.java \
	HomeScreen.java \
	EmployeeScreen.java \
	CompanyScreen.java \
	ProfileScreen.java \
	OrderScreen.java \
	SearchScreen.java \
	InstructionScreen.java \
	CustTable.java \
	ScreenData.java \
	ScreenData_CompanyScreen.java \
	ScreenData_ProfileScreen.java \
	ScreenData_OrderScreen.java \
	ScreenData_SearchScreen.java \
	Tables.java \
	MainDatabaseApp.java

default: classes

classes: $(CLASSES:.java=.class)

run:
	java -cp ".:mysql-connector-j-8.0.32.jar" MainDatabaseApp

clean:
	$(RM) *.class

