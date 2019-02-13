The File guitarwaves.db is a SqLite databse which contains -170 to 180 degrees of guitarsound in 10 degeree steps.

Samplingrate is 48000.

Every signal is saved in one table just for this.

Table naming convetion is like minus170, minus20, zero, plus10, plus180, etc.

Table format ist like:

table minus170{
	sampleNr INTEGER PRIMARY KEY,
	left REAL,
	right REAL,
}