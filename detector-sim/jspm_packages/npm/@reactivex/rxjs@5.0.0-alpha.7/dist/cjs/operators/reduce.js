/* */ 
'use strict';
exports.__esModule = true;
exports['default'] = reduce;
var _reduceSupport = require('./reduce-support');
function reduce(project, acc) {
  return this.lift(new _reduceSupport.ReduceOperator(project, acc));
}
module.exports = exports['default'];
