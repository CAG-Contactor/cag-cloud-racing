/* */ 
"format cjs";
import { NgZone } from 'angular2/src/core/zone/ng_zone';
import { isBlank, isPresent, assertionsEnabled } from 'angular2/src/facade/lang';
import { provide, Injector } from 'angular2/src/core/di';
import { APP_COMPONENT_REF_PROMISE, APP_COMPONENT, APP_ID_RANDOM_PROVIDER } from './application_tokens';
import { PromiseWrapper, ObservableWrapper } from 'angular2/src/facade/async';
import { ListWrapper } from 'angular2/src/facade/collection';
import { Reflector, reflector } from 'angular2/src/core/reflection/reflection';
import { TestabilityRegistry, Testability } from 'angular2/src/core/testability/testability';
import { DynamicComponentLoader } from 'angular2/src/core/linker/dynamic_component_loader';
import { BaseException, ExceptionHandler, unimplemented } from 'angular2/src/facade/exceptions';
import { DOM } from 'angular2/src/core/dom/dom_adapter';
import { internalView } from 'angular2/src/core/linker/view_ref';
import { IterableDiffers, defaultIterableDiffers, KeyValueDiffers, defaultKeyValueDiffers } from 'angular2/src/core/change_detection/change_detection';
import { AppViewPool, APP_VIEW_POOL_CAPACITY } from 'angular2/src/core/linker/view_pool';
import { AppViewManager } from 'angular2/src/core/linker/view_manager';
import { AppViewManagerUtils } from 'angular2/src/core/linker/view_manager_utils';
import { AppViewListener } from 'angular2/src/core/linker/view_listener';
import { ProtoViewFactory } from './linker/proto_view_factory';
import { ViewResolver } from './linker/view_resolver';
import { DirectiveResolver } from './linker/directive_resolver';
import { PipeResolver } from './linker/pipe_resolver';
import { Compiler } from 'angular2/src/core/linker/compiler';
import { DynamicComponentLoader_ } from "./linker/dynamic_component_loader";
import { AppViewManager_ } from "./linker/view_manager";
import { Compiler_ } from "./linker/compiler";
import { wtfLeave, wtfCreateScope } from './profile/profile';
import { PLATFORM_DIRECTIVES, PLATFORM_PIPES } from "angular2/src/core/platform_directives_and_pipes";
import { lockDevMode } from 'angular2/src/facade/lang';
import { COMMON_DIRECTIVES, COMMON_PIPES } from "angular2/common";
/**
 * Constructs the set of providers meant for use at the platform level.
 *
 * These are providers that should be singletons shared among all Angular applications
 * running on the page.
 */
export function platformProviders() {
    return [provide(Reflector, { useValue: reflector }), TestabilityRegistry];
}
/**
 * Construct providers specific to an individual root component.
 */
function _componentProviders(appComponentType) {
    return [
        provide(APP_COMPONENT, { useValue: appComponentType }),
        provide(APP_COMPONENT_REF_PROMISE, {
            useFactory: (dynamicComponentLoader, appRef, injector) => {
                // Save the ComponentRef for disposal later.
                var ref;
                // TODO(rado): investigate whether to support providers on root component.
                return dynamicComponentLoader.loadAsRoot(appComponentType, null, injector, () => { appRef._unloadComponent(ref); })
                    .then((componentRef) => {
                    ref = componentRef;
                    if (isPresent(componentRef.location.nativeElement)) {
                        injector.get(TestabilityRegistry)
                            .registerApplication(componentRef.location.nativeElement, injector.get(Testability));
                    }
                    return componentRef;
                });
            },
            deps: [DynamicComponentLoader, ApplicationRef, Injector]
        }),
        provide(appComponentType, {
            useFactory: (p) => p.then(ref => ref.instance),
            deps: [APP_COMPONENT_REF_PROMISE]
        }),
    ];
}
/**
 * Construct a default set of providers which should be included in any Angular
 * application, regardless of whether it runs on the UI thread or in a web worker.
 */
export function applicationCommonProviders() {
    return [
        provide(Compiler, { useClass: Compiler_ }),
        APP_ID_RANDOM_PROVIDER,
        AppViewPool,
        provide(APP_VIEW_POOL_CAPACITY, { useValue: 10000 }),
        provide(AppViewManager, { useClass: AppViewManager_ }),
        AppViewManagerUtils,
        AppViewListener,
        ProtoViewFactory,
        ViewResolver,
        provide(IterableDiffers, { useValue: defaultIterableDiffers }),
        provide(KeyValueDiffers, { useValue: defaultKeyValueDiffers }),
        DirectiveResolver,
        PipeResolver,
        provide(PLATFORM_PIPES, { useValue: COMMON_PIPES, multi: true }),
        provide(PLATFORM_DIRECTIVES, { useValue: COMMON_DIRECTIVES, multi: true }),
        provide(DynamicComponentLoader, { useClass: DynamicComponentLoader_ })
    ];
}
/**
 * Create an Angular zone.
 */
export function createNgZone() {
    return new NgZone({ enableLongStackTrace: assertionsEnabled() });
}
var _platform;
export function platformCommon(providers, initializer) {
    lockDevMode();
    if (isPresent(_platform)) {
        if (isBlank(providers)) {
            return _platform;
        }
        throw "platform() can only be called once per page";
    }
    if (isPresent(initializer)) {
        initializer();
    }
    if (isBlank(providers)) {
        providers = platformProviders();
    }
    _platform = new PlatformRef_(Injector.resolveAndCreate(providers), () => { _platform = null; });
    return _platform;
}
/**
 * The Angular platform is the entry point for Angular on a web page. Each page
 * has exactly one platform, and services (such as reflection) which are common
 * to every Angular application running on the page are bound in its scope.
 *
 * A page's platform is initialized implicitly when {@link bootstrap}() is called, or
 * explicitly by calling {@link platform}().
 */
export class PlatformRef {
    /**
     * Retrieve the platform {@link Injector}, which is the parent injector for
     * every Angular application on the page and provides singleton providers.
     */
    get injector() { return unimplemented(); }
    ;
}
export class PlatformRef_ extends PlatformRef {
    constructor(_injector, _dispose) {
        super();
        this._injector = _injector;
        this._dispose = _dispose;
        /** @internal */
        this._applications = [];
        /** @internal */
        this._disposeListeners = [];
    }
    registerDisposeListener(dispose) { this._disposeListeners.push(dispose); }
    get injector() { return this._injector; }
    application(providers) {
        var app = this._initApp(createNgZone(), providers);
        return app;
    }
    asyncApplication(bindingFn) {
        var zone = createNgZone();
        var completer = PromiseWrapper.completer();
        zone.run(() => {
            PromiseWrapper.then(bindingFn(zone), (providers) => {
                completer.resolve(this._initApp(zone, providers));
            });
        });
        return completer.promise;
    }
    _initApp(zone, providers) {
        var injector;
        var app;
        zone.run(() => {
            providers.push(provide(NgZone, { useValue: zone }));
            providers.push(provide(ApplicationRef, { useFactory: () => app, deps: [] }));
            var exceptionHandler;
            try {
                injector = this.injector.resolveAndCreateChild(providers);
                exceptionHandler = injector.get(ExceptionHandler);
                zone.overrideOnErrorHandler((e, s) => exceptionHandler.call(e, s));
            }
            catch (e) {
                if (isPresent(exceptionHandler)) {
                    exceptionHandler.call(e, e.stack);
                }
                else {
                    DOM.logError(e);
                }
            }
        });
        app = new ApplicationRef_(this, zone, injector);
        this._applications.push(app);
        return app;
    }
    dispose() {
        this._applications.forEach((app) => app.dispose());
        this._disposeListeners.forEach((dispose) => dispose());
        this._dispose();
    }
    /** @internal */
    _applicationDisposed(app) { ListWrapper.remove(this._applications, app); }
}
/**
 * A reference to an Angular application running on a page.
 *
 * For more about Angular applications, see the documentation for {@link bootstrap}.
 */
