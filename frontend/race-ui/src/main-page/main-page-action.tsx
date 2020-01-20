import { createAction } from "typesafe-actions";

export const getCurrentStatus = createAction('GetCurrentStatus')<string, string>();

export const setUser = createAction('SetUser')<string, string>();
