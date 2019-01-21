import { Action } from '@ngrx/store';

export const START_LOADING = '[UI] Set Is Loading';
export const STOP_LOADING = '[UI] Set Finished Loading';
export const START_DOWNLOADING = '[UI] Set Is Downloading';
export const STOP_DOWNLOADING = '[UI] Set Finished Downloading';

export class IsLoading implements Action {
  readonly type = START_LOADING;
}

export class FinishedLoading implements Action {
  readonly type = STOP_LOADING;
}

export class IsDownloading implements Action {
  readonly type = START_DOWNLOADING;
}

export class FinishedDownloading implements Action {
  readonly type = STOP_DOWNLOADING;
}

export type uiActions = IsLoading | FinishedLoading | IsDownloading | FinishedDownloading; 