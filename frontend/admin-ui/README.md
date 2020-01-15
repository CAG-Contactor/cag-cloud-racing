# AdminUi

## Build

Build for production:

    $ npm run build:prod
    
This configures the application to access the 
- API gateway at `https://backend.jfokus.caglabs.se`    
- web socket at `wss://websocket.jfokus.caglabs.se`    

Build for test:

    $ npm run build:test
    
This configures the application to access the
- API gateway at `https://test.backend.jfokus.caglabs.se`    
- web socket gateway at `wss://test.websocket.jfokus.caglabs.se`    

For local development where backend is your own AWS stack, 
start dev server for a user specific backend:

    $ export API_ENDPOINT=<your API server>
    $ export WS_ENDPOINT=<your web socket server>
    $ npm run start:local-dev
    
This configures the application to access the 
- API gateway at `<your API server>`.    
- web socket at `<your web socket server>`.    
    
The name of the API gateway you find under in the AWS web page under Services and 
API Gateway and the API with name  "cloud-racing-api-XX", where XX is your domain. 

Then you select "Stages" in the list to the left and then you select your domain 
name under "Stages". Click on any message under "current-race" and the API endpoint 
is shown under as "Invoke URL. Exlcude the last part ("current-race/")

The name of the Web Socket server is found in a similar way by browsing to the end 
of the API list and look for an API with name "race-websocket-<domain>".  

Select your API and "Stages" in the left list and your domain in a similar way 
as described above. The  Web socket server is shown as: "WebSocket URL"

