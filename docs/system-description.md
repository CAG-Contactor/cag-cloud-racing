# CAG Cloud Racing
The new CAG Racing Manager.

The overall architecture is illustrated [here](https://drive.google.com/file/d/1tkuwic7LL2QKBTZxVNJYqREY1GGFSiT_/view?usp=sharing).

### Racing Manager
This is the backend that handles all [business logic](business-logic.md) in the system. 

The _Racing manager_ provides interfaces for the clients

- Interface towards laser detectors
- Interface towards UI for administrator
- Interface towards UI for contestant
- Interface towards UI for leader board and ongoing race

The proposed implementation using AWS is illustrated [here](https://drive.google.com/file/d/1enmsnM97tp0FsPzq2BGzctTi64KrAt9m/view?usp=sharing).

### AWS DynamoDB Tables
The following AWS DynamoDB tables are used

| Name | Columns | Comment |
|:---|:---|:---|
| registered-users | id, password, type| |
| races | id, createdTime, startTime, splitTime, finishTime, status, userId| All races in the system. The _userId_ is mandatory and refers to the owning user in the _registered-users_ table. |
| sessions | token, userId | All races in the system. The _userId_ is mandatory and refers to the owning user in the _registered-users_ table. |
| race-queue | raceId | The queue of pending races in the system. The _raceId_ refers to the race in the _races_ table. |
| current-race | raceId | The current active race in the system. The table either contains one or zero items. The _raceId_ refers to the race in the _races_ table. |

The leader board need no specific table. It is just the set of races sorted by the diff between _finishTime_ and _startTime_.

### UI for administrator
This UI provides the following features

- Arm, disarm and cancel race
- Add, remove and list contestants
- Presentation of health status of the _Racing manager_

### UI for leader board and ongoing race
This UI provides the following features

- Presentation of the top 10 contestants with result
- Presentation of queue of contestants
- Presentation of the ongoing race
    - Status (last finished, ready for start, ongoing)
    - Current time since start (active when race is ongoing)
    - Split time
    - Finish time
    
When a race is ongoing the status of the ongoing race is displayed.

When no race is ongoing the result of the last finished race is displayed together with 
the leader board alternated with the queue of participants

### UI for contestant
This UI provides the following features

- Registration
- Sign-in and sign-out
- Request to race
- Leader board
- My results

### Raspberry laser detector bridge
This component is connected to the actual photo diodes sensing the lasers in the gates for passage detection.

It is responsible for reporting the passages of gates to the _Racing Manager_.

## Scenarios
### Main scenario: a contestant performs a race
In order to participate in a race the person must register itself as a contestant in the system. 
The person does this in the _UI for contestant_. By doing this a contestant is added to the system.

After this the contestant signs in via _UI for contestant_ and is then able to request for participation in a race.
When the contestant performs such a request it is placed in a queue. 
The queue is displayed in the _UI for leader board_.  

In order to start a race the administrator request to arm the race (i.e make the race ready for start). 
However, this is only possible if a) no race is ongoing and b) there exist at least one contestant in the queue.

In the _UI for the administrator_, the administrator requests to arm the race. 
When this is done the contestant that is first in the queue is removed from the queue.
A race is created and is associated with the contestant just removed from the queue.
At this point there is a race ongoing.

After this the system waits for the passage of the start gate. 
When this happens the time stamp is saved.
When the split time gate is passed this fact is registered together with the time stamp for this event.

Finally when the finish gate is passed the finish time is registered, the race is moved to the leader baord and 
there is no longer an ongoing race (thus making it possible to arm the next race). 
  
#### Alternative scenario 1: Abort race
When the race is ongoing it is possible to abort it.
This is done via the _UI for the administrator_.
When doing this the race is moved to the set of aborted races and there will no longer be an ongoing race. 

#### Alternative scenario 2: Gates are passed when no race is ongoing
If a gate detection is registered passed when no race is ongoing, the passage is ignored and nothing happens.

#### Alternative scenario 2: Gates are passed in wrong order when race is ongoing
The gates must be passed in the following strict order
- Start gate
- Split time gate
- Finish gate

In case a gate passage is detected outside this order, the passage is ignored. For example if the start gate has been 
passed and after this a start gate passage is detected again, the second passage is ignored. 
Likewise, if the start gate has been passed and is then followed by passage if the finish gate, 
the finish gate passage is ignored, since no split time gate passage has been registered.

