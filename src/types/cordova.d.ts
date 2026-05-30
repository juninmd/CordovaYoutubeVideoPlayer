/// <reference types="node" />

declare module 'cordova/exec' {
    function exec(
        successCallback: (result: any) => void,
        errorCallback: (error: any) => void,
        service: string,
        action: string,
        args?: any[]
    ): void;
    
    export default exec;
}