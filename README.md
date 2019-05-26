# CAG Cloud Racing
The new CAG Racing Manager.

## Summary
The CAG Racing Manager is a system for handling racing games which are based on three events that are timed: start, split, finish. 

The system has the following main features:

- User registration and login
- Keeping track of ongoing race
  - Arming
  - Time keeping
  - Completion
- Leader board

The overall architecture is illustrated [here](https://drive.google.com/file/d/1tkuwic7LL2QKBTZxVNJYqREY1GGFSiT_/view?usp=sharing).

## Racing Manager
This is the backend that handles all functions in the system. 
It is based on a number of micro services for different function areas and the division is guided by [Domain Driven Design](https://en.wikipedia.org/wiki/Domain-driven_design) principles, each micro service handling a specific aggregate.

The aggregates are

- Management of contestants
- Management of leader board
- Management of ongoing race

In addition to this the _Racing manager_ provides interfaces for the clients

- Interface towards laser detectors
- Interface towards UI for administrator
- Interface towards UI for contestant
- Interface towards UI for leader board and ongoing race

## UI for administrator
This UI provides the following features

- Arm, disarm and cancel race
- Add, remove and list contestants
- Presentation of health status of the _Racing manager_

## UI for leader board and ongoing race
This UI provides the following features

- Presentation of the top 10 contestants with result
- Presentation of the ongoing race
    - Status (last finished, ready for start, ongoing)
    - Current time since start (active when race is ongoing)
    - Split time
    - Finish time

## UI for contestant
This UI provides the following features

- Registration
- Sign-in and sign-out
- Request to race
- Leader board
- My results
