// import * as cookies from 'browser-cookies'
// import * as fetchIntercept from 'fetch-intercept'
import { UserResult } from '../leaderboard/Leaderboard'

// fetchIntercept.register({
//   request: (url: string, init: RequestInit) => {
//     init = init || {};
//     init.headers = init.headers || {};
//     if (cookies.get('x-authtoken')) {
//       init.headers['x-authtoken'] = cookies.get('x-authtoken');
//     }
//     init.mode = 'cors';
//     init.cache = 'no-cache';
//     init.credentials = 'include';
//     return [url, init];
//   },
//   response: (r: Response) => {
//     if (!r.ok) {
//       throw new Error(`Error in call to the clientapi: ${r.status}: ${r.statusText}`);
//     } else {
//       return r;
//     }
//   }
// });

export class ClientApi {

  constructor(private readonly clientApiBaseUrl: string = 'http://localhost:10580') {
  }

  fetchLeaderboard(): Promise<UserResult[]> {
    return fetch(`${this.clientApiBaseUrl}/leader-board`)
      .then(r => r.json());
  }
  
  fetchRaceStatus(): Promise<UserResult[]> {
    return fetch(`${this.clientApiBaseUrl}/current-race`)
      .then(r => r.json());
  }

  getLastRace(): Promise<any[]> {
    return fetch(`${this.clientApiBaseUrl}/lastrace`)
      .then(r => r.json());
  }
}
