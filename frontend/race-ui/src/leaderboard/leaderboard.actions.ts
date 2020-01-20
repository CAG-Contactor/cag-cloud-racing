import { createAction } from "typesafe-actions";
import { UserResult } from "./Leaderboard";

export const getLeaderboard = createAction('GetLeaderboard')<UserResult[]>();
