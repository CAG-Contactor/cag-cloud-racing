import { combineReducers } from 'redux';
import loginReducer from "./loginReducer"
import queueReducer from "./queueReducer"

const rootReducer = combineReducers({
    loginState: loginReducer,
    queueState: queueReducer
})

export default rootReducer