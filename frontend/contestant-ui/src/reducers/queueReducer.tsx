export interface QueueState {
    queue: Array<Object>
}

const INIT_STATE: QueueState = {
    queue: []
};

const queueReducer = (currentState: QueueState = INIT_STATE, action: any) => {
    switch (action.type) {
        case "GET_QUEUE":
            return {
                ...currentState,
                queue: action.payload
            };
        default:
            return currentState;
    }
}

export default queueReducer