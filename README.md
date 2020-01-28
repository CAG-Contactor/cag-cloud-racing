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

The system is described in the [system description](docs/system-description.md).

## Build and deploy

Use the command `./build-and-deploy.sh -p <your AWS profile> <stage>`, e.g.:

    $ ./build-and-deploy.sh -p charlie test
    
## Addresses 

### Prod
[Contestant-UI](jfokus.caglabs.se)    
[Admin-UI](admin.jfokus.caglabs.se)    
[Race/Leaderboard-UI](leaderboard.jfokus.caglabs.se)    

### Test
[Contestant-UI](test.jfokus.caglabs.se)    
[Admin-UI](test.admin.jfokus.caglabs.se)    
[Race/Leaderboard-UI](test.leaderboard.jfokus.caglabs.se)    
