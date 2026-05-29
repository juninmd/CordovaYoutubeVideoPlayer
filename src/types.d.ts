type ExecCallback = (message?: string) => void;

interface CordovaExec {
  (
    successCallback: ExecCallback,
    errorCallback: ExecCallback,
    service: string,
    action: string,
    args: string[],
  ): void;
}

declare module 'cordova/exec' {
  const exec: CordovaExec;
  export = exec;
}