export class ApplicationRef {
    /**
     * Retrieve the application {@link Injector}.
     */
    get injector() { return unimplemented(); }
    ;
    /**
     * Retrieve the application {@link NgZone}.
     */
    get zone() { return unimplemented(); }
    ;
    /**
     * Get a list of component types registered to this application.
     */
    get componentTypes() { return unimplemented(); }
    ;
}
export class ApplicationRef_ extends ApplicationRef {
    constructor(_platform, _zone, _injector) {
        super();
        this._platform = _platform;
        this._zone = _zone;
        this._injector = _injector;
        /** @internal */
        this._bootstrapListeners = [];
        /** @internal */
        this._disposeListeners = [];
        /** @internal */
        this._rootComponents = [];
        /** @internal */
        this._rootComponentTypes = [];
        /** @internal */
        this._changeDetectorRefs = [];
        /** @internal */
        this._runningTick = false;
        /** @internal */
        this._enforceNoNewChanges = false;
        if (isPresent(this._zone)) {
            ObservableWrapper.subscribe(this._zone.onTurnDone, (_) => { this._zone.run(() => { this.tick(); }); });
        }
        this._enforceNoNewChanges = assertionsEnabled();
    }
    registerBootstrapListener(listener) {
        this._bootstrapListeners.push(listener);
    }
    registerDisposeListener(dispose) { this._disposeListeners.push(dispose); }
    registerChangeDetector(changeDetector) {
        this._changeDetectorRefs.push(changeDetector);
    }
    unregisterChangeDetector(changeDetector) {
        ListWrapper.remove(this._changeDetectorRefs, changeDetector);
    }
    bootstrap(componentType, providers) {
        var completer = PromiseWrapper.completer();
        this._zone.run(() => {
            var componentProviders = _componentProviders(componentType);
            if (isPresent(providers)) {
                componentProviders.push(providers);
            }
            var exceptionHandler = this._injector.get(ExceptionHandler);
            this._rootComponentTypes.push(componentType);
            try {
                var injector = this._injector.resolveAndCreateChild(componentProviders);
                var compRefToken = injector.get(APP_COMPONENT_REF_PROMISE);
                var tick = (componentRef) => {
                    this._loadComponent(componentRef);
                    completer.resolve(componentRef);
                };
                var tickResult = PromiseWrapper.then(compRefToken, tick);
                PromiseWrapper.then(tickResult, (_) => { });
                PromiseWrapper.then(tickResult, null, (err, stackTrace) => completer.reject(err, stackTrace));
            }
            catch (e) {
                exceptionHandler.call(e, e.stack);
                completer.reject(e, e.stack);
            }
        });
        return completer.promise;
    }
    /** @internal */
    _loadComponent(ref) {
        var appChangeDetector = internalView(ref.hostView).changeDetector;
        this._changeDetectorRefs.push(appChangeDetector.ref);
        this.tick();
        this._rootComponents.push(ref);
        this._bootstrapListeners.forEach((listener) => listener(ref));
    }
    /** @internal */
    _unloadComponent(ref) {
        if (!ListWrapper.contains(this._rootComponents, ref)) {
            return;
        }
        this.unregisterChangeDetector(internalView(ref.hostView).changeDetector.ref);
        ListWrapper.remove(this._rootComponents, ref);
    }
    get injector() { return this._injector; }
    get zone() { return this._zone; }
    tick() {
        if (this._runningTick) {
            throw new BaseException("ApplicationRef.tick is called recursively");
        }
        var s = ApplicationRef_._tickScope();
        try {
            this._runningTick = true;
            this._changeDetectorRefs.forEach((detector) => detector.detectChanges());
            if (this._enforceNoNewChanges) {
                this._changeDetectorRefs.forEach((detector) => detector.checkNoChanges());
            }
        }
        finally {
            this._runningTick = false;
            wtfLeave(s);
        }
    }
    dispose() {
        // TODO(alxhub): Dispose of the NgZone.
        this._rootComponents.forEach((ref) => ref.dispose());
        this._disposeListeners.forEach((dispose) => dispose());
        this._platform._applicationDisposed(this);
    }
    get componentTypes() { return this._rootComponentTypes; }
}
/** @internal */
ApplicationRef_._tickScope = wtfCreateScope('ApplicationRef#tick()');
//# sourceMappingURL=application_ref.js.map