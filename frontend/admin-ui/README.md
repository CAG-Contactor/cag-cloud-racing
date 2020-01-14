# AdminUi

## Build

Build for production:

    $ npm run build:prod
    
This configures the application to access the API gateway at `backend.jfokus.caglabs.se`    

Build for test:

    $ npm run build:test
    
This configures the application to access the API gateway at `test.backend.jfokus.caglabs.se`    

Start dev server for a user specific backend:

    $ export API_ENDPOINT=<your API server>
    $ export WS_ENDPOINT=<your web socket server>
    $ npm run start:local-dev
    
This configures the application to access the 
- API gateway at `<your API server>`.    
- web socket at `<your web socket server>`.    
    
  
