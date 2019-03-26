# Chord original paper Java implementation
## *Developed by Giuseppe Severino and Marco Mussi*
### Project for the Distributed System Course at Politecnico di Milano
### Academic Year 2018/2019

## **How to run it**
In order to run the project execute the Client class.
Once started is possible to create a new Chord network (command `CREATE`), join an existing network (command `JOIN`) and search for the node containing an item (command `SEARCH`).
Every node require a new process and a node is mantained active since the command `DELETENODE` is typed or the process hosting it is killed.
Once a node is active is possible to see the real time finger table by using the command `INFO`.