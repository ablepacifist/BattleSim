# Define directories
SRC_DIR = C:/Users/Owner/Documents/DnD/warGame/src
BIN_DIR = C:/Users/Owner/Documents/DnD/warGame/bin
CONFIG_DIR = C:/Users/Owner/Documents/DnD/warGame/config
DATA_DIR = C:/Users/Owner/Documents/DnD/warGame/data
DIST_DIR = C:/Users/Owner/Documents/DnD/warGame/dist

# Define the main class
MAIN_CLASS = WarGame

# Define the source files
SRC_FILES = $(wildcard $(SRC_DIR)/*.java)

# Define the class files
CLASS_FILES = $(SRC_FILES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target
all: $(CLASS_FILES)

# Rule to compile .java files to .class files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	javac -d $(BIN_DIR) $(SRC_FILES)

# Rule to create JAR file
jar: $(CLASS_FILES)
	jar cfm $(DIST_DIR)/WarGame.jar $(CONFIG_DIR)/MANIFEST.MF -C $(BIN_DIR) .

# Rule to run the batch file
run: jar
	cd $(DIST_DIR) && run.bat

# Clean up the class files and JAR file
clean:
	del /Q $(BIN_DIR)/*.class $(DIST_DIR)/WarGame.jar

.PHONY: all jar run clean