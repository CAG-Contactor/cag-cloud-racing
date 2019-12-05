/* */ 
"format cjs";
import Subscriber from '../../Subscriber';
export default function isEmpty() {
    return this.lift(new IsEmptyOperator());
}
class IsEmptyOperator {
    call(observer) {
        return new IsEmptySubscriber(observer);
    }
}
class IsEmptySubscriber extends Subscriber {
    constructor(destination) {
        super(destination);
    }
    notifyComplete(isEmpty) {
        const destination = this.destination;
        destination.next(isEmpty);
        destination.complete();
    }
    _next(value) {
        this.notifyComplete(false);
    }
    _complete() {
        this.notifyComplete(true);
    }
}
//# sourceMappingURL=isEmpty.js.map