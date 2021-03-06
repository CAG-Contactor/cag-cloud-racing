/* */ 
"format cjs";
import Subscriber from '../Subscriber';
import tryCatch from '../util/tryCatch';
import { errorObject } from '../util/errorObject';
import bindCallback from '../util/bindCallback';
import EmptyError from '../util/EmptyError';
export default function single(predicate, thisArg) {
    return this.lift(new SingleOperator(predicate, thisArg, this));
}
class SingleOperator {
    constructor(predicate, thisArg, source) {
        this.predicate = predicate;
        this.thisArg = thisArg;
        this.source = source;
    }
    call(subscriber) {
        return new SingleSubscriber(subscriber, this.predicate, this.thisArg, this.source);
    }
}
class SingleSubscriber extends Subscriber {
    constructor(destination, predicate, thisArg, source) {
        super(destination);
        this.thisArg = thisArg;
        this.source = source;
        this.seenValue = false;
        this.index = 0;
        if (typeof predicate === 'function') {
            this.predicate = bindCallback(predicate, thisArg, 3);
        }
    }
    applySingleValue(value) {
        if (this.seenValue) {
            this.destination.error('Sequence contains more than one element');
        }
        else {
            this.seenValue = true;
            this.singleValue = value;
        }
    }
    _next(value) {
        const predicate = this.predicate;
        const currentIndex = this.index++;
        if (predicate) {
            let result = tryCatch(predicate)(value, currentIndex, this.source);
            if (result === errorObject) {
                this.destination.error(result.e);
            }
            else if (result) {
                this.applySingleValue(value);
            }
        }
        else {
            this.applySingleValue(value);
        }
    }
    _complete() {
        const destination = this.destination;
        if (this.index > 0) {
            destination.next(this.seenValue ? this.singleValue : undefined);
            destination.complete();
        }
        else {
            destination.error(new EmptyError);
        }
    }
}
//# sourceMappingURL=single.js.map