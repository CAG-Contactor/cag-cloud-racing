export interface LeaderboardState {
    leaderboard: Array<Object>
}

const INIT_STATE: LeaderboardState = {
    leaderboard: []
};

const leaderboardReducer = (currentState: LeaderboardState = INIT_STATE, action: any) => {
    switch (action.type) {
        case "GET_LEADERBOARD":
            return {
                ...currentState,
                leaderboard: action.payload
            };
        default:
            return currentState;
    }
}

export default leaderboardReducer