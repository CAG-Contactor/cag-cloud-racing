# JFokus Contestant UI

## Build

Build for production:

    $ npm run build:prod
    
This configures the application to access the API gateway at `backend.jfokus.caglabs.se`    

Build for test:

    $ npm run build:test
    
This configures the application to access the API gateway at `test.backend.jfokus.caglabs.se`    

Start dev server for a user specific backend:

    $ export REACT_APP_API_ENDPOINT=<your backend server>
    $ npm start
    
This configures the application to access the API gateway at `<your backend server>`.    

