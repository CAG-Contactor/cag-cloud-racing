/* */ 
'use strict';
var __decorate = (this && this.__decorate) || function(decorators, target, key, desc) {
  if (typeof Reflect === "object" && typeof Reflect.decorate === "function")
    return Reflect.decorate(decorators, target, key, desc);
  switch (arguments.length) {
    case 2:
      return decorators.reduceRight(function(o, d) {
        return (d && d(o)) || o;
      }, target);
    case 3:
      return decorators.reduceRight(function(o, d) {
        return (d && d(target, key)), void 0;
      }, void 0);
    case 4:
      return decorators.reduceRight(function(o, d) {
        return (d && d(target, key, o)) || o;
      }, desc);
  }
};
var __metadata = (this && this.__metadata) || function(k, v) {
  if (typeof Reflect === "object" && typeof Reflect.metadata === "function")
    return Reflect.metadata(k, v);
};
var angular2_1 = require('../../angular2');
var lang_1 = require('../facade/lang');
var router_1 = require('./router');
var location_1 = require('./location');
var instruction_1 = require('./instruction');
var RouterLink = (function() {
  function RouterLink(_router, _location) {
    this._router = _router;
    this._location = _location;
  }
  Object.defineProperty(RouterLink.prototype, "isRouteActive", {
    get: function() {
      return this._router.isRouteActive(this._navigationInstruction);
    },
    enumerable: true,
    configurable: true
  });
  Object.defineProperty(RouterLink.prototype, "routeParams", {
    set: function(changes) {
      this._routeParams = changes;
      this._navigationInstruction = this._router.generate(this._routeParams);
      var navigationHref = instruction_1.stringifyInstruction(this._navigationInstruction);
      this.visibleHref = this._location.prepareExternalUrl(navigationHref);
    },
    enumerable: true,
    configurable: true
  });
  RouterLink.prototype.onClick = function() {
    if (!lang_1.isString(this.target) || this.target == '_self') {
      this._router.navigateByInstruction(this._navigationInstruction);
      return false;
    }
    return true;
  };
  RouterLink = __decorate([angular2_1.Directive({
    selector: '[router-link]',
    inputs: ['routeParams: routerLink', 'target: target'],
    host: {
      '(click)': 'onClick()',
      '[attr.href]': 'visibleHref',
      '[class.router-link-active]': 'isRouteActive'
    }
  }), __metadata('design:paramtypes', [router_1.Router, location_1.Location])], RouterLink);
  return RouterLink;
})();
exports.RouterLink = RouterLink;
