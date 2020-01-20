import { createAction } from "typesafe-actions";
import { RaceStatus } from './currentrace'

export const getRaceStatus = createAction('GetRaceStatus')<RaceStatus, string>();

export const setRunningTime = createAction('SetRunningTime')<string>();

export const setFinishTime = createAction('SetFinishTime')<string>();

export const setSplitTime = createAction('SetSplitTime')<string>();

export const setUsername = createAction('SetUsername')<string>();

export const getLastRace = createAction('GetLastRace')<string>();

export const setRaceEvent = createAction('SetRaceEvent')<string>();

export const setStartTime = createAction('SetStartTime')<string>();

