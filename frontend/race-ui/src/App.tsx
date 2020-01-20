import * as React from 'react'
import './App.css'
import { BackendEventChannel } from './backend-event-channel/backend-event-channel'
import AppContextWithMainPage from './main-page/main-page'


// --- Component
interface AppProps {
  websocket: BackendEventChannel;
}

const App = (props: AppProps) => {
  const {websocket} = props;
  return (
    <div>
        <div>
          <AppContextWithMainPage websocket={websocket}/>
        </div>
    </div>
  );
};

export default App;
