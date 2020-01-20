import { createAction } from "typesafe-actions";
import { BackendEvent } from './event'

export const backendEventChannelHasOpened = createAction('BackendEventChannelHasOpened')<void>();
export const backendEventChannelHasClosed = createAction('BackendEventChannelClosed')<void>();
export const backendEventChannelGotError = createAction('BackendEventChannelGotError')<Event>();
export const backendEventChannelReceivedMessage = createAction('BackendEventChannelReceivedMessage')<BackendEvent>();
