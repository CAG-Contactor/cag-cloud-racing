import React, { useEffect, useState } from 'react'
import CurrentRace from './CurrentRace'
import backend, { websocketSubject } from './backend'
import LeaderBoard from './LeaderBoard'

const App: React.FC = () => {
  const [view, setView] = useState<'LEADERBOARD' | 'CURRENTRACE'>('LEADERBOARD')
  const [channelState, setChannelState] = useState<string | undefined>()
  const [channelError, setChannelError] = useState<any>()

  useEffect(() => {
    const sub = websocketSubject.asObservable()
      .subscribe(e => {
        switch (e.type) {
          case 'CHANNEL_OPENED':
          case 'CHANNEL_CLOSED':
            setChannelError(undefined)
            setChannelState(e.type)
            break
          case 'CHANNEL_ERROR':
            setChannelState(undefined)
            setChannelError(e.error)
            break
          default:
          // do nothing
        }
      })
    return () => {
      sub.unsubscribe()
    }
  }, [channelState, channelError])


  useEffect(() => {
    backend.getCurrentRace()
      .then(r => setView('CURRENTRACE'))
      .catch((e) => setView('LEADERBOARD'))
  }, [])

  return (
    <div>
      <div className="channelstatus fixed-bottom">
        {channelState && <span>{channelState}</span>}
        {channelError && <span className="ml-2">WS error: {JSON.stringify(channelError)}</span>}
      </div>
      <div>
        {view === 'LEADERBOARD' && <LeaderBoard onChangeView={() => setView('CURRENTRACE')}/>}
        {view === 'CURRENTRACE' && <CurrentRace onChangeView={() => setView('LEADERBOARD')}/>}
      </div>
    </div>
  )
}

export default App
