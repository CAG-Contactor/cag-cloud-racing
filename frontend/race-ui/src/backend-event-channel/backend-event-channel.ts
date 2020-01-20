import {
  backendEventChannelGotError,
  backendEventChannelHasClosed,
  backendEventChannelHasOpened,
  backendEventChannelReceivedMessage
} from './backend-event-channel.actions'
import { Store } from 'redux'
import { BackendEvent } from './event'


export class BackendEventChannel {
  private websocket: WebSocket;

  constructor(private readonly store: Store, clientApi: string = 'wss://e1d1f0mupa.execute-api.eu-central-1.amazonaws.com/ccjobi/') {
    console.log(`Setup websocket to: ${clientApi}`)
    this.websocket = new WebSocket(`${clientApi}`);
    this.websocket.onmessage = (messageEvent: MessageEvent) => {
      const backendEvent = extractBackendEvent(messageEvent);
      if (!!backendEvent) {
        this.store.dispatch(backendEventChannelReceivedMessage(JSON.parse(messageEvent.data) as BackendEvent));
      } else {
        console.error('Unknown event received from backend via web socket:', messageEvent);
      }
    };
    this.websocket.onopen = () => this.store.dispatch(backendEventChannelHasOpened());
    this.websocket.onclose = () => this.store.dispatch(backendEventChannelHasClosed());
    this.websocket.onerror = (error: Event) => this.store.dispatch(backendEventChannelGotError(error));
  }
}

function extractBackendEvent(messageEvent: MessageEvent): BackendEvent | undefined {
  if (!!messageEvent) {
    const data: any = JSON.parse(messageEvent.data);
    if (!!data.eventType) {
      return data as BackendEvent;
    }
  }
  return undefined;
}
