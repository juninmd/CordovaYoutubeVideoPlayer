/// <reference types="node" />

declare module 'cordova/exec' {
    function exec(
        successCallback: (result: unknown) => void,
        errorCallback: (error: unknown) => void,
        service: string,
        action: string,
        args?: unknown[]
    ): void;

    export default exec;
}