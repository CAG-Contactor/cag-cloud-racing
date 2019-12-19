import { combineReducers } from 'redux';
import loginReducer from "./loginReducer"
import queueReducer from "./queueReducer"
import leaderboardReducer from './leaderboardReducer';

const rootReducer = combineReducers({
    loginState: loginReducer,
    queueState: queueReducer,
    leaderboardState: leaderboardReducer
})

export default rootReducer