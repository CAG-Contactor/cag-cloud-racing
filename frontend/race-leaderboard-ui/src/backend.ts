import { ReplaySubject } from 'rxjs'

const backendApiBaseUrl = process.env.REACT_APP_API_ENDPOINT || 'https://fh48ov3fpj.execute-api.eu-central-1.amazonaws.com/ccjobi'
const backendWsBaseUrl = process.env.REACT_WS_ENDPOINT || 'wss://e1d1f0mupa.execute-api.eu-central-1.amazonaws.com/ccjobi/'

export interface Race {
  userName: string
  raceStatus: RaceStatus
  startTime: number
  splitTime: number
  finishTime: number
  createTime: number
}

export const websocketSubject = new ReplaySubject<BackendEvent>(1)

export class BackendEventChannel {
  private websocket: WebSocket

  constructor() {
    console.log(`Setup websocket to: ${backendWsBaseUrl}`)
    this.websocket = new WebSocket(`${backendWsBaseUrl}`)
    this.websocket.onmessage = (messageEvent: MessageEvent) => {
      const backendEvent = extractBackendEvent(messageEvent)
      if (backendEvent) {
        websocketSubject.next(backendEvent)
      } else {
        console.info('Got event from backend via web socket:', messageEvent, ', but ignoring it')
      }
    }
    this.websocket.onopen = () => {
      websocketSubject.next({type: 'CHANNEL_OPENED'})
    }
    this.websocket.onclose = () => {
      websocketSubject.next({type: 'CHANNEL_CLOSED'})
    }
    this.websocket.onerror = (error: Event) => {
      websocketSubject.next({type: 'CHANNEL_ERROR', error})
    }
  }

  async getLeaderBoard(): Promise<Race[]> {
    return fetch(`${backendApiBaseUrl}/leader-board`)
      .then(r => r.json())
  }

  async getCurrentRace(): Promise<Race> {
    return fetch(`${backendApiBaseUrl}/current-race`)
      .then(r => r.json())
  }
}

export interface ChannelOpened {
  type: 'CHANNEL_OPENED'
}

export interface ChannelClosed {
  type: 'CHANNEL_CLOSED'
}

export interface ChannelError {
  type: 'CHANNEL_ERROR'
  error: any
}

export interface RaceArmed {
  type: 'RACE_ARMED'
  user: string
}

export interface RaceBailedOut {
  type: 'RACE_BAILED_OUT'
  user: string
}

export interface RaceStarted {
  type: 'RACE_STARTED'
  user: string
  startTime: number
}

export interface RaceAborted {
  type: 'RACE_ABORTED'
  user: string
}

export interface SplitTime {
  type: 'SPLIT_TIME'
  user: string
  splitTime: number
}

export interface RaceFinished {
  type: 'RACE_FINISHED'
  user: string
  finishTime: number
}

export type BackendEvent =
  ChannelOpened
  | ChannelClosed
  | ChannelError
  | RaceArmed
  | RaceBailedOut
  | RaceStarted
  | RaceAborted
  | RaceFinished
  | SplitTime

/*
 {"newImage":{"finishTime":{"N":"1578503997645"},"createTime":{"N":"1578503280735"},"startTime":{"N":"1578503706253"},"id":{"S":"638fa987-b677-435c-bbd7-5a42d0a3bd75"},"userName":{"S":"Kaka"},"raceStatus":{"S":"FINISHED"},"splitTime":{"N":"1578503960619"}},"oldImage":{"finishTime":{"N":"1578503997645"},"createTime":{"N":"1578503280735"},"startTime":{"N":"1578503706252"},"id":{"S":"638fa987-b677-435c-bbd7-5a42d0a3bd75"},"userName":{"S":"Kaka"},"raceStatus":{"S":"FINISHED"},"splitTime":{"N":"1578503960619"}}}
 */
export type RaceStatus = 'IDLE' | 'ARMED' | 'STARTED' | 'FINISHED' | 'ABORTED'

interface RacesTableItemImage {
  finishTime: { N: number },
  createTime: { N: number },
  startTime: { N: number },
  id: { S: string },
  userName: { S: string },
  raceStatus: { S: RaceStatus },
  splitTime: { N: number }
}

interface BackendMessage {
  oldImage: RacesTableItemImage
  newImage: RacesTableItemImage
}

function determineEventTypeFrom(oldImage: RacesTableItemImage, newImage: RacesTableItemImage): BackendEvent | undefined {
  if (!oldImage || !newImage) {
    return undefined
  }

  if (oldImage.raceStatus?.S === 'IDLE' && newImage.raceStatus?.S === 'ARMED') {
    return {type: 'RACE_ARMED', user: newImage.userName?.S}
  }

  if (oldImage.raceStatus?.S === 'ARMED' && newImage.raceStatus?.S === 'ABORTED') {
    return {type: 'RACE_BAILED_OUT', user: newImage.userName?.S}
  }

  if (oldImage.raceStatus?.S === 'ARMED' && newImage.raceStatus?.S === 'STARTED') {
    return {type: 'RACE_STARTED', user: newImage.userName?.S, startTime: +(newImage.startTime?.N)}
  }

  if (oldImage.raceStatus?.S === 'STARTED' && newImage.raceStatus?.S === 'ABORTED') {
    return {type: 'RACE_ABORTED', user: newImage.userName?.S}
  }

  if (oldImage.raceStatus?.S === 'STARTED' && (oldImage.splitTime?.N !== newImage.splitTime?.N)) {
    return {type: 'SPLIT_TIME', user: newImage.userName?.S, splitTime: +(newImage.splitTime?.N)}
  }

  if (oldImage.raceStatus?.S === 'STARTED' && newImage.raceStatus?.S === 'FINISHED') {
    return {type: 'RACE_FINISHED', user: newImage.userName?.S, finishTime: +(newImage.finishTime?.N)}
  }
}

function extractBackendEvent(messageEvent: MessageEvent): BackendEvent | undefined {

  if (messageEvent) {
    const data: any = JSON.parse(messageEvent.data)
    const oldImage: RacesTableItemImage = data.oldImage
    const newImage: RacesTableItemImage = data.newImage
    return determineEventTypeFrom(oldImage, newImage)
  }
  return undefined
}

const backend = new BackendEventChannel()
export default backend
