import React, { useEffect, useState } from 'react'
import Moment from 'react-moment'
import backend, { Race, websocketSubject } from './backend'
import { filter } from 'rxjs/operators'

const LeaderBoard: React.FC<{ onChangeView?: () => void }> = (props) => {
  const {
    onChangeView = () => {
    }
  } = props
  const [leaderBoard, setLeaderBoard] = useState<Race[] | undefined>()
  const [error, setError] = useState<any>()
  const [loading, setLoading] = useState(false)
  useEffect(() => {
    setLoading(true)
    backend.getLeaderBoard()
      .then(lb => setLeaderBoard(lb))
      .catch(e => setError(e))
      .finally(() => setLoading(false))
  }, [])

  useEffect(() => {
      const sub = websocketSubject.asObservable()
        .pipe(filter(be =>
          be.type === 'RACE_ARMED'
        ))
        .subscribe(e => onChangeView())
      return () => {
        sub.unsubscribe()
      }
    }
  )

  return (
    <>
      {loading && <p className="loading-indicator">Laddar...</p>}
      {error && JSON.stringify(error)}
      {!loading &&
      <div className="leaderboard">
        <h1 className="text-center">Leaderboard</h1>
        <div className="w-100 d-flex justify-content-center">
          <table className="table table-striped table-dark">
            <thead>
            <tr>
              <th>Contestant</th>
              <th>Status</th>
              <th>Split time</th>
              <th>Finish time</th>
            </tr>
            </thead>
            <tbody>
            {leaderBoard && leaderBoard
              .sort((e1, e2) =>
                (e1.finishTime - e1.startTime) - (e2.finishTime - e2.startTime)
              )
              .map((entry, i) =>
                <tr key={i}>
                  <td>{entry.userName}</td>
                  <td>{entry.raceStatus}</td>
                  <td><Moment format="mm:ss:SSS">{entry.splitTime - entry.startTime}</Moment></td>
                  <td><Moment format="mm:ss:SSS">{entry.finishTime - entry.startTime}</Moment></td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
      }
    </>
  )
}

export default LeaderBoard
