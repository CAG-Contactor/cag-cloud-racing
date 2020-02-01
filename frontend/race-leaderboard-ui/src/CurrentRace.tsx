import React, { useEffect, useState } from 'react'
import backend, { websocketSubject } from './backend'
import Moment from 'react-moment'

const CurrentRace: React.FC<{ onChangeView?: () => void }> = (props) => {
  const {
    onChangeView = () => {
    }
  } = props

  const [raceStatus, setRaceStatus] = useState<{ status: string, user: string } | undefined>()
  const [startTime, setStartTime] = useState<number | undefined>()
  const [splitTime, setSplitTime] = useState<number | undefined>()
  const [finishTime, setFinishTime] = useState<number | undefined>()
  const [timerActive, setTimerActive] = useState<boolean>(false)
  const [currentTime, setCurrentTime] = useState<number>(0)
  const [loading, setLoading] = useState(false)
  const [timer, setTimer] = useState<number | undefined>()

  useEffect(() => {
    if (timerActive && !timer) {
      setTimer(
        window.setTimeout(() => {
          setTimer(undefined)
          if (!finishTime) {
            setCurrentTime(prev => prev + 100)
          } else {
            setCurrentTime(finishTime)
          }
        }, 100)
      )
    }
  }, [timerActive, currentTime, finishTime])

  useEffect(() => {
    if (!raceStatus) {
      setLoading(true)
      backend.getCurrentRace()
        .then(cr => {
          console.log('current race:', cr)
          setRaceStatus({user: cr.userName, status: cr.raceStatus})
          setTimerActive(cr.raceStatus === 'STARTED')
          setCurrentTime(cr.splitTime || cr.startTime)
          setStartTime(cr.startTime)
          setSplitTime(cr.splitTime)
          setFinishTime(cr.finishTime)

        })
        .catch(() => onChangeView())
        .finally(() => setLoading(false))
    }
  }, [raceStatus, onChangeView])

  useEffect(() => {
    const sub = websocketSubject.asObservable()
      .subscribe(e => {
        switch (e.type) {
          case 'RACE_ARMED':
          case 'RACE_BAILED_OUT':
          case 'RACE_ABORTED':
            setSplitTime(undefined)
            setFinishTime(undefined)
            setStartTime(undefined)
            setCurrentTime(0)
            setTimerActive(false)
            setRaceStatus({status: e.type, user: e.user})
            break
          case 'RACE_STARTED':
            setTimerActive(true)
            setStartTime(e.startTime)
            setCurrentTime(e.startTime)
            break
          case 'SPLIT_TIME':
            setSplitTime(e.splitTime)
            setCurrentTime(e.splitTime)
            break
          case 'RACE_FINISHED':
            setTimerActive(false)
            setFinishTime(e.finishTime)
            setCurrentTime(e.finishTime)
            setRaceStatus({user: e.user, status: e.type})
            setTimeout(() => onChangeView(), 30000)
            break
          default:
          // do nothing
        }
      })
    return () => {
      sub.unsubscribe()
    }
  }, [onChangeView])

  return (
    <>
      {loading && <p className="loading-indicator">Laddar...</p>}
      {!loading &&
      <div className="currentrace d-flex flex-column align-items-center">
        <div className="container">
          <div className="row">
            <div className="col-6 text-right">Race status</div>
            <div className="col-6">{raceStatus?.status}</div>
          </div>
          <div className="row">
            <div className="col-6 text-right">User</div>
            <div className="col-6">{raceStatus?.user}</div>
          </div>
          {/*<div className="row">*/}
          {/*  <div className="col-6 text-right">Timer active</div>*/}
          {/*  <div className="col-6">{timerActive}</div>*/}
          {/*</div>*/}
          {/*<div className="row">*/}
          {/*  <div className="col-6 text-right">Current time</div>*/}
          {/*  <div className="col-6">{currentTime} {finishTime}</div>*/}
          {/*</div>*/}
          <div className="row">
            <div className="col-6 text-right">Elapsed time</div>
            <div className="col-6">{startTime && finishTime ?
              <Moment format="mm:ss:SSS">{(finishTime - startTime)}</Moment> :
              startTime && <Moment format="mm:ss:SSS">{(currentTime - startTime)}</Moment>}
            </div>
          </div>
          <div className="row">
            <div className="col-6 text-right">Split time</div>
            <div className="col-6">{splitTime && startTime &&
            <Moment format="mm:ss:SSS">{(splitTime - startTime)}</Moment>}</div>
          </div>
          <div className="row">
            <div className="col-6 text-right">Finish time</div>
            <div className="col-6">{finishTime && startTime &&
            <Moment format="mm:ss:SSS">{(finishTime - startTime)}</Moment>}</div>
          </div>
        </div>
      </div>
      }
    </>
  )
}

export default CurrentRace
