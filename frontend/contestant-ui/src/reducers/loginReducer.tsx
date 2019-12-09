export interface LoginState {
    isLoggedIn: boolean;
    user: object;
}

const INIT_STATE: LoginState = {
    isLoggedIn: false,
    user: { token: "", userName: "" }
};

const loginReducer = (currentState: LoginState = INIT_STATE, action: any) => {
    switch (action.type) {
        case "AUTH":
            return {
                ...currentState,
                isLoggedIn: action.payload.isLoggedIn,
                user: action.payload.user
            };
        default:
            return currentState;
    }
}

export default loginReducer